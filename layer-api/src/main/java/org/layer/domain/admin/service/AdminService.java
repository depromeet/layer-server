package org.layer.domain.admin.service;

import lombok.RequiredArgsConstructor;
import org.layer.domain.admin.controller.dto.AdminRetrospectCountGetResponse;
import org.layer.domain.admin.controller.dto.AdminRetrospectsGetResponse;
import org.layer.domain.admin.controller.dto.AdminSpaceCountGetResponse;
import org.layer.domain.admin.controller.dto.AdminSpacesGetResponse;
import org.layer.domain.retrospect.dto.AdminRetrospectCountGroupBySpaceGetResponse;
import org.layer.domain.retrospect.dto.AdminRetrospectGetResponse;
import org.layer.domain.retrospect.repository.RetrospectAdminRepository;
import org.layer.domain.space.dto.AdminSpaceGetResponse;
import org.layer.domain.space.repository.SpaceAdminRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService {

	private final SpaceAdminRepository spaceAdminRepository;
	private final RetrospectAdminRepository retrospectAdminRepository;

	@Value("${admin.password}")
	private String password;

	public AdminSpacesGetResponse getSpaceData(LocalDateTime startDate, LocalDateTime endDate, String requestPassword){

		if(!requestPassword.equals(password)){
			throw new IllegalArgumentException("비밀번호가 틀렸습니다.");
		}

		List<AdminSpaceGetResponse> spaces = spaceAdminRepository.findAllByCreatedAtAfterAndCreatedAtBefore(startDate, endDate);

		return new AdminSpacesGetResponse(spaces, spaces.size());
	}

	public AdminRetrospectsGetResponse getRetrospectData(LocalDateTime startDate, LocalDateTime endDate, String requestPassword){

		if(!requestPassword.equals(password)){
			throw new IllegalArgumentException("비밀번호가 틀렸습니다.");
		}

		List<AdminRetrospectGetResponse> retrospects = retrospectAdminRepository.findAllByCreatedAtAfterAndCreatedAtBefore(startDate, endDate);

		return new AdminRetrospectsGetResponse(retrospects, retrospects.size());
	}

	public AdminSpaceCountGetResponse getSpaceCount(LocalDateTime startDate, LocalDateTime endDate, String requestPassword) {
		if(!requestPassword.equals(password)) {
			throw new IllegalArgumentException("비밀번호가 틀렸습니다.");
		}

		Long count = spaceAdminRepository.countSpacesExceptForAdminSpace(startDate, endDate);
		return new AdminSpaceCountGetResponse(count);
	}

	public AdminRetrospectCountGetResponse getRetrospectCount(LocalDateTime startDate, LocalDateTime endDate, String requestPassword) {
		if(!requestPassword.equals(password)) {
			throw new IllegalArgumentException("비밀번호가 틀렸습니다.");
		}

		Long count = retrospectAdminRepository.countRetrospectsExceptForAdminSpace(startDate, endDate);
		return new AdminRetrospectCountGetResponse(count);
	}

	public AdminRetrospectCountGetResponse getRetrospectCountInSpace(LocalDateTime startDate, LocalDateTime endDate, Long spaceId, String requestPassword) {
		if(!requestPassword.equals(password)) {
			throw new IllegalArgumentException("비밀번호가 틀렸습니다.");
		}

		Long count = retrospectAdminRepository.countRetrospectsBySpaceId(spaceId, startDate, endDate);
		return new AdminRetrospectCountGetResponse(count);
	}

	public List<AdminRetrospectCountGroupBySpaceGetResponse> getRetrospectCountGroupSpace(LocalDateTime startDate, LocalDateTime endDate, String requestPassword) {
		if(!requestPassword.equals(password)) {
			throw new IllegalArgumentException("비밀번호가 틀렸습니다.");
		}

		return retrospectAdminRepository.countRetrospectsGroupBySpace(startDate, endDate);
	}
}
