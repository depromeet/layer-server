package org.layer.domain.space.entity;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonDeserialize
public enum SpaceField {
    PLANNER("PLANNER", "기획"),
    EDUCATION("EDUCATION", "교육"),
    DEVELOPMENT("DEVELOPMENT", "개발"),
    DESIGN("DESIGN", "디자인"),
    MANAGEMENT("MANAGEMENT", "운영 및 관리"),
    DATA_ANALYSIS("DATA_ANALYSIS", "데이터 분석"),
    MARKETING("MARKETING", "마케팅"),
    RESEARCH("RESEARCH", "연구"),
    ETC("ETC", "기타");


    private final String value;
    private final String name;
}
