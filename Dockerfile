FROM openjdk:8-jdk-alpine
MAINTAINER seyijava@gmail.com
COPY hl7-messageRouter-0.0.1-SNAPSHOT.jar /opt/app/
ENTRYPOINT ["/usr/bin/java"]
CMD ["-jar", "/opt/app/hl7-messageRouter-0.0.1-SNAPSHOT.jar"]



