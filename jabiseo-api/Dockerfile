FROM openjdk:17

RUN mkdir -p /usr/local/newrelic

WORKDIR app

ARG JAV_FILE=./build/libs/*.jar

COPY ${JAV_FILE} app.jar

ADD ./newrelic/newrelic.jar /usr/local/newrelic/newrelic.jar
ADD ./newrelic/newrelic.yml /usr/local/newrelic/newrelic.yml

EXPOSE 8080

#ENTRYPOINT ["java","-javaagent:/usr/local/newrelic/newrelic.jar","-jar $JAVA_OPTS", "app.jar"]
ENTRYPOINT ["sh", "-c", "java -javaagent:/usr/local/newrelic/newrelic.jar $JAVA_OPTS -jar app.jar"]
