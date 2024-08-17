package org.layer.external.google.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SheetType {

    FEEDBACK("feedback", "A:H"),
    WITHDRAW("withdraw", "A:H");

    private String sheetName;
    private String Columns;


}
