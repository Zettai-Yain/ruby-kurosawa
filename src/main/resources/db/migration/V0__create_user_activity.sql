CREATE TABLE user_activity
(
    id     BIGINT,
    source VARCHAR,
    score  BIGINT,

    PRIMARY KEY (id, source)
);