CREATE TABLE projects (
                          project_id UUID PRIMARY KEY,

                          project_name VARCHAR(100) NOT NULL,

                          description VARCHAR(1000),

                          created_by UUID NOT NULL,

                          created_at TIMESTAMP NOT NULL,

                          updated_at TIMESTAMP,

                          version BIGINT NOT NULL DEFAULT 0
);

CREATE INDEX idx_project_created_by
    ON projects(created_by);

CREATE UNIQUE INDEX uk_project_name_user
    ON projects(project_name, created_by);


CREATE TABLE uploaded_logs (
                               id UUID PRIMARY KEY,

                               project_id UUID NOT NULL,

                               file_name VARCHAR(500) NOT NULL,

                               status VARCHAR(50) NOT NULL,

                               uploaded_at TIMESTAMP NOT NULL,

                               CONSTRAINT fk_uploaded_logs_project
                                   FOREIGN KEY (project_id)
                                       REFERENCES projects(project_id)
                                       ON DELETE CASCADE
);


CREATE TABLE sources (
                         source_id UUID PRIMARY KEY,

                         project_id UUID NOT NULL,

                         service_name VARCHAR(255) NOT NULL,

                         environment VARCHAR(100),

                         host VARCHAR(255),

                         created_at TIMESTAMP NOT NULL,

                         CONSTRAINT fk_sources_project
                             FOREIGN KEY (project_id)
                                 REFERENCES projects(project_id)
                                 ON DELETE CASCADE
);


CREATE TABLE agents (
                        agent_id UUID PRIMARY KEY,

                        source_id UUID NOT NULL,

                        api_key VARCHAR(255) NOT NULL UNIQUE,

                        hostname VARCHAR(255) NOT NULL,

                        status VARCHAR(50) NOT NULL,

                        last_heartbeat TIMESTAMP,

                        registered_at TIMESTAMP NOT NULL,

                        agent_version VARCHAR(50) NOT NULL,

                        CONSTRAINT fk_agents_source
                            FOREIGN KEY (source_id)
                                REFERENCES sources(source_id)
                                ON DELETE CASCADE
);