CREATE TABLE IF NOT EXISTS topic (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    keyword VARCHAR(255) NOT NULL,
    current_summary TEXT,
    last_check_time DATETIME,
    status VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS topic_update (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    topic_id BIGINT NOT NULL,
    title VARCHAR(255),
    url VARCHAR(500),
    summary TEXT,
    is_significant BOOLEAN,
    created_at DATETIME,
    FOREIGN KEY (topic_id) REFERENCES topic(id)
);
