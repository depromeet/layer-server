package org.layer.domain.form.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum FormTag {
    KPT("KPT"),
    FIVE_F("5F"),
    MAD_SAD_GLAD("Mad Sad Glad"),
    SSC("SSC"),
    PMI("PMI"),
    UNTITLED("무제"),
    CUSTOM("CUSTOM");

    private final String tag;
}
