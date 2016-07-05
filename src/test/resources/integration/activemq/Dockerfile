FROM java:8

ENV VERSION 5.13.3

RUN curl http://www.mirrorservice.org/sites/ftp.apache.org/activemq/$VERSION/apache-activemq-$VERSION-bin.tar.gz | tar -xz

EXPOSE 61612 61613 61616 8161

RUN mv apache-activemq-$VERSION/conf/activemq.xml apache-activemq-$VERSION/conf/activemq.xml.orig
RUN awk '/.*stomp.*/{print "            <transportConnector name=\"stomp+ssl\" uri=\"stomp+ssl://0.0.0.0:61612\"/>"}1' apache-activemq-$VERSION/conf/activemq.xml.orig >> apache-activemq-$VERSION/conf/activemq.xml

CMD java -Xms1G -Xmx1G -Djava.util.logging.config.file=logging.properties -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote -Djava.io.tmpdir=apache-activemq-$VERSION/tmp -Dactivemq.classpath=apache-activemq-$VERSION/conf -Dactivemq.home=apache-activemq-$VERSION -Dactivemq.base=apache-activemq-$VERSION -Dactivemq.conf=apache-activemq-$VERSION/conf -Dactivemq.data=apache-activemq-$VERSION/data -jar apache-activemq-$VERSION/bin/activemq.jar start
