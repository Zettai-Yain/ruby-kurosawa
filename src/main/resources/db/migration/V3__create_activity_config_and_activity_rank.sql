CREATE TABLE activity_config
(
    id               BIGINT,
    min_gain         INT,
    max_gain         INT,
    activity_rank_id SERIAL UNIQUE,

    PRIMARY KEY (id)
);

CREATE TABLE activity_rank
(
    id               SERIAL,
    activity_rank_id INT,
    role_id          BIGINT,
    score            BIGINT,

    PRIMARY KEY (id),
    FOREIGN KEY (activity_rank_id) REFERENCES activity_config (activity_rank_id)
)