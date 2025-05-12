FROM confluentinc/cp-kafka-connect:7.5.0
# JDBC 커넥터 설치
RUN confluent-hub install --no-prompt confluentinc/kafka-connect-jdbc:10.7.4
# 2. MySQL JDBC 드라이버 다운로드
RUN wget https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.0.33/mysql-connector-j-8.0.33.jar \
    -P /usr/share/confluent-hub-components/confluentinc-kafka-connect-jdbc/lib/