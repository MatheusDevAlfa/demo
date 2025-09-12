-- =====================================
-- Populando a tabela TEAM
-- =====================================
INSERT INTO team (name, is_active) VALUES ('Time A', TRUE);
INSERT INTO team (name, is_active) VALUES ('Time B', TRUE);
INSERT INTO team (name, is_active) VALUES ('Time C', TRUE);
INSERT INTO team (name, is_active) VALUES ('Time D', TRUE);
INSERT INTO team (name, is_active) VALUES ('Time E', TRUE);
INSERT INTO team (name, is_active) VALUES ('Time F', TRUE);
INSERT INTO team (name, is_active) VALUES ('Time G', TRUE);
INSERT INTO team (name, is_active) VALUES ('Time H', TRUE);
INSERT INTO team (name, is_active) VALUES ('Time I', TRUE);
INSERT INTO team (name, is_active) VALUES ('Time J', TRUE);

-- =====================================
-- Populando a tabela CYCLE
-- =====================================
INSERT INTO cycle (name, start_date, end_date, is_active) VALUES ('T1', PARSEDATETIME('01/01/2025', 'dd/MM/yyyy'), PARSEDATETIME('31/03/2025', 'dd/MM/yyyy'), TRUE);
INSERT INTO cycle (name, start_date, end_date, is_active) VALUES ('T2', PARSEDATETIME('01/04/2025', 'dd/MM/yyyy'), PARSEDATETIME('30/06/2025', 'dd/MM/yyyy'), TRUE);
INSERT INTO cycle (name, start_date, end_date, is_active) VALUES ('T3', PARSEDATETIME('01/07/2025', 'dd/MM/yyyy'), PARSEDATETIME('30/09/2025', 'dd/MM/yyyy'), TRUE);
INSERT INTO cycle (name, start_date, end_date, is_active) VALUES ('T4', PARSEDATETIME('01/10/2025', 'dd/MM/yyyy'), PARSEDATETIME('31/12/2025', 'dd/MM/yyyy'), TRUE);
INSERT INTO cycle (name, start_date, end_date, is_active) VALUES ('T5', PARSEDATETIME('01/01/2026', 'dd/MM/yyyy'), PARSEDATETIME('31/03/2026', 'dd/MM/yyyy'), TRUE);
INSERT INTO cycle (name, start_date, end_date, is_active) VALUES ('T6', PARSEDATETIME('01/04/2026', 'dd/MM/yyyy'), PARSEDATETIME('30/06/2026', 'dd/MM/yyyy'), TRUE);
INSERT INTO cycle (name, start_date, end_date, is_active) VALUES ('T7', PARSEDATETIME('01/07/2026', 'dd/MM/yyyy'), PARSEDATETIME('30/09/2026', 'dd/MM/yyyy'), TRUE);
INSERT INTO cycle (name, start_date, end_date, is_active) VALUES ('T8', PARSEDATETIME('01/10/2026', 'dd/MM/yyyy'), PARSEDATETIME('31/12/2026', 'dd/MM/yyyy'), TRUE);
INSERT INTO cycle (name, start_date, end_date, is_active) VALUES ('T9', PARSEDATETIME('01/01/2027', 'dd/MM/yyyy'), PARSEDATETIME('31/03/2027', 'dd/MM/yyyy'), FALSE);

-- =====================================
-- Populando a tabela OBJECTIVE
-- =====================================
INSERT INTO objective (title, description, is_active, team_id) VALUES ('Objetivo 1', 'Descrição do objetivo 1', TRUE, 1);
INSERT INTO objective (title, description, is_active, team_id) VALUES ('Objetivo 2', 'Descrição do objetivo 2', TRUE, 2);
INSERT INTO objective (title, description, is_active, team_id) VALUES ('Objetivo 3', 'Descrição do objetivo 3', TRUE, 3);
INSERT INTO objective (title, description, is_active, team_id) VALUES ('Objetivo 4', 'Descrição do objetivo 4', TRUE, 4);
INSERT INTO objective (title, description, is_active, team_id) VALUES ('Objetivo 5', 'Descrição do objetivo 5', TRUE, 5);
INSERT INTO objective (title, description, is_active, team_id) VALUES ('Objetivo 6', 'Descrição do objetivo 6', TRUE, 6);
INSERT INTO objective (title, description, is_active, team_id) VALUES ('Objetivo 7', 'Descrição do objetivo 7', TRUE, 7);
INSERT INTO objective (title, description, is_active, team_id) VALUES ('Objetivo 8', 'Descrição do objetivo 8', TRUE, 8);
INSERT INTO objective (title, description, is_active, team_id) VALUES ('Objetivo 9', 'Descrição do objetivo 9', TRUE, 9);

-- =====================================
-- Associando OBJECTIVE e CYCLE (M:N)
-- =====================================
INSERT INTO objective_cycle (objective_id, cycle_id) VALUES (1, 1);
INSERT INTO objective_cycle (objective_id, cycle_id) VALUES (2, 2);
INSERT INTO objective_cycle (objective_id, cycle_id) VALUES (3, 3);
INSERT INTO objective_cycle (objective_id, cycle_id) VALUES (4, 4);
INSERT INTO objective_cycle (objective_id, cycle_id) VALUES (5, 5);
INSERT INTO objective_cycle (objective_id, cycle_id) VALUES (6, 6);
INSERT INTO objective_cycle (objective_id, cycle_id) VALUES (7, 7);
INSERT INTO objective_cycle (objective_id, cycle_id) VALUES (8, 8);
INSERT INTO objective_cycle (objective_id, cycle_id) VALUES (9, 9);