FROM quay-quay-openshift-operators.apps.raoshclsd01.darden.com/pointofsale-dev/redhatopenjdk
ARG ARGENVRMNT="dev"
ARG Version="0.0.1-SNAPSHOT" 
ENV TAG=$Version
ENV SPRING_PROFILES_ACTIVE=$ARGENVRMNT
ENV SPRING_APPLICATION_JSON="DEFAULT"
EXPOSE 7083
ADD target/darden-dash-capacity-management-$TAG.jar darden-dash-capacity-management-$TAG.jar
RUN echo $TAG
ENTRYPOINT java -jar darden-dash-capacity-management-$TAG.jar