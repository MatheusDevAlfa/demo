-- =====================================
-- Tabela TEAM
-- =====================================
CREATE TABLE team (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE
);

-- =====================================
-- Tabela CYCLE
-- =====================================
CREATE TABLE cycle (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,           -- T1, T2, T3...
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE
);

-- =====================================
-- Tabela OBJECTIVE
-- =====================================
CREATE TABLE objective (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(1000),
--    is_completed BOOLEAN NOT NULL DEFAULT FALSE,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    team_id BIGINT NOT NULL,
    CONSTRAINT fk_objective_team FOREIGN KEY (team_id) REFERENCES team(id)
);

-- =====================================
-- Tabela OBJECTIVE_CYCLE (M:N)
-- =====================================
CREATE TABLE objective_cycle (
    objective_id BIGINT NOT NULL,
    cycle_id BIGINT NOT NULL,
    PRIMARY KEY (objective_id, cycle_id),
    CONSTRAINT fk_obj_cycle_objective FOREIGN KEY (objective_id) REFERENCES objective(id),
    CONSTRAINT fk_obj_cycle_cycle FOREIGN KEY (cycle_id) REFERENCES cycle(id)
);
