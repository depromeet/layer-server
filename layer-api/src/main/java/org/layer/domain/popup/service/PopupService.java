package org.layer.domain.popup.service;

import org.layer.domain.popup.controller.dto.PopupResponse.PopupElement;
import org.layer.domain.popup.repository.PopupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PopupService {

    private final PopupRepository popupRepository;

    // 활성 팝업은 항상 최대 1개이므로, 없으면 null을 그대로 반환한다.
    public PopupElement getActivePopup() {
        return popupRepository.findByIsActiveTrue()
            .map(PopupElement::of)
            .orElse(null);
    }
}
