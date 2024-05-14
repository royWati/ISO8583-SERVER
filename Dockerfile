
FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY  *.jar /app/app.jar
COPY deploy/server.xml ./deploy/
COPY packager/genericpackager.xml ./packager/
ENTRYPOINT ["java","-jar","app.jar"]
