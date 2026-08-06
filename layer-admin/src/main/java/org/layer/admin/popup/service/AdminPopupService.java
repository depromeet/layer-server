package org.layer.admin.popup.service;

import org.layer.admin.popup.controller.dto.AdminPopupRequest.CreatePopupRequest;
import org.layer.admin.popup.controller.dto.AdminPopupRequest.UpdatePopupActiveRequest;
import org.layer.admin.popup.controller.dto.AdminPopupRequest.UpdatePopupRequest;
import org.layer.admin.popup.controller.dto.AdminPopupResponse.PopupDetail;
import org.layer.admin.popup.controller.dto.AdminPopupResponse.PopupPage;
import org.layer.domain.popup.entity.Popup;
import org.layer.domain.popup.repository.PopupRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminPopupService {

    private final PopupRepository popupRepository;

    public PopupPage getPopups(int page, int size) {
        Page<Popup> popups = popupRepository.findAll(
            PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt")));
        return PopupPage.of(popups);
    }

    public PopupDetail getPopup(Long popupId) {
        Popup popup = popupRepository.findByIdOrThrow(popupId);
        return PopupDetail.of(popup);
    }

    @Transactional
    public Long createPopup(CreatePopupRequest request) {
        Popup popup = popupRepository.save(request.toEntity());
        return popup.getId();
    }

    @Transactional
    public void updatePopup(Long popupId, UpdatePopupRequest request) {
        Popup popup = popupRepository.findByIdOrThrow(popupId);
        popup.update(request.iconType(), request.title(), request.content(), request.moveButtonUrl(),
            request.isActive());
    }

    @Transactional
    public void updatePopupActive(Long popupId, UpdatePopupActiveRequest request) {
        Popup popup = popupRepository.findByIdOrThrow(popupId);
        popup.updateActive(request.isActive());
    }
}
