package org.layer.oauth.dto.service;

public record KakaoGetMemberInfoServiceResponse(String id, KakaoAccountServiceResponse kakao_account) {
    public static KakaoGetMemberInfoServiceResponse of(String id, KakaoAccountServiceResponse kakao_account) {
        return new KakaoGetMemberInfoServiceResponse(id, kakao_account);
    }

}
