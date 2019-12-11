FROM hub.c.163.com/library/java:8-alpine

MAINTAINER lovingliu

ADD target/*.jar eureka.jar

EXPOSE 1000

ENTRYPOINT ["java", "-jar", "/eureka.jar"]