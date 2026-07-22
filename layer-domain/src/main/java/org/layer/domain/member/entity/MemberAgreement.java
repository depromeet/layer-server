package org.layer.domain.member.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.layer.domain.common.BaseTimeEntity;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"memberId", "agreementType"}))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberAgreement extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long memberId;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private AgreementType agreementType;

    @NotNull
    @Column(columnDefinition = "TINYINT(1)")
    private boolean agreed;

    // 동의한 시각. 미동의 상태면 null
    private LocalDateTime agreedAt;

    // 마지막으로 동의 여부를 물어본 시각 (선택 항목 재요청 주기 계산에 사용)
    private LocalDateTime lastAskedAt;

    @Builder(access = AccessLevel.PUBLIC)
    private MemberAgreement(Long memberId, AgreementType agreementType, boolean agreed,
                             LocalDateTime agreedAt, LocalDateTime lastAskedAt) {
        this.memberId = memberId;
        this.agreementType = agreementType;
        this.agreed = agreed;
        this.agreedAt = agreedAt;
        this.lastAskedAt = lastAskedAt;
    }

    public void updateAgreement(boolean agreed, LocalDateTime now) {
        this.agreed = agreed;
        this.agreedAt = agreed ? now : null;
        this.lastAskedAt = now;
    }
}
