SET MODE POSTGRESQL;
CREATE TABLE IF NOT EXISTS student (
  id                 NUMERIC(18) PRIMARY KEY,
  last_name          VARCHAR(256) NOT NULL,
  first_name         VARCHAR(256) NOT NULL,
  patronymic         VARCHAR(256) NOT NULL,
  date_of_birth      TIMESTAMP    NOT NULL,
  date_of_graduation TIMESTAMP,
  education_form     VARCHAR(1),
  graduation_type    VARCHAR(1),
  additional_info    TEXT
);

CREATE SEQUENCE seq_student START WITH 1;