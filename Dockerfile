FROM frolvlad/alpine-oraclejdk8:slim

VOLUME /src

ADD target/cgk-camel-demo-0.0.1-SNAPSHOT.jar app.jar

RUN sh -c 'touch /app.jar'

ENTRYPOINT ["java","-jar","/app.jar"]