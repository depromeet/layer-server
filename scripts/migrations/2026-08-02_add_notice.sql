-- 공지사항(CMS) 기능 추가를 위한 스키마 변경
--
-- 이 프로젝트는 Flyway/Liquibase 같은 자동 마이그레이션 도구가 없고
-- hibernate.ddl-auto=validate 로 운영되므로, 새 애플리케이션을 배포하기 "전"에
-- 이 스크립트를 DB에 직접 실행해야 합니다. (순서: 이 SQL 실행 -> 배포)
--
-- 실행 대상: layer_dev, layer_prod 각각에 동일하게 실행

CREATE TABLE notice (
    id         BIGINT       NOT NULL AUTO_INCREMENT,
    title      VARCHAR(255) NOT NULL,
    content    LONGTEXT     NOT NULL,
    category   VARCHAR(20)  NOT NULL,          -- GENERAL / EVENT / UPDATE / MAINTENANCE
    is_pinned  TINYINT(1)   NOT NULL DEFAULT 0, -- 상단 고정 여부
    is_active  TINYINT(1)   NOT NULL DEFAULT 1, -- 노출 On/Off
    start_at   DATETIME     NULL,               -- 노출 시작일시 (NULL이면 즉시 노출)
    end_at     DATETIME     NULL,               -- 노출 종료일시 (NULL이면 무기한 노출)
    created_at DATETIME     NOT NULL,
    updated_at DATETIME     NOT NULL,
    PRIMARY KEY (id),
    KEY idx_notice_is_active_is_pinned (is_active, is_pinned)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
