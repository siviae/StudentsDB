CREATE TABLE IF NOT EXISTS student (
  id              NUMERIC(18) PRIMARY KEY,
  surname         VARCHAR(256) NOT NULL,
  name            VARCHAR(256) NOT NULL,
  patronymic      VARCHAR(256) NOT NULL,
  date_of_birth   DATE,
  additional_info TEXT
);