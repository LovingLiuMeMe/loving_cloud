FROM hub.c.163.com/library/java:8-alpine

MAINTAINER lovingliu

ADD target/*.jar auth.jar

EXPOSE 4000

ENTRYPOINT ["java", "-jar", "/auth.jar"]