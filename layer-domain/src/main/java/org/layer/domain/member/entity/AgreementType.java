package org.layer.domain.member.entity;

public enum AgreementType {
    TERMS(true),
    PRIVACY(true),
    MARKETING(false);

    private final boolean required;

    AgreementType(boolean required) {
        this.required = required;
    }

    public boolean isRequired() {
        return required;
    }
}
