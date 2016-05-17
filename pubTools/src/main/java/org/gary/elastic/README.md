# Elasticsearch 基本安装

标签（空格分隔）： Elasticsearch


[TOC]


---
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
path.log : /hksdata/java/elasticsearch/logs
#设置绑定ip,多个ip用逗号隔开，默认为本地
network.host : 10.163.101.230
http.port : 9200
transport.tcp.port : 9300 
transport.tcp.compress : true
#设置传输最大数据为100M
http.max_content_length : 10mb
# 启用外部访问http
http.enabled : true
```

---
git提交代码无密码配置
1、进入到系统当前用户的目录，win7超级管理员目录为：C:\Users\Administrator
2、创建文件： _netrc
3、内容为：(#后面的字符不要填入文件内)
```
machine garyHuang.github.com #garyHuang为git显示名称
login 834865081@qq.com  #邮箱为账号
password pwd  #密码
```


[1]: http://static.zybuluo.com/Great-Chinese/130dknbwxaldacou2h5z6b50/014.png
[2]: http://static.zybuluo.com/Great-Chinese/8o09yieh7cv7n41h4dnnz4el/02.png