
# Yelp Database Project


## Starting Up the Database
Following Oracle instructions, I have created a docker image of a oracle database.
Pull and run this oracle image by doing the following commands:\
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;   `cd oracledb` \
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;   `docker-compose up -d`\

It will take roughly 10-20 minutes to download the image and start it up. 
If you run into any problems. starting the image check to see if the directories in the 'Volume' exist and it has 
After it is up and running, go into the container and create an admin user for the database. 
To do this, first we will log in as a sysadmin \
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;   `docker exec -ti oracledb_database_1 sh` \
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;   `sqlplus / as sysdba` 

The SQL Prompt should now appear : SQL> \
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; `ALTER session set "_ORACLE_SCRIPT"=true;` \
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; `CREATE USER admin IDENTIFIED BY admin;`\
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; `GRANT CONNECT, RESOURCE, DBA TO admin;`\
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; `GRANT CREATE SESSION TO admin;`\
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; `GRANT ANY PRIVILEGE TO admin;`

Your Database should be ready to go! 

## Accessing your Database CLI 
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;   `docker exec -ti oracledb_database_1 sh` \
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;   `sqlplus admin/admin `


## Populating the Database 
First go into the container and create some tables. (feel free to copy the createdb.sql in the src/sql directory)\
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; `java Populate yelp_business.json yelp_user.json yelp_review.json yelp_checkin.json`

## Starting the GUI
The GUI is interface to query the database. To start the GUI run the following command \
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; `java Main`


#### Current Dependencies
- json-simple-1.1.jar
- ojdbc8.jar
- YelpDataset (should be in in project root directory).


## Screenshot
![alt text](https://photos.app.goo.gl/2MJgzYe2Vq9ty9Mq2)

