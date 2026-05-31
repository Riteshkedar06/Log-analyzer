CREATE TABLE user_sessions (
                               id BIGSERIAL PRIMARY KEY,

                               user_id BIGINT NOT NULL,

                               refresh_token_hash VARCHAR(255) NOT NULL,

                               device_name VARCHAR(255),

                               ip_address VARCHAR(100),

                               created_at TIMESTAMP NOT NULL,

                               expires_at TIMESTAMP NOT NULL,

                               revoked BOOLEAN NOT NULL DEFAULT FALSE,

                               CONSTRAINT fk_user_session_user
                                   FOREIGN KEY (user_id)
                                       REFERENCES users(id)
                                       ON DELETE CASCADE
);