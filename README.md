## 考研社区

##部署
###依赖
- Git
- JDK
- Maven
- MySQL
##步骤
- 强制删除rm -rf 
- yum update
- yum install git
- mkdir App
- cd App
- git clone https://github.com/small-zhihao/community.git
- cd community
- yum install maven
- java -version
- mvn -v
- mvn clean compile package//打包引入依赖,编译
- more src/main/resources/application.properties
- cp  src/main/resources/application.properties src/main/resources/application-production.properties
- vim src/main/resources/application-production.properties
- mvn package
- java -jar -Dspring.profiles.active=production target/community-0.0.1-SNAPSHOT.jar
- Ctrl+c 杀掉进程
- ps -aux | grep java //查看进程是否杀掉
- git pull//拉代码
-mvn package 编译代码
//更改脚本后拉代码
- git pull
- mvn flyway:repair
- mvn flyway:migrate
运行即可
- mvn clean compile flyway:migrate
- mvn clean compile flyway:repair

mvn分离后
mvn clean compile flyway:migrate -Pdev //本地是默认的
mvn clean compile flyway:migrate -Pproduction//线上

## 资料
[Spring 文档](https://spring.io/guides)
[Spring Web文档](https://spring.io/guides/gs/serving-web-content/)
[es](https://elasticsearch.cn/explore)
[Github deploy key](https://developer.github.com/v3/guides/managing-deploy-keys/)
[Boot Strap文档](https://v3.bootcss.com/getting-started/#download)
[Github 的授权文档](https://developer.github.com/apps/building-oauth-apps/creating-an-oauth-app/)
##工具
[Git](https://git-scm.com/download)
[VP](https://www.visual-paradigm.com)
[mapper更新]（mvn -Dmybatis.generator.overwrite=true mybatis-generator:generate）