package org.layer.domain.form.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FormPublishedBy {

    ADMIN("관리자","0"),
    PERSONAL("개인","1");

    private String description;
    private String code;

}
