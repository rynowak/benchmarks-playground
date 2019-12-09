FROM gradle:4.10-jdk8-alpine as compile
COPY . /home/source/java
WORKDIR /home/source/java
USER root
RUN chown -R gradle /home/source/java
USER gradle
RUN gradle clean build 

FROM openjdk:8-jre-alpine
WORKDIR /home/application/java
COPY --from=compile "/home/source/java/build/libs/weatherapp-0.0.1-SNAPSHOT.jar" .
EXPOSE 5000
ENTRYPOINT [ "java", "-jar", "/home/application/java/weatherapp-0.0.1-SNAPSHOT.jar"]