package org.layer.oauth.dto.service.apple;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class ApplePublicKey {
    private final String kty;

    private final String kid;

    private final String use;

    private final String alg;

    private final String n;

    private final String e;

    public boolean isSameAlg(final String alg) {
        return this.alg.equals(alg);
    }

    public boolean isSameKid(final String kid) {
        return this.kid.equals(kid);
    }

    @JsonCreator
    public ApplePublicKey(@JsonProperty("kty") final String kty,
                          @JsonProperty("kid") final String kid,
                          @JsonProperty("use") final String use,
                          @JsonProperty("alg") final String alg,
                          @JsonProperty("n") final String n,
                          @JsonProperty("e") final String e) {
        this.kty = kty;
        this.kid = kid;
        this.use = use;
        this.alg = alg;
        this.n = n;
        this.e = e;
    }
}
