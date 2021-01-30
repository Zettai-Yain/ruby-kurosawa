CREATE TABLE user_activity_log
(
    seq    BIGSERIAL,
    time   TIMESTAMP WITH TIME ZONE,
    id     BIGINT,
    source VARCHAR,
    score  BIGINT,

    PRIMARY KEY (seq)
);
