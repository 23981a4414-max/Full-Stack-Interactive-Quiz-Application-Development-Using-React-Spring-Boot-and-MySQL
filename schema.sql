-- Optional: Hibernate (ddl-auto=update) creates these automatically.
-- Provided for documentation / manual setup.
CREATE DATABASE IF NOT EXISTS quizdb;
USE quizdb;

CREATE TABLE users (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(50) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  role VARCHAR(20) NOT NULL
);

CREATE TABLE questions (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  text VARCHAR(500) NOT NULL,
  correct_index INT NOT NULL,
  category VARCHAR(255),
  explanation VARCHAR(500)
);

CREATE TABLE question_options (
  question_id BIGINT NOT NULL,
  position INT NOT NULL,
  option_text VARCHAR(300) NOT NULL,
  PRIMARY KEY (question_id, position),
  FOREIGN KEY (question_id) REFERENCES questions(id) ON DELETE CASCADE
);

CREATE TABLE quiz_results (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  score INT NOT NULL,
  total INT NOT NULL,
  taken_at DATETIME(6),
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
