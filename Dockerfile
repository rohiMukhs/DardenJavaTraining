FROM openjdk:11
ARG ARGENVRMNT="dev"
ENV SPRING_PROFILES_ACTIVE=$ARGENVRMNT
ENV SPRING_APPLICATION_JSON="DEFAULT"
EXPOSE 8081
ADD target/darden-dash-capacity-management-0.0.1-SNAPSHOT.jar darden-dash-capacity-management-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "darden-dash-capacity-management-0.0.1-SNAPSHOT.jar"]