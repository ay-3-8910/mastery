FROM openjdk:11
WORKDIR usr/src/app/
COPY target/RestApplication.jar app.jar
CMD ["java","-jar","app.jar"]