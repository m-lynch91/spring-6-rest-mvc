--MySQL version
--DROP DATABASE IF EXISTS restdb;
--DROP USER IF EXISTS `restadmin`@`%`;
--CREATE DATABASE IF NOT EXISTS restdb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;   --utf8mb4_unicode_ci flexible characterset
--CREATE USER IF NOT EXISTS `restadmin`@`%` IDENTIFIED WITH mysql_native_password BY 'password';
--GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, DROP, REFERENCES, INDEX, ALTER, EXECUTE, CREATE VIEW, SHOW VIEW,
--    CREATE ROUTINE, ALTER ROUTINE, EVENT, TRIGGER ON `restdb`.* TO `restadmin`@`%`;

-- PostgreSQL version
-- Drop the database and user if they exist
DROP DATABASE IF EXISTS restdb;
DROP ROLE IF EXISTS restadmin;

-- Create the user (called a role in Postgres)
CREATE ROLE restadmin WITH LOGIN PASSWORD 'password';

-- Create the database
CREATE DATABASE restdb
    WITH OWNER = restadmin
    ENCODING = 'UTF8'
    LC_COLLATE = 'en_US.utf8'
    LC_CTYPE = 'en_US.utf8'
    TEMPLATE template0;

-- Grant typical privileges (Postgres doesn’t have all the same granular privileges as MySQL)
GRANT CONNECT ON DATABASE restdb TO restadmin;

-- Grant privileges on future objects (tables, sequences, functions, etc.)
ALTER DEFAULT PRIVILEGES FOR ROLE restadmin IN SCHEMA public
    GRANT SELECT, INSERT, UPDATE, DELETE, REFERENCES, TRIGGER ON TABLES TO restadmin;

ALTER DEFAULT PRIVILEGES FOR ROLE restadmin IN SCHEMA public
    GRANT USAGE, SELECT, UPDATE ON SEQUENCES TO restadmin;

ALTER DEFAULT PRIVILEGES FOR ROLE restadmin IN SCHEMA public
    GRANT EXECUTE ON FUNCTIONS TO restadmin;
