# POS_FX 
JavaFx POS System

#SetUp
- 1- install java 11
- 2- install mysql database with
- 3- run db script 'POSFullScript.sql'
- 4- copy setting.xml file and edit configuration based on your data
-
#After build project
- move fat jar 'Market-1.0-SNAPSHOT-shaded.jar' and 'setting.xml'
- then double click on jar to run
- Login Using User:01277437142 and password:123

#Revamp 
- ~~Encript password~~
- Create Service , DAO packages and move all sql in new class and all connection and database in DAOs
- Table Product "items" add new field 'image' , 'Created date' , 'lastupdate'
- ~~Adding License to The System~~
# import
- ~~log4j~~
- Screen Service to saving all screen paths 
- Fillo lib issue Excel Report
- Handle Report and Receipt
- Handle Security in another way and make defferent roles (Create table for edit update delete for POS modules)





#online database
- https://console.aiven.io/login
Database name : defaultdb
Host: mysql-2087c03b-ahmedaly-7d1a.j.aivencloud.com
Port: 20845
User: avnadmin
Password : AVNS_LysEsx1JKuRH7wVKPpx
SSL mode : REQUIRED
