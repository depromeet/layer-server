package org.layer.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.layer.common.annotation.MemberId;
import org.layer.domain.form.controller.dto.response.FormGetResponse;
import org.layer.domain.member.controller.dto.UpdateMemberInfoRequest;
import org.layer.domain.member.controller.dto.UpdateMemberInfoResponse;
import org.springframework.http.ResponseEntity;

@Tag(name = "회원 서비스", description = "회원 관련 api")
public interface MemberApi {
    @Operation(summary = "회원 정보(이름, 프로필 사진) 수정", method = "POST", description = "회원의 이름과 프로필 사진(url)을 수정합니다.")
    ResponseEntity<UpdateMemberInfoResponse> updateMemberInfo(@MemberId Long memberId, @Valid UpdateMemberInfoRequest updateMemberInfoRequest);

}
