package org.layer.domain.auth.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ReissueTokenRequest(@JsonProperty("member_id") Long memberId) {
}
