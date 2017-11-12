#!/usr/bin/env bash
# Setting up Java Jar Files IF YOU AREN'T USING IDE
export CLASSPATH=$CLASSPATH:~/Desktop/SchoolWork/swingtest/ojdbc8.jar
export CLASSPATH=$CLASSPATH:~/Desktop/SchoolWork/swingtest/json-simple-1.1.jar

# Docker Exec
docker exec oracledb_database_1 ./setPassword.sh mysecretpasswords   # Change password on system
docker exec -ti oracledb_database_1 sh  # ssh into container. change to your container name

# Used to check if Listener on DB is Running - and to start it if its not on
lsnrctl status     # Check status
lsnrctl services   # Check currently running services. should list your DB
lsnrctl start      # will start listening service if it is not on
lsnrctl services ORCLCDB   # Checks your particular db

# Enter SQL CLI
sqlplus / as sysdba   # This should be used the first time you start up the container
d   # Used after you created the admin user

# Queries
SELECT tablespace_name, table_name from user_tables;
# I sort of forgot what these guys do. it finds the SID of a given service, and existing services
SELECT value from v$parameter where name like '%service_name%';
SELECT sys_context('userenv','instance_name') from dual;
SELECT sys_context('USERENV', 'SID') FROM DUAL;
SELECT NAME FROM v$database;
SELECT user from dual;

# IMPORTANT: THIS GIVES ADMIN RIGHTS TO A USER WITH admin user and password
ALTER session set "_ORACLE_SCRIPT"=true;
CREATE USER admin IDENTIFIED BY admin;
GRANT CONNECT, RESOURCE, DBA TO admin;
GRANT CREATE SESSION TO admin;
GRANT ANY PRIVILEGE TO admin;
GRANT UNLIMITED TABLESPACE TO admin;
