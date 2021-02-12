DROP TABLE activity_rank;
CREATE TABLE activity_rank
(
    id               SERIAL,
    role_id          BIGINT,
    score            BIGINT,

    PRIMARY KEY (id)
);

DROP TABLE activity_config;
CREATE TABLE activity_config
(
    id               SERIAL,
    min_gain         INT,
    max_gain         INT,

    PRIMARY KEY (id)
);
