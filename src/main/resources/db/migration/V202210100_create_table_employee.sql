CREATE TABLE Employee
(
    employee_id   BIGSERIAL PRIMARY KEY,
    first_name    VARCHAR(255) NOT NULL,
    last_name     VARCHAR(255) NOT NULL,
    department_id BIGINT NOT NULL,
    email         VARCHAR(255) NOT NULL UNIQUE,
    phone_number  VARCHAR(255) NOT NULL UNIQUE,
    salary        DOUBLE PRECISION NOT NULL,
    CONSTRAINT employment_salary_check CHECK (salary >= 1.0),
    CONSTRAINT first_name_len CHECK (LENGTH(BTRIM(first_name)) > 0),
    CONSTRAINT last_name_len CHECK (LENGTH(BTRIM(last_name)) > 0),
    CONSTRAINT email_len CHECK (LENGTH(BTRIM(email)) > 0),
    CONSTRAINT department_fk FOREIGN KEY (department_id) REFERENCES Department (department_id)
);