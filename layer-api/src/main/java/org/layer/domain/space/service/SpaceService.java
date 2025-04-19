package org.layer.domain.space.service;

import static org.layer.global.exception.ApiMemberSpaceRelationExceptionType.*;
import static org.layer.global.exception.ApiSpaceExceptionType.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.layer.common.dto.Meta;
import org.layer.domain.actionItem.repository.ActionItemRepository;
import org.layer.domain.common.time.Time;
import org.layer.discord.event.CreateSpaceEvent;
import org.layer.domain.space.dto.SpaceMember;
import org.layer.domain.space.dto.SpaceWithMemberCount;
import org.layer.storage.service.StorageService;
import org.layer.domain.retrospect.repository.RetrospectRepository;
import org.layer.domain.space.controller.dto.SpaceRequest;
import org.layer.domain.space.controller.dto.SpaceResponse;
import org.layer.domain.space.entity.MemberSpaceRelation;
import org.layer.domain.space.entity.Space;
import org.layer.domain.space.entity.SpaceCategory;
import org.layer.domain.space.exception.MemberSpaceRelationException;
import org.layer.domain.space.exception.SpaceException;
import org.layer.domain.space.repository.MemberSpaceRelationRepository;
import org.layer.domain.space.repository.SpaceRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class SpaceService {
	private final StorageService storageService;
	private final SpaceRepository spaceRepository;
	private final MemberSpaceRelationRepository memberSpaceRelationRepository;
	private final ActionItemRepository actionItemRepository;
	private final RetrospectRepository retrospectRepository;

	private final ApplicationEventPublisher eventPublisher;

	private final Time time;

	public SpaceResponse.SpacePage getSpaceListFromMemberId(Long memberId, SpaceRequest.GetSpaceRequest request) {

		int pageSize = request.pageSize();
		List<SpaceWithMemberCount> spaces = spaceRepository.findAllSpacesByMemberIdAndCategoryAndCursor(
			memberId, request.cursorId(), request.category(), pageSize + 1 // hasNext 판단을 위해 +1
		);

		boolean hasNext = spaces.size() > pageSize;
		if (hasNext) {
			spaces.remove(spaces.size() - 1); // 다음 페이지 존재 시 마지막 요소 제거
		}

		Long nextCursor = spaces.isEmpty() ? null : spaces.get(spaces.size() - 1).getId();

		List<SpaceResponse.SpaceWithMemberCountInfo> responseList = spaces.stream()
			.map(SpaceResponse.SpaceWithMemberCountInfo::toResponse)
			.toList();

		Meta meta = Meta.builder()
			.cursor(hasNext ? nextCursor : null)
			.hasNextPage(hasNext)
			.build();

		return SpaceResponse.SpacePage.toResponse(responseList, meta);
	}

	@Transactional
	public Long createSpace(Long memberId, SpaceRequest.CreateSpaceRequest createSpaceRequest) {
		if (createSpaceRequest.bannerUrl() != null) {
			storageService.checkObjectExistOrThrow(createSpaceRequest.bannerUrl());
		}
		Space newSpace = spaceRepository.save(createSpaceRequest.toEntity(memberId));
		MemberSpaceRelation memberSpaceRelation = MemberSpaceRelation.builder()
			.memberId(memberId)
			.space(newSpace)
			.build();

		memberSpaceRelationRepository.save(memberSpaceRelation);

		publishCreateSpaceEvent(newSpace, memberId);
		return newSpace.getId();
	}

	private void publishCreateSpaceEvent(final Space space, final Long memberId) {
		eventPublisher.publishEvent(CreateSpaceEvent.of(
			space.getName(),
			memberId,
			time.now()
		));
	}

	@Transactional
	public void updateSpace(Long memberId, SpaceRequest.UpdateSpaceRequest updateSpaceRequest) {
		Space space = spaceRepository.findByIdOrThrow(updateSpaceRequest.id());
		space.updateSpace(memberId, updateSpaceRequest.bannerUrl(), updateSpaceRequest.name(),
			updateSpaceRequest.introduction());
	}

	public SpaceResponse.SpaceWithMemberCountInfo getSpaceById(Long memberId, Long spaceId) {
		var foundSpace = spaceRepository.findByIdAndJoinedMemberId(spaceId, memberId)
			.orElseThrow(() -> new SpaceException(NOT_FOUND_SPACE));
		return SpaceResponse.SpaceWithMemberCountInfo.toResponse(foundSpace);
	}

	public SpaceResponse.SpaceWithMemberCountInfo getPublicSpaceById(Long spaceId) {
		var foundSpace = spaceRepository.findByIdAndJoinedMemberId(spaceId)
			.orElseThrow(() -> new SpaceException(NOT_FOUND_SPACE));

		return SpaceResponse.SpaceWithMemberCountInfo.toResponse(foundSpace);
	}

	@Transactional
	public void createMemberSpace(Long memberId, Long spaceId) {

        /*
          존재하는 스페이스 여부 확인
         */
		var foundSpace = spaceRepository.findById(spaceId)
			.orElseThrow(() -> new SpaceException(NOT_FOUND_SPACE)
			);

		if (foundSpace.getCategory() == SpaceCategory.INDIVIDUAL) {
			throw new SpaceException(NOT_FOUND_SPACE);
		}


        /*
          이미 참여중인 스페이스 여부 확인
         */
		memberSpaceRelationRepository.findBySpaceIdAndMemberId(spaceId, memberId).ifPresent(it -> {
			throw new SpaceException(SPACE_ALREADY_JOINED);
		});

        /*
          스페이스 참여여부 저장
         */
		var joinedSpace = MemberSpaceRelation.builder()
			.space(foundSpace)
			.memberId(memberId)
			.build();
		memberSpaceRelationRepository.save(joinedSpace);
	}

	@Transactional
	public void removeMemberSpace(Long memberId, Long spaceId) {
        /*
          존재하는 스페이스 여부 확인
         */
		var foundSpace = spaceRepository.findById(spaceId)
			.orElseThrow(() -> new SpaceException(NOT_FOUND_SPACE)
			);

        /*
          스페이스 팀장 여부 확인
         */
		if (foundSpace.getLeaderId().equals(memberId)) {
			throw new SpaceException(SPACE_LEADER_CANNOT_LEAVE);
		}


        /*
          개인 스페이스의 경우, 이탈 시 Space 엔티티의 로직이 된다.
          따라서 INDIVIDUAL인 Space인 경우 가능한 케이스는 2가지로
          1. Space 삭제
          2. 속한 스페이스 떠나기, 스페이스 삭제하기 분리
          현재는 2번 케이스를 기준으로 구현되어 있음
         */
		if (foundSpace.getCategory() == SpaceCategory.INDIVIDUAL) {
			throw new SpaceException(NOT_FOUND_SPACE);
		}

        /*
          이미 참여중인 스페이스 여부 확인
         */
		var foundMemberSpaceRelation = memberSpaceRelationRepository.findBySpaceIdAndMemberId(spaceId, memberId)
			.orElseThrow(
				() -> new SpaceException(SPACE_ALREADY_JOINED)
			);

        /*
          스페이스 참여여부 삭제 ( 스페이스 떠나기 )
         */
		memberSpaceRelationRepository.deleteById(foundMemberSpaceRelation.getId());
	}

	@Transactional
	public void changeSpaceLeader(Long leaderId, Long spaceId, Long memberId) {
		// 스페이스 리더 여부 확인
		var foundSpace = checkLeaderFromSpace(spaceId, leaderId);

		// 스페이스 존재하는 멤버 확인
		memberSpaceRelationRepository.findBySpaceIdAndMemberId(spaceId, memberId)
			.orElseThrow(() -> new MemberSpaceRelationException(NOT_FOUND_MEMBER_SPACE_RELATION));

		foundSpace.changeLeader(memberId);
	}

	@Transactional
	public void kickMemberFromSpace(Long leaderId, Long spaceId, Long memberId) {
		// 스페이스 리더 여부 확인
		checkLeaderFromSpace(spaceId, leaderId);

		// 스페이스 존재하는 멤버 확인
		var foundTeamByMemberId = memberSpaceRelationRepository.findBySpaceIdAndMemberId(spaceId, memberId)
			.orElseThrow(() -> new MemberSpaceRelationException(NOT_FOUND_MEMBER_SPACE_RELATION));

		// 팀에서 삭제하기
		memberSpaceRelationRepository.delete(foundTeamByMemberId);
	}

	public List<SpaceResponse.SpaceMemberResponse> getSpaceMembers(Long memberId, Long spaceId) {
        /*
            스페이스 소속 여부 조회
         */
		var isExist = findSpaceByIdAndJoinedMemberId(spaceId, memberId);
		if (isExist.isEmpty()) {
			throw new SpaceException(NOT_FOUND_SPACE);
		}

		List<SpaceMember> SpaceMembers = spaceRepository.findAllSpaceMemberBySpaceIdWithIsLeader(spaceId);
		return SpaceMembers.stream()
			.filter(a -> a.getDeletedAt() == null)
			.map(SpaceResponse.SpaceMemberResponse::toResponse)
			.sorted(
				Comparator.comparing(SpaceResponse.SpaceMemberResponse::isLeader).reversed())
			.toList();
	}

	private Optional<MemberSpaceRelation> findSpaceByIdAndJoinedMemberId(Long spaceId, Long memberId) {
        /*
          이미 참여중인 스페이스 여부 확인
         */
		return memberSpaceRelationRepository.findBySpaceIdAndMemberId(spaceId, memberId);
	}

	@Transactional
	public void removeSpace(Long spaceId, Long leaderId) {
		// 스페이스 리더 여부 확인
		var foundSpace = checkLeaderFromSpace(spaceId, leaderId);

		// 액션 아이템 삭제
		actionItemRepository.deleteAllBySpaceId(spaceId);

		// 진행중인 회고 삭제
		retrospectRepository.deleteAllBySpaceId(spaceId);

		memberSpaceRelationRepository.deleteAllBySpaceIdInBatch(foundSpace.getId());
		spaceRepository.delete(foundSpace);
	}

	/**
	 * 스페이스 리더 여부 확인
	 *
	 * @param spaceId  스페이스 아이디
	 * @param leaderId 확인하고자 하는 리더 아이디
	 */
	private Space checkLeaderFromSpace(Long spaceId, Long leaderId) {
		var foundSpace = spaceRepository.findById(spaceId).orElseThrow(() -> new SpaceException(NOT_FOUND_SPACE));
		foundSpace.isLeaderSpace(leaderId);

		return foundSpace;

	}

}
