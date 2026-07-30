CREATE TABLE kosts (
    id BIGSERIAL PRIMARY KEY,

    owner_id BIGINT NOT NULL,

    name VARCHAR(150) NOT NULL,

    description TEXT,

    location VARCHAR(255) NOT NULL,

    price DECIMAL(12,2) NOT NULL,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    deleted_at TIMESTAMP NULL,

    CONSTRAINT fk_kost_owner
        FOREIGN KEY (owner_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);