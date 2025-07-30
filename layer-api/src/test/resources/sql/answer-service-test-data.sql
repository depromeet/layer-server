INSERT INTO member (id, created_at, updated_at, email, member_role, name, profile_image_url, social_id, social_type, deleted_at)
VALUES
    (1, '2024-12-27 00:00:00', '2024-12-27 00:00:00', 'user1@example.com', 'USER', '홍길동', 'https://example.com/image1.png', 'social_id_1', 'KAKAO', NULL),
    (2, '2024-12-27 00:00:00', '2024-12-27 00:00:00', 'user2@example.com', 'USER', '김철수', 'https://example.com/image2.png', 'social_id_2', 'KAKAO', NULL);

INSERT INTO space (id, created_at, updated_at, banner_url, category, field_list, form_id, introduction, leader_id, name)
VALUES
    (1, '2024-12-27 00:00:00', '2024-12-27 00:00:00', 'https://example.com/banner1.jpg', 'INDIVIDUAL', 'EDUCATION,DEVELOPMENT', NULL, '개인 프로젝트를 위한 공간입니다.', 1, '개인 공간 1');

INSERT INTO member_space_relation (created_at, updated_at, member_id, space_id)
VALUES
    ('2024-12-27 00:00:00', '2024-12-27 00:00:00', 1, 1),
    ('2024-12-27 00:00:00', '2024-12-27 00:00:00', 2, 1);

INSERT INTO retrospect (id, created_at, updated_at, space_id, title, introduction, retrospect_status,
                        analysis_status, deadline)
VALUES
    (1, '2024-12-27 00:00:00', '2024-12-27 00:00:00', 1, '2024년 12월 회고', '2024년 12월 회고를 위한 공간입니다.', 'PROCEEDING',
     'NOT_STARTED', null);

INSERT INTO question (id, created_at, updated_at, content, form_id, question_order, question_owner, question_type, retrospect_id)
VALUES
    (1, '2024-12-27 00:00:00', '2024-12-27 00:00:00', '질문1', 1, 1, 'TEAM', 'NUMBER', 1),
    (2, '2024-12-27 00:00:00', '2024-12-27 00:00:00', '질문2', 1, 2, 'TEAM', 'RANGER', 1),
    (3, '2024-12-27 00:00:00', '2024-12-27 00:00:00', '질문3', 1, 3, 'TEAM', 'PLAIN_TEXT', 1),
    (4, '2024-12-27 00:00:00', '2024-12-27 00:00:00', '질문4', 1, 4, 'TEAM', 'PLAIN_TEXT', 1);

INSERT INTO answer (id, created_at, updated_at, answer_status, content, member_id, question_id, retrospect_id)
VALUES
    (1, '2024-12-27 00:00:00', '2024-12-27 00:00:00', 'DONE', '5', 1, 1, 1),
    (2, '2024-12-27 00:00:00', '2024-12-27 00:00:00', 'TEMPORARY', '80', 1, 2, 1),
    (3, '2024-12-27 00:00:00', '2024-12-27 00:00:00', 'DONE', '80', 1, 2, 1),
    (4, '2024-12-27 00:00:00', '2024-12-27 00:00:00', 'DONE', '답변1', 1, 3, 1),
    (5, '2024-12-27 00:00:00', '2024-12-27 00:00:00', 'DONE', '답변2', 1, 4, 1);

