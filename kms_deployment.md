# 密钥管理服务部署文档

## 1. 前提条件

| 序号 | 软件                         |
| ---- | ---------------------------- |
| 1    | MySQL5.6或以上版本           |
| 2    | Java8或以上版本，使用OpenJDK |

## 2. 拉取代码

执行命令：

```shell
git clone https://github.com/chaychen2005/FISCO-Key-Manager.git
```

## 3. 编译代码

进入项目目录，使用gradlew编译：

```shell
cd FISCO-Key-Manager && chmod +x ./gradlew && ./gradlew build
```

构建完成后，在根目录FISCO-Key-Manager下生成目录dist。

## 4. 数据库初始化

### 4.1 新建数据库

```bash
#登录MySQL:
mysql -u ${your_db_account} -p${your_db_password}  
#新建数据库：
CREATE DATABASE IF NOT EXISTS {your_db_name} DEFAULT CHARSET utf8 COLLATE utf8_general_ci;
```

例如：

```bash
mysql -u root -p123456
CREATE DATABASE IF NOT EXISTS my_key_manager DEFAULT CHARSET utf8 COLLATE utf8_general_ci;
```

### 4.2 修改脚本配置

进入数据库脚本目录：

```shell
cd dist/script
```

修改数据库连接信息：

```shell
修改数据库名称：sed -i "s/fisco_key_manager/${your_db_name}/g" kms.sh
修改数据库用户名：sed -i "s/defaultAccount/${your_db_account}/g" kms.sh
修改数据库密码：sed -i "s/defaultPassword/${your_db_password}/g" kms.sh
```

例如：

```shell
sed -i "s/fisco_key_manager/my_key_manager/g" kms.sh
sed -i "s/defaultAccount/root/g" kms.sh
sed -i "s/defaultPassword/123456/g" kms.sh
```

### 4.3 运行数据库脚本

执行命令：

```shell
bash kms.sh ${dbIP} ${dbPort}
```

例如：

```shell
bash kms.sh 127.0.0.1 3306
```

## 5. 服务配置及启停

### 5.1 服务配置修改

（1）在dist目录，根据配置模板生成一份实际配置conf。

```shell
cd .. && cp -r conf_template conf
```

（2）修改服务配置，注意需与`4.2 修改脚本配置`的数据库信息保持一致：

```shell
修改服务端口：sed -i "s/servicePort/${your_server_port}/g" conf/application.yml
修改数据库IP：sed -i "s/dbIP/${your_db_ip}/g" conf/application.yml
修改数据库端口：sed -i "s/dbPort/${your_db_port}/g" conf/application.yml
修改数据库名称：sed -i "s/fisco_key_manager/${your_db_name}/g" conf/application.yml
修改数据库用户：sed -i "s/defaultAccount/${your_db_account}/g" conf/application.yml
修改数据库密码：sed -i "s/defaultPassword/${your_db_password}/g" conf/application.yml
```

例如：

```shell
sed -i "s/servicePort/9501/g" conf/application.yml
sed -i "s/dbIP/127.0.0.1/g" conf/application.yml
sed -i "s/dbPort/3306/g" conf/application.yml
sed -i "s/fisco_key_manager/my_key_manager/g" conf/application.yml
sed -i "s/defaultAccount/root/g" conf/application.yml
sed -i "s/defaultPassword/123456/g" conf/application.yml
```

### 5.2 服务启停及状态检查

在dist目录下执行：

```shell
启动：
[app@VM_0_1_centos dist]$ bash start.sh
try to start server org.fisco.bcos.key.mgr.Application
    server org.fisco.bcos.key.mgr.Application start successfully.
停止：
[app@VM_0_1_centos dist]$ bash stop.sh
try to stop server org.fisco.bcos.key.mgr.Application
    server org.fisco.bcos.key.mgr.Application stop successfully.
检查：
[app@VM_0_1_centos dist]$ ps aux | grep "org.fisco.bcos.key.mgr.Application" | grep java
```

### 5.3 查看日志

在dist目录查看：

```shell
全量日志：tail -f log/FISCO-Key-Manager.log
错误日志：tail -f log/FISCO-Key-Manager-error.log
```