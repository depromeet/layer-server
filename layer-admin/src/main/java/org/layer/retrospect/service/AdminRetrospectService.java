package org.layer.retrospect.service;

import lombok.RequiredArgsConstructor;
import org.layer.domain.retrospect.dto.AdminRetrospectCountGroupBySpaceGetResponse;
import org.layer.domain.retrospect.dto.AdminRetrospectGetResponse;
import org.layer.domain.retrospect.repository.RetrospectAdminRepository;
import org.layer.retrospect.controller.dto.AdminRetrospectCountGetResponse;
import org.layer.retrospect.controller.dto.AdminRetrospectsGetResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminRetrospectService {
    private final RetrospectAdminRepository retrospectAdminRepository;

    @Value("${admin.password}")
    private String password;



    public AdminRetrospectsGetResponse getRetrospectData(LocalDateTime startDate, LocalDateTime endDate, String requestPassword){

        if(!requestPassword.equals(password)){
            throw new IllegalArgumentException("비밀번호가 틀렸습니다.");
        }

        List<AdminRetrospectGetResponse> retrospects = retrospectAdminRepository.findAllByCreatedAtAfterAndCreatedAtBefore(startDate, endDate);

        return new AdminRetrospectsGetResponse(retrospects, retrospects.size());
    }



    public AdminRetrospectCountGetResponse getRetrospectCount(LocalDateTime startDate, LocalDateTime endDate, String requestPassword) {
        if(!requestPassword.equals(password)) {
            throw new IllegalArgumentException("비밀번호가 틀렸습니다.");
        }

        Long count = retrospectAdminRepository.countRetrospectsExceptForAdminSpace(startDate, endDate);
        return new AdminRetrospectCountGetResponse(count);
    }

    public List<AdminRetrospectCountGroupBySpaceGetResponse> getRetrospectCountGroupSpace(LocalDateTime startDate, LocalDateTime endDate, String requestPassword) {
        if(!requestPassword.equals(password)) {
            throw new IllegalArgumentException("비밀번호가 틀렸습니다.");
        }

        return retrospectAdminRepository.countRetrospectsGroupBySpace(startDate, endDate);
    }
}
