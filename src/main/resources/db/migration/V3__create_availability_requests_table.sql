CREATE TABLE availability_requests (
    id BIGSERIAL PRIMARY KEY,

    user_id BIGINT NOT NULL,

    kost_id BIGINT NOT NULL,

    credit_used INTEGER NOT NULL,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_availability_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_availability_kost
        FOREIGN KEY (kost_id)
        REFERENCES kosts(id)
        ON DELETE CASCADE
);