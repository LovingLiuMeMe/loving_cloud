FROM hub.c.163.com/library/java:8-alpine

MAINTAINER lovingliu

ADD target/*.jar order.jar

EXPOSE 2000

ENTRYPOINT ["java", "-jar", "/order.jar"]