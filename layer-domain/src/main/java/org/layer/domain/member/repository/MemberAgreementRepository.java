package org.layer.domain.member.repository;

import org.layer.domain.member.entity.AgreementType;
import org.layer.domain.member.entity.MemberAgreement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberAgreementRepository extends JpaRepository<MemberAgreement, Long> {

    Optional<MemberAgreement> findByMemberIdAndAgreementType(Long memberId, AgreementType agreementType);

    List<MemberAgreement> findAllByMemberId(Long memberId);
}
