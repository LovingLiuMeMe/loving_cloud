FROM hub.c.163.com/library/java:8-alpine

MAINTAINER lovingliu

ADD target/*.jar config.jar

EXPOSE 9000

ENTRYPOINT ["java", "-jar", "/config.jar"]