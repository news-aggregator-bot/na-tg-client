FROM adoptopenjdk/openjdk15

MAINTAINER Vladyslav Yemelianov <emelyanov.vladyslav@gmail.com>

ADD ./target/na-tg-client.jar /app/
CMD ["java", "-Xmx512m", "-jar", "/app/na-tg-client.jar"]

EXPOSE 8080