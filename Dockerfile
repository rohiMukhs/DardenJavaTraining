FROM openjdk:20
ARG ARGENVRMNT="dev"
ARG Version="0.0.1-SNAPSHOT"
ENV TAG=$Version
ENV SPRING_PROFILES_ACTIVE=$ARGENVRMNT
ENV SPRING_APPLICATION_JSON="DEFAULT"
EXPOSE 8081
ADD target/darden-dash-capacity-management-$TAG.jar darden-dash-capacity-management-$TAG.jar
RUN echo $TAG
ENTRYPOINT powershell -Command java -jar darden-dash-capacity-management-$env:TAG.jar