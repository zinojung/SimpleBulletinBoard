INSERT INTO USER (ID, USER_ID, PASSWORD, NAME, EMAIL) VALUES (1, 'jinho', 'jinho', '진호', '123@123.com');
INSERT INTO USER (ID, USER_ID, PASSWORD, NAME, EMAIL) VALUES (2, 'hana', 'hana', '하나', '123@123.com');


INSERT INTO QUESTION (id, writer_id, title, contents, create_date) VALUES (1, 1, 'jinho test 제목입니다.', 'test내용입니다.', CURRENT_TIMESTAMP());
INSERT INTO QUESTION (id, writer_id, title, contents, create_date) VALUES (2, 2, 'hana test 제목입니다.', 'hana test내용입니다.', CURRENT_TIMESTAMP());
