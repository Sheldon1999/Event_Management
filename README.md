# Event Management System

## Overview
This is a very simple DBMS project. It is actually an [**standalone(portable) application**](https://www.quora.com/What-is-a-standalone-application). Theme of application is **Event Management**(college events). This project can be very helpful to those who just started with [these](#technologies) technologies.

## Technologies

- Frontend : Java AWT, Swing
- Backend : Java11
- Database : MySQL
- Database Connector : JDBC

## Dependencies

- [Java SE 11](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html).
- [MySQL database](https://www.mysql.com/downloads/)
- [JDBC](https://dev.mysql.com/downloads/connector/j/5.1.html)

## Local setup

1. install all the dependecies above(_install JDK11 or later_). 
2. make a mysql user named ```superadmin``` and set password ```Super@1999```.
3. To make databases and tables see the [source code](https://github.com/Sheldon1999/Event_Management/blob/master/Source_Code.java) from line 259. Here ```Department``` are databses and ```Event``` are tables.
4. Every table will contain follwing entities:
  - Name (VARCHAR)
  - Department (VARCHAR)
  - Semester (VARCHAR)
  - Contact_No (INT)
  - Gmail (VARCHAR)
5. download [EMS.jar](https://github.com/Sheldon1999/Event_Management/blob/master/EMS.jar)
6. open command prompt or terminal go to the directory where jar file is downloaded.
7. After setting up everything,run ```java -jar EMS.jar```.

## Screenshots

### Home page

[!Home_page](https://github.com/Sheldon1999/Event_Management/blob/master/screenshots/image3.jpg)
