FROM hub.c.163.com/library/java:8-alpine

MAINTAINER lovingliu

ADD target/*.jar activity.jar

EXPOSE 8000

ENTRYPOINT ["java", "-jar", "/activity.jar"]