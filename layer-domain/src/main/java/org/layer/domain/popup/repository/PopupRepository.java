package org.layer.domain.popup.repository;

import static org.layer.global.exception.PopupExceptionType.*;

import java.util.Optional;

import org.layer.domain.popup.entity.Popup;
import org.layer.domain.popup.exception.PopupException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface PopupRepository extends JpaRepository<Popup, Long> {
    default Popup findByIdOrThrow(Long popupId) {
        return findById(popupId)
            .orElseThrow(() -> new PopupException(NOT_FOUND_POPUP));
    }

    // 팝업은 항상 최대 1개만 활성 상태여야 한다. 하나를 활성화하기 전에 호출해서 나머지를 일괄 비활성화한다.
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE Popup p SET p.isActive = false WHERE p.isActive = true")
    void deactivateAll();

    Optional<Popup> findByIsActiveTrue();
}
