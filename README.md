# Sample Todo Project for Vert.X 4.0.2 and Spring Boot 2.3.0 Reactive

Prerequisite
Docker, Java 11, Docker-Compose, Maven

To run both project, you first need to build vert.x project and spring rx project.
Vert.X
1. navigate to vert.x folder
2. mvn clean
3. mvn package

Spring Rx
1. navigate to spring.rx folder
2. mvn clean
3. mvn package spring-boot:repackage

4. go back to root folder where the docker-compose.yml is.
5. docker-compose up

To build the vert.x project individually
1. navigate to vert.x folder
   modify the TodoServiceImpl.java
    replace the mysql connection string "mysql://<username>:<password>@<host>:<port>/tododb";
2. mvn clean
3. mvn package
4. run it with mvn exec:java

The vert.x project will run under 127.0.0.1:8081