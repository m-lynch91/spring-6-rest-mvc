DROP DATABASE IF EXISTS springboottutorial;
DROP ROLE IF EXISTS restadmin;

-- Create the user (called a role in Postgres)
CREATE ROLE restadmin WITH LOGIN PASSWORD 'password';

-- Create the database
CREATE DATABASE springboottutorial
    WITH OWNER = restadmin
    ENCODING = 'UTF8'
    LC_COLLATE = 'en_US.utf8'
    LC_CTYPE = 'en_US.utf8'
    TEMPLATE template0;

-- Grant typical privileges (Postgres doesn’t have all the same granular privileges as MySQL)
GRANT CONNECT ON DATABASE springboottutorial TO restadmin;


-- Grant privileges on future objects (tables, sequences, functions, etc.)
ALTER DEFAULT PRIVILEGES FOR ROLE restadmin IN SCHEMA public
    GRANT SELECT, INSERT, UPDATE, DELETE, REFERENCES, TRIGGER ON TABLES TO restadmin;

ALTER DEFAULT PRIVILEGES FOR ROLE restadmin IN SCHEMA public
    GRANT USAGE, SELECT, UPDATE ON SEQUENCES TO restadmin;

ALTER DEFAULT PRIVILEGES FOR ROLE restadmin IN SCHEMA public
    GRANT EXECUTE ON FUNCTIONS TO restadmin;