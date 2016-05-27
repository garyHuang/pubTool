# Elasticsearch 基本安装

标签（空格分隔）： Elasticsearch

[TOC]

---

##集群健康值说明:
```
红色,代表集群不可用
黄色,代表集群可用不可复制
绿色,代表集群正常
```
##1、下载
https://www.elastic.co/downloads/elasticsearch
##2、安装
###2.1解压 elasticsearch-*.tar.gz 文件
> tar -xvf elasticsearch-*.tar.gz

![014.png-12.8kB][1]

###2.2 安装marvel插件
> bin/plugin install license
bin/plugin install marvel-agent

Note
每个版本的安装命令都不同，具体参看最新官网文档，
https://www.elastic.co/downloads/marvel

###2.3安装 head
> bin/plugin install mobz/elasticsearch-head



##3、启动
> bin/elasticsearch
bin/elasticsearch -d #后台启动

##4、基本配置
***冒号后面必需预留一个空格***
```
cluster.name: hks
node.name: hks_data
node.master: true
node.data: true
#分片索引个数
index.number_of_shards : 5
#副本个数
index.number_of_replicas : 1
#索引文件路径,多个用逗号隔开
path.data : /hksdata/java/elasticsearch/data
#临时文件存储
path.work : /hksdata/java/elasticsearch/tmp
#日志文件路径
path.logs : /hksdata/java/elasticsearch/logs
#设置绑定ip,多个ip用逗号隔开，默认为本地
network.host : 10.163.101.230,120.27.43.49
http.port : 9200
transport.tcp.port : 9300 
transport.tcp.compress : true
#设置传输最大数据为100M
http.max_content_length : 10mb
# 启用对外http
http.enabled : true

#es集群配置，数据自动复制
discovery.zen.minimum_master_nodes: 2
discovery.zen.ping.timeout: 10s
discovery.zen.ping.multicast.enabled: true
discovery.zen.ping.unicast.hosts: ["10.163.101.230" , "10.45.19.14"]

#配置IK分词器
index.analysis.analyzer.default.type: ik
```
##5、IK分词器
a、下载ik分词器已发布的版本 https://github.com/medcl/elasticsearch-analysis-ik/releases
b、安装maven
c、编译ik分词器
  1、解压该zip到一个目录
  进入改目录，输入命令
```
 mvn clean install -Dmaven.test.skip=true 
```
  编译成功后会在target/releases目录下生成一个zip的文件，将这个文件复制到 es_home/plugins/analysis-ik 目录下（因为网络原因，一次可能编译不成功，可以尝试多次编译）
    ![02.png-23.3kB][3]
##6、设置某个字段不分词
```
curl -XPUT http://10.163.101.230:9200/hksdata/ -d'
{
    "mappings": {
        "part":{
            "properties": {
                "relation_code": {
                    "type": "string",
					"index": "not_analyzed"
                },
                "materialname": {
                    "type": "string"
                },
                "relation_brandid": {
                    "type": "string","index": "not_analyzed"
                },
               
                "relation_picno": {
                    "type": "string","index": "not_analyzed"
                },
                "relation_postion": {
                    "type": "string","index": "not_analyzed"
                },
               
                "relation_oecode": {
                    "type": "string","index": "not_analyzed"
                },
                "relation_cartypecode": {
                    "type": "string","index": "not_analyzed"
                },
               
                "brandcode": {
                    "type": "string",
					"index": "not_analyzed"
                },
                
                "originscode": {
                    "type": "string",
					"index": "not_analyzed"
                },
                "ecpunit": {
                    "type": "string",
					"index": "not_analyzed"
                },
                "relation_groupid": {
                    "type": "string","index": "not_analyzed"
                }
            }
        }
    }
}'
```

  [1]: http://static.zybuluo.com/Great-Chinese/130dknbwxaldacou2h5z6b50/014.png
  [2]: http://static.zybuluo.com/Great-Chinese/z04ap0koqiaovevn3bwzfr7t/00.png
  [3]: http://static.zybuluo.com/Great-Chinese/9x39tisypkm6owo2elxdzyom/02.png