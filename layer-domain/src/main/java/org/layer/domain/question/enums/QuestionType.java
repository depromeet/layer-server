package org.layer.domain.question.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum QuestionType {
    /**
     * 질문(입력) 블록 종류
     * 1. 짧은 인풋
     * 2. 마크다운
     * 3. 레인저
     * 4. 콤보 박스
     * 5. 카드
     * 6. 숫자 인풋
     * <p>
     * 질문 블록 중 이산적인 블록 종류
     * 1. 레인저( 숫자 )
     * 2. 콤보 박스
     * 3. 카드
     */
    PLAIN_TEXT("짧은 입력", "plain_text", "single"),
    MARKDOWN("마크다운 입력", "markdown", "single"),
    RANGER("범위 지정", "range", "multi"),
    COMBOBOX("콤보 박스", "combobox", "multi"),
    CARD("카드 선택 입력", "card", "multi"),
    NUMBER("숫자 입력", "number", "single");

    private String description;
    private String style;
    private String type;
}
