CREATE TABLE Department
(
    department_id BIGSERIAL PRIMARY KEY,
    name          VARCHAR(255) NOT NULL,
    location      VARCHAR(255) NOT NULL,
    CONSTRAINT name_len CHECK (LENGTH(BTRIM(name)) > 1),
    CONSTRAINT location__len CHECK (LENGTH(BTRIM(location)) > 1),
    CONSTRAINT name_location_uniq UNIQUE (name, location)
);