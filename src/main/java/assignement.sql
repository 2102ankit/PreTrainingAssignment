CREATE TABLE employees(
	id SERIAL PRIMARY KEY,
	name VARCHAR(50),
	department VARCHAR(50),
	salary NUMERIC,
	joining_date DATE
);

INSERT INTO employees (name, department, salary, joining_date)
VALUES
('Ankit	', 'Dev',60000,'2025-01-02'),
('Vaibhav', 'IT', 75000, '2024-07-15'),
('Vijesh', 'Finance', 70000, '2023-10-01'),
('Aarya', 'HR', 65000, '2023-03-01'),
('Satyam', 'IT', 80000, '2021-07-15');


SELECT name, department FROM employees;

SELECT DISTINCT department FROM employees;

SELECT * FROM employees
WHERE (department='IT' OR department='HR') AND (salary>80000 OR joining_date > '2023-01-01');

SELECT * FROM employees ORDER BY salary DESC;

SELECT * FROM employees ORDER BY joining_date ASC;

--INSERT NEW RECORD
INSERT INTO employees(name, department, salary, joining_date)
VALUES ('Harshey','Marketing',55000,'2025-02-02');

UPDATE employees
SET salary = salary*1.10
WHERE department = 'HR';

SELECT * FROM employees;

DELETE FROM employees
WHERE salary<60000;

SELECT * FROM employees;

SELECT * FROM employees LIMIT 3;

SELECT * FROM employees WHERE name LIKE 'A%';

SELECT * FROM employees WHERE department IN ('IT','Finance');

SELECT * FROM employees WHERE salary BETWEEN 60000 AND 70000;

SELECT name AS employee_name , salary AS employee_salary FROM employees;

--------------------------------------------------------------------------------

CREATE TABLE departments(
	dept_id SERIAL PRIMARY KEY,
	dept_name VARCHAR(50),
	manager_name VARCHAR(50)
);


INSERT INTO departments(dept_name, manager_name)
VALUES 
('HR', 'Yusuf'),
('IT', 'Saad'),
('Finance', 'Neha'),
('Marketing', 'Jayraj');


SELECT e.name, e.department, d.manager_name
FROM employees e
INNER JOIN departments d ON e.department = d.dept_name;

SELECT e.name, e.department, d.manager_name
FROM employees e
LEFT JOIN departments d ON e.department = d.dept_name;

SELECT e.name, e.department, d.manager_name
FROM employees e
RIGHT JOIN departments d ON e.department = d.dept_name;

SELECT e.name, e.department, d.manager_name
FROM employees e
FULL JOIN departments d ON e.department = d.dept_name;

--------------------------------------------------------------------------------


SELECT department FROM employees
UNION 
SELECT dept_name FROM departments;

-- SELECT INTO (Create new table from existing data)

SELECT * INTO employee_backup FROM employees;

-- CREATE Database
-- CREATE DATABASE company_db;


CREATE TABLE projects(
	project_id SERIAL PRIMARY KEY,
	project_name VARCHAR(100) NOT NULL,
	start_date DATE NOT NULL,
	end_date DATE,
	budget NUMERIC CHECK(budget > 0),
	department_id INT REFERENCES departments(dept_id)
);

CREATE TABLE clients (
    client_id SERIAL PRIMARY KEY,
    client_name VARCHAR(100) UNIQUE
);


ALTER TABLE projects
ADD CONSTRAINT fk_department FOREIGN KEY (department_id) REFERENCES departments(dept_id);


ALTER TABLE projects
ADD COLUMN status VARCHAR(25) DEFAULT 'In Progress';

CREATE INDEX idx_department ON employees(department);

SELECT * FROM employees ORDER BY department;


---------------------------------------------------------------------------

DROP TABLE employee_backup;

ALTER TABLE employees ADD COLUMN email VARCHAR(100);

CREATE VIEW high_salary_employees AS 
SELECT name, salary FROM employees WHERE salary > 70000;

SELECT * FROM high_salary_employees;


SELECT * FROM employees WHERE email IS NULL;


----------------------------------------------------------------

SELECT department, AVG(salary) AS avg_salary
FROM employees
GROUP BY department;


SELECT department, AVG(salary) AS avg_salary
FROM employees
GROUP BY department
HAVING AVG(salary) > 75000;


SELECT department, AVG(salary) AS avg_salary
FROM (SELECT * FROM employees WHERE salary > 75000)
GROUP BY department;

SELECT UPPER(name) AS uppercase_name FROM employees;

SELECT COALESCE	(email, 'No Email Provided') AS email_info FROM employees;


-------------------------------------------------------------------------------------

PREPARE get_employee_by_department (VARCHAR) AS
SELECT * FROM employees WHERE department = $1;

EXECUTE get_employee_by_department('IT');
EXECUTE get_employee_by_department('HR');

DROP FUNCTION IF EXISTS increase_salary(VARCHAR, NUMERIC);

-- CREATE FUNCTION increase_salary(dept_name VARCHAR, increment NUMERIC)
-- RETURNS VOID AS $$
-- BEGIN
--     UPDATE employees SET salary = salary + increment WHERE department = dept_name;
-- END;
-- $$ LANGUAGE plpgsql;

-- SELECT increase_salary('HR', 5000);

