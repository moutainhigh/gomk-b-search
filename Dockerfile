ARG harbor
#阿里云172.17.0.16:5000
#电厂 10.2.128.215:5000
FROM ${harbor}/ops/centos7_jre8:v1

###以下为修改项每个业务有差异###
###向镜像注入war包
ADD target/gomk-b-core-1.0.jar /export/server/


###  JAVA参数调优，注意内存大小参考deploymengt.yml的requets和limit中的内存大小，需小于limit内存大小，不然会OOM
ENV JAVA_OPTS='-Xms512m -Xmx2048m'

##tomcatcat启动命令
CMD java -jar /export/server/gomk-b-core-1.0.jar
