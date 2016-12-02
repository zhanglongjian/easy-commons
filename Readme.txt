工具包介绍：

1、PhantomjsUtils.java
		用于网页需要执行js脚本才能完成全部html加载的场景
		
2、redis
	spring-data-redis的配置，redis订阅发布示例，spring-data-redis配置成@Cache注解可用。
	
3、memcached 
	配置成@Cache注解可用的缓存实现
	
4、guava本地缓存
 	配置成@Cache注解可用的本地缓存实现
 	
 5、rabbitmq
 	spring-data-rabbitmq基础配置，发送消息与接收消息示例
 	
 6、zookeeper
 	 zookeeper基础配置，及基础操作实现 ZookeeperService.java；
 	使用zookeeper做为统一配置的实现，即将分布式环境下 或多应用的环境下，数据库等的配置统一存放到zookeeper上；
 	zookeeper节点监听通知，类似订阅功能；
 	zookeeper分布式锁的实现，具体查看测试用例 ZooKeeperServiceTest.distriguteLock();
 	
 7、spring-data-jpa的基础配置
  	applicationContext-data.xml
  	
 8、FastJSONUtils.java 
 	本用 fastjson 对数据的序列化与反序列化的简化封装
 
 9、HttpUtils.java
 	使用java sdk原生实现的，http get post 以及文件下载等封装的工具类
 	
 10、RequestUtils.java
 	用于存放 和 获取，当前web登录用户等的工具类
 	
 11、ResponseOutputUtils.java
   用于web应用向客户端返回信息的封装的工具类
   
  12、MailUtils.java
  	邮件发送工具类,
  	要先通过spring将发送方的信息注入MailService.java
  	然可以可以直接使用MailUtils中的方法，具体看MailUtilsTest
  	
  	13、Spring-data-mongodb的配置和使用示例
  	applicationContext-mongodb.xml
  	applicationContext-mongodb-demo.xml
  	MongoTest.java
  	
  	14、ehcache 
	配置成@Cache注解可用的缓存实现
	
	15、dubbo
	dubbo配置使用实例