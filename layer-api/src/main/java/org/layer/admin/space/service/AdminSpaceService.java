package org.layer.admin.space.service;

import lombok.RequiredArgsConstructor;

import org.layer.admin.space.controller.dto.AdminSpaceCountGetResponse;
import org.layer.admin.space.controller.dto.AdminSpacesGetResponse;
import org.layer.admin.retrospect.controller.dto.AdminRetrospectCountGetResponse;
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
public class AdminSpaceService {

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

    public AdminSpaceCountGetResponse getSpaceCount(LocalDateTime startDate, LocalDateTime endDate, String requestPassword) {
        if(!requestPassword.equals(password)) {
            throw new IllegalArgumentException("비밀번호가 틀렸습니다.");
        }

        Long count = spaceAdminRepository.countSpacesExceptForAdminSpace(startDate, endDate);
        return new AdminSpaceCountGetResponse(count);
    }

    public AdminRetrospectCountGetResponse getRetrospectCountInSpace(LocalDateTime startDate, LocalDateTime endDate, Long spaceId, String requestPassword) {
        if(!requestPassword.equals(password)) {
            throw new IllegalArgumentException("비밀번호가 틀렸습니다.");
        }

        Long count = retrospectAdminRepository.countRetrospectsBySpaceId(spaceId, startDate, endDate);
        return new AdminRetrospectCountGetResponse(count);
    }
}
