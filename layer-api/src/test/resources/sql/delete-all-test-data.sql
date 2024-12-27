-- 모든 테이블의 데이터 삭제 (외래키 제약 조건 무시)
SET FOREIGN_KEY_CHECKS = 0;

DELETE FROM action_item;
DELETE FROM `analyze`;
DELETE FROM analyze_detail;
DELETE FROM answer;
DELETE FROM form;
DELETE FROM member;
DELETE FROM question;
DELETE FROM question_description;
DELETE FROM question_option;
DELETE FROM retrospect;
DELETE FROM space;
DELETE FROM member_space_relation;
DELETE FROM template_metadata;
DELETE FROM template_purpose;

-- AUTO_INCREMENT 초기화
ALTER TABLE action_item AUTO_INCREMENT = 1;
ALTER TABLE `analyze` AUTO_INCREMENT = 1;
ALTER TABLE analyze_detail AUTO_INCREMENT = 1;
ALTER TABLE answer AUTO_INCREMENT = 1;
ALTER TABLE form AUTO_INCREMENT = 1;
ALTER TABLE member AUTO_INCREMENT = 1;
ALTER TABLE question AUTO_INCREMENT = 1;
ALTER TABLE question_description AUTO_INCREMENT = 1;
ALTER TABLE question_option AUTO_INCREMENT = 1;
ALTER TABLE retrospect AUTO_INCREMENT = 1;
ALTER TABLE space AUTO_INCREMENT = 1;
ALTER TABLE member_space_relation AUTO_INCREMENT = 1;
ALTER TABLE template_metadata AUTO_INCREMENT = 1;
ALTER TABLE template_purpose AUTO_INCREMENT = 1;

-- 외래키 제약 조건 복구
SET FOREIGN_KEY_CHECKS = 1;

-- 테이블 초기화 완료