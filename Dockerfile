FROM quay.io/wildfly/wildfly-centos7
ADD ./target/openinghours.war /opt/wildfly/standalone/deployments/
