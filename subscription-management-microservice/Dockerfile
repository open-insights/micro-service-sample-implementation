FROM openjdk:8-jdk-alpine
LABEL Author=open-insights.co.in

VOLUME /tmp

# Add the generated ear to the container
ARG artifactPath
ADD ${artifactPath} app.jar

# Start the application
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
