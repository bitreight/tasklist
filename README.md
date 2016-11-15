# Description
Tasklist is a web application for managing your tasks and to-dos, organizing collaborative projects.

# Usecases
Usecases of the first development iteration are:
* create tasks
* set deadline and priority of the task
* organize your tasks in projects (non-shared)
* sort your tasks by criteria (name, deadline, priority, etc.)

Full information about development iterations see in docs.

# Deploy
Tasklist uses a MySQL as a production DBMS and a Flyway migration tool. So at first do the following:
* Create an empty database.
* Set database url, username and password as jdbc properties in tasklist-backend/src/main/resources/db/db.properties.

## Embedded server
To run at tomcat7-maven-plugin embedded server:
<p>`mvn clean package tomcat7:run`
<p>And visit http://localhost:9090/tasklist

## On remote tomcat
* To deploy on the running instance of Tomcat:
  ```
  mvn clean package tomcat7:deploy -Dmaven.tomcat.url=http://<tomcat_ip_and_port>/manager/text -Dtomcat.username=<username> -Dtomcat.password=<password>
  ```
  And visit http://tomcat_ip_and_port/tasklist

* To undeploy:
  ```
  mvn tomcat7:undeploy -Dmaven.tomcat.url=http://<tomcat_ip_and_port>/manager/text -Dtomcat.username=<username> -Dtomcat.password=<password>
  ```