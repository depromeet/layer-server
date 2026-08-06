package org.layer.domain.popup.service;

import org.layer.domain.popup.controller.dto.PopupResponse.PopupListResponse;
import org.layer.domain.popup.repository.PopupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PopupService {

    private final PopupRepository popupRepository;

    public PopupListResponse getExposedPopups() {
        return PopupListResponse.of(popupRepository.findAllByIsActiveTrueOrderByCreatedAtDesc());
    }
}
