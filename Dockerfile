FROM openjdk:11
ARG ARGENVRMNT="dev"
ENV ENVRMNT=$ARGENVRMNT
ENV PROFL="Dspring.profiles.active="
RUN powershell -c "Write-Output $env:ENVRMNT"
RUN powershell -c "Write-Output $env:PROFL"
ADD target/darden-dash-capacity-management-0.0.1-SNAPSHOT.jar .
ENTRYPOINT java -$env:PROFL$env:ENVRMNT -jar darden-dash-capacity-management-0.0.1-SNAPSHOT.jar