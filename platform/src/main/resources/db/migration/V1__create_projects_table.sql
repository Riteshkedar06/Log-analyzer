CREATE TABLE projects (
                          id UUID PRIMARY KEY,
                          name VARCHAR(100) NOT NULL,
                          description VARCHAR(1000),
                          created_by UUID NOT NULL,
                          created_at TIMESTAMP NOT NULL,
                          updated_at TIMESTAMP,
                          version BIGINT NOT NULL DEFAULT 0
);

CREATE INDEX idx_projects_created_by
    ON projects(created_by);