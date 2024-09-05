package org.layer.domain.admin.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.layer.domain.admin.controller.dto.AdminRetrospectsGetResponse;
import org.layer.domain.admin.controller.dto.AdminSpacesGetResponse;
import org.layer.domain.retrospect.dto.AdminRetrospectGetResponse;
import org.layer.domain.retrospect.repository.RetrospectAdminRepository;
import org.layer.domain.space.dto.AdminSpaceGetResponse;
import org.layer.domain.space.entity.Space;
import org.layer.domain.space.repository.SpaceAdminRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

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
}
