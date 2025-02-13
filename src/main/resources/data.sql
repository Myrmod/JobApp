INSERT INTO companies (name, description) VALUES
('Company 1', 'Description 1'),
('Company 2', 'Description 2'),
('Company 3', 'Description 3'),
('Company 4', 'Description 4'),
('Company 5', 'Description 5'),
('Company 6', 'Description 6'),
('Company 7', 'Description 7'),
('Company 8', 'Description 8'),
('Company 9', 'Description 9'),
('Company 10', 'Description 10');

INSERT INTO jobs (title, description, min_salary, max_salary, location, company_id) VALUES
('Job 1', 'Description 1', '40000€', '50000€', 'Germany', 1),
('Job 2', 'Description 2', '40000€', '50000€', 'Germany', 1),
('Job 3', 'Description 3', '40000€', '50000€', 'Germany', 1),
('Job 4', 'Description 4', '40000€', '50000€', 'Germany', 2),
('Job 5', 'Description 5', '40000€', '50000€', 'Germany', 2),
('Job 6', 'Description 6', '40000€', '50000€', 'Germany', 3),
('Job 7', 'Description 7', '40000€', '50000€', 'Germany', 4),
('Job 8', 'Description 8', '40000€', '50000€', 'Germany', 5),
('Job 9', 'Description 9', '40000€', '50000€', 'Germany', 6),
('Job 10', 'Description 10', '40000€', '50000€', 'Germany', 6);

INSERT INTO reviews (id, title, description, rating, company_id) VALUES
(1, 'Great Place to Work', 'I had an amazing experience working here.', 5, 1),
(2, 'Good Work Environment', 'Nice colleagues and good management.', 4, 2),
(3, 'Average Experience', 'The workload was a bit too much.', 3, 1),
(4, 'Needs Improvement', 'Management could be better.', 2, 3),
(5, 'Terrible Experience', 'Toxic environment, wouldn’t recommend.', 1, 4);
