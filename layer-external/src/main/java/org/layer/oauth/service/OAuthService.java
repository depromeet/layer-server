package org.layer.oauth.service;

import org.layer.oauth.dto.service.MemberInfoServiceResponse;

public interface OAuthService {
    MemberInfoServiceResponse getMemberInfo(final String accessToken);

}
