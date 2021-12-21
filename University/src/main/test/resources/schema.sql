DROP TABLE IF EXISTS LESSONS;
DROP TABLE IF EXISTS STUDENTS;
DROP TABLE IF EXISTS COURSES;
DROP TABLE IF EXISTS TEACHERS;
DROP TABLE IF EXISTS GROUPS;
DROP TABLE IF EXISTS CLASSROOMS;
CREATE TABLE TEACHERS (id BIGSERIAL PRIMARY KEY, firstname varchar(255), lastname varchar(255), CONSTRAINT teachers_pkey PRIMARY KEY (id));
CREATE TABLE COURSES (id BIGSERIAL PRIMARY KEY, name varchar(255), teacher int REFERENCES TEACHERS(id));
CREATE TABLE GROUPS (id BIGSERIAL PRIMARY KEY, name varchar(255), CONSTRAINT groups_pkey PRIMARY KEY (id));
CREATE TABLE STUDENTS (id BIGSERIAL PRIMARY KEY, firstname varchar(255), lastname varchar(255), group_id integer, CONSTRAINT students_pkey PRIMARY KEY (id), CONSTRAINT FK_StudentGroup FOREIGN KEY (group_id) REFERENCES GROUPS(id));
CREATE TABLE CLASSROOMS (id BIGSERIAL PRIMARY KEY, name varchar(255));
CREATE TABLE LESSONS (id BIGSERIAL PRIMARY KEY, group_id int REFERENCES GROUPS(id), course int REFERENCES COURSES(id), classroom int REFERENCES CLASSROOMS(id), starttime bigint NOT NULL, duration bigint NOT NULL);