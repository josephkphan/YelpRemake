#!/usr/bin/env bash
export CLASSPATH=$CLASSPATH:~/Desktop/SchoolWork/swingtest/ojdbc8.jar
export CLASSPATH=$CLASSPATH:~/Desktop/SchoolWork/swingtest/json-simple-1.1.jar

javac -cp "../ojdbc8.jar" OracleJDBCExample.java


lsnrctl status
lsnrctl services
lsnrctl start

lsnrctl services ORCLCDB
docker exec -ti oracledb_database_1 sh

sqlplus / as sysdba   -- use this to enter SQL CLI
sqlplus system/mysecretpassword



select value from v$parameter where name like '%service_name%';

select sys_context('userenv','instance_name') from dual;

SELECT sys_context('USERENV', 'SID') FROM DUAL;

SELECT NAME FROM v$database;

select user from dual;


alter session set "_ORACLE_SCRIPT"=true;
CREATE USER admin IDENTIFIED BY admin;
GRANT CONNECT, RESOURCE, DBA TO admin;
GRANT CREATE SESSION TO admin;
GRANT ANY PRIVILEGE TO admin;
GRANT UNLIMITED TABLESPACE TO admin;
