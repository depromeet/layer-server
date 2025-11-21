package org.layer.domain.actionItem.service;

import java.util.Comparator;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.layer.domain.actionItem.controller.dto.request.ActionItemUpdateRequest;
import org.layer.domain.actionItem.entity.ActionItem;
import org.layer.domain.actionItem.repository.ActionItemRepository;
import org.layer.domain.fixture.MemberFixture;
import org.layer.domain.fixture.RetrospectFixture;
import org.layer.domain.fixture.SpaceFixture;
import org.layer.domain.member.entity.Member;
import org.layer.domain.member.repository.MemberRepository;
import org.layer.domain.retrospect.entity.AnalysisStatus;
import org.layer.domain.retrospect.entity.Retrospect;
import org.layer.domain.retrospect.entity.RetrospectStatus;
import org.layer.domain.retrospect.repository.RetrospectRepository;
import org.layer.domain.space.entity.MemberSpaceRelation;
import org.layer.domain.space.entity.Space;
import org.layer.domain.space.repository.MemberSpaceRelationRepository;
import org.layer.domain.space.repository.SpaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ActionItemServiceTest {

	@Autowired
	private ActionItemService actionItemService;

	@Autowired
	private ActionItemRepository actionItemRepository;

	@Autowired
	private RetrospectRepository retrospectRepository;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private SpaceRepository spaceRepository;

	@Autowired
	private MemberSpaceRelationRepository memberSpaceRelationRepository;

	@Nested
	class 실행_목표_수정 {

		@Test
		@DisplayName("요청 리스트에 따라 기존 항목 삭제, 내용 수정, 신규 생성이 수행되고 순서가 재정렬된다.")
		void updateActionItems_Sync_Test() {
			// given
			// 1. 리더 멤버 및 스페이스 생성
			Member leader = memberRepository.save(MemberFixture.createFixture("social-leader"));
			Space space = spaceRepository.save(SpaceFixture.createFixture(leader.getId(), 1L));
			memberSpaceRelationRepository.save(new MemberSpaceRelation(leader.getId(), space));

			// 2. 회고 생성
			Retrospect retrospect = retrospectRepository.save(RetrospectFixture.createFixture(
				space.getId(), RetrospectStatus.PROCEEDING, AnalysisStatus.NOT_STARTED, null
			));

			// 3. [DB 상태] 기존 실행 목표 3개 저장 (A, B, C)
			ActionItem itemA = actionItemRepository.save(createActionItem(retrospect, space, leader, "목표 A", 1));
			ActionItem itemB = actionItemRepository.save(createActionItem(retrospect, space, leader, "목표 B", 2));
			ActionItem itemC = actionItemRepository.save(createActionItem(retrospect, space, leader, "목표 C", 3));

			// 4. [요청 생성] A, C 삭제 / B 수정 / D 신규 생성
			// 기대 결과 순서: 1. B(수정됨) -> 2. D(신규)
			List<ActionItemUpdateRequest.ActionItemUpdateElementRequest> updateElements = List.of(
				// B 수정 (ID 유지)
				new ActionItemUpdateRequest.ActionItemUpdateElementRequest(itemB.getId(), "목표 B 수정"),
				// D 생성 (ID null)
				new ActionItemUpdateRequest.ActionItemUpdateElementRequest(null, "목표 D 신규")
			);
			ActionItemUpdateRequest request = new ActionItemUpdateRequest(updateElements);

			// when
			// (서비스 메서드 호출)
			actionItemService.updateActionItems(leader.getId(), retrospect.getId(), request);

			// then
			List<ActionItem> results = actionItemRepository.findAllByRetrospectId(retrospect.getId());

			// 1. 개수 검증 (A, C 삭제, B 유지, D 생성 -> 총 2개)
			Assertions.assertThat(results).hasSize(2);

			// 순서대로 정렬하여 검증
			results.sort(Comparator.comparingInt(ActionItem::getActionItemOrder));
			ActionItem firstItem = results.get(0);
			ActionItem secondItem = results.get(1);

			// 2. 첫 번째 아이템 검증 (B 수정 확인)
			Assertions.assertThat(firstItem.getId()).isEqualTo(itemB.getId());
			Assertions.assertThat(firstItem.getContent()).isEqualTo("목표 B 수정");
			Assertions.assertThat(firstItem.getActionItemOrder()).isEqualTo(1);

			// 3. 두 번째 아이템 검증 (D 생성 확인)
			Assertions.assertThat(secondItem.getId()).isNotEqualTo(itemA.getId()); // A가 아님
			Assertions.assertThat(secondItem.getId()).isNotEqualTo(itemC.getId()); // C가 아님
			Assertions.assertThat(secondItem.getContent()).isEqualTo("목표 D 신규");
			Assertions.assertThat(secondItem.getActionItemOrder()).isEqualTo(2);
		}

		// 테스트용 헬퍼 메서드 (Fixture가 없다면 간편하게 사용)
		private ActionItem createActionItem(Retrospect retrospect, Space space, Member member, String content, int order) {
			return ActionItem.builder()
				.retrospectId(retrospect.getId())
				.spaceId(space.getId())
				.memberId(member.getId())
				.content(content)
				.actionItemOrder(order)
				.build();
		}
	}
}
