# Deploy
## Embedded server
To run at tomcat7-maven-plugin embedded server:
<p>`mvn clean package tomcat7:run`
<p>And visit http://localhost:9090/tasklist

## On remote tomcat
* To deploy on the running instance of Tomcat:
  ```
  mvn clean package tomcat7:deploy -Dmaven.tomcat.url=http://<tomcat_ip_and_port>/manager/text -Dtomcat.username=<username> -Dtomcat.password=<password>
  ```
  And visit http://<tomcat_ip_and_port>/tasklist

* To undeploy:
  ```
  mvn tomcat7:undeploy -Dmaven.tomcat.url=http://<tomcat_ip_and_port>/manager/text -Dtomcat.username=<username> -Dtomcat.password=<password>
  ```