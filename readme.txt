

HDFS
=====================================================================================================
cd $HADOOP_HOME
bin/start-all.sh
hdfs dfs -mkdir /bigdata/healthcare/datalake/hl7/




Kafka 
=======================================================================================================
cd $Flume_HOME
bin/kafka-topics --create  --zookeeper localhost:2181  --partitions 1 --replication-factor 1 --topic HL7_Ingest




Flume
=====================================================================================================
cd $Flume_HOME
bin/flume-ng agent -n lambda -c conf -f conf/lambda_hdfs_flume_conf.properties


  
  
  
Spark
==========================================================================================================  
spark-submit --class com.bigdataconcept.bigdata.healthcare.processor.hL7.HL7ETLSparkProcessor  --master local[2] spark-hL7-etl-jar-with-dependencies.jar


 