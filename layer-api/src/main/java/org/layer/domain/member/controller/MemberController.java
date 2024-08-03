package org.layer.domain.member.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.layer.common.annotation.MemberId;
import org.layer.domain.member.controller.dto.UpdateMemberInfoRequest;
import org.layer.domain.member.controller.dto.UpdateMemberInfoResponse;
import org.layer.domain.member.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/member")
@RequiredArgsConstructor
@RestController
public class MemberController implements MemberApi {
    private final MemberService memberService;

    @Override
    @PatchMapping("/update-profile")
    public ResponseEntity<UpdateMemberInfoResponse> updateMemberInfo(@MemberId Long memberId, @Valid @RequestBody UpdateMemberInfoRequest updateMemberInfoRequest) {
        UpdateMemberInfoResponse response = memberService.updateMemberInfo(memberId, updateMemberInfoRequest);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
