FROM amazoncorretto:21

RUN mkdir /app

COPY ./target/mortgage-rates-mvp-0.0.1-SNAPSHOT.jar /app/app.jar
COPY ./docker/entrypoint.sh    /app/entrypoint.sh

RUN chmod +x /app/entrypoint.sh

ENTRYPOINT /app/entrypoint.sh
