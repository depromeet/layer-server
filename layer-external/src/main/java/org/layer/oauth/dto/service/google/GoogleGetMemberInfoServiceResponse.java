package org.layer.oauth.dto.service.google;

import org.layer.oauth.dto.service.kakao.KakaoAccountServiceResponse;
import org.layer.oauth.dto.service.kakao.KakaoGetMemberInfoServiceResponse;

public record GoogleGetMemberInfoServiceResponse(String id, String email) {
}
