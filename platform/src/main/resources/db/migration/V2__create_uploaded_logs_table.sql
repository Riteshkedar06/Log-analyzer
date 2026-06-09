CREATE TABLE uploaded_logs (
                               id UUID PRIMARY KEY,
                               project_id UUID NOT NULL,
                               file_name VARCHAR(500) NOT NULL,
                               status VARCHAR(50) NOT NULL,
                               uploaded_at TIMESTAMP NOT NULL
);