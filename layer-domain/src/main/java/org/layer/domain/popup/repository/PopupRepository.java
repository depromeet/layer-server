package org.layer.domain.popup.repository;

import static org.layer.global.exception.PopupExceptionType.*;

import java.util.List;

import org.layer.domain.popup.entity.Popup;
import org.layer.domain.popup.exception.PopupException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PopupRepository extends JpaRepository<Popup, Long> {
    default Popup findByIdOrThrow(Long popupId) {
        return findById(popupId)
            .orElseThrow(() -> new PopupException(NOT_FOUND_POPUP));
    }

    List<Popup> findAllByIsActiveTrueOrderByCreatedAtDesc();
}
