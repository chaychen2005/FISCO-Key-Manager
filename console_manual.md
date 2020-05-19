# 密钥管理服务控制台使用手册

[控制台](https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/manual/console.html)在原有连接区块链节点的基础上增加访问密钥管理服务的功能，实现对私钥的托管及找回操作。控制台原有连接区块链的命令可继续使用，本文档单独给出用于密钥管理服务的命令的使用说明。

用户在启动控制台时，可选地连接区块链或者访问密钥管理服务。控制台目前尚未提供同时连接区块链与访问密钥管理服务的功能。

## 前置条件

- 搭建密钥管理服务
- 搭建Nginx（可选）

## 控制台直连密钥管理服务

```mermaid
graph LR;
    控制台-->密钥管理服务;
    密钥管理服务-->MySQL数据库;
```

### 1. 获取源码 

```text
git clone https://github.com/chaychen2005/console.git && cd console && git checkout kms
```

### 2. 使用gradlew编译

```text
./gradlew build
```

构建完成后，在根目录console下生成目录dist。

### 3. 修改配置

在dist/conf目录，根据配置模板生成一份实际配置文件applicationContext.xml。

```text
cd dist/conf && cp applicationContext-sample.xml applicationContext.xml
```

修改密钥管理服务的IP和Port

```text
sed -i "s/serviceIP:servicePort/${your_server_ip}:${your_server_port}/g" applicationContext.xml
```

例如：

```text
sed -i "s/serviceIP:servicePort/127.0.0.1:9501/g" applicationContext.xml
```

### 4. 启动控制台

在运行密钥管理服务的情况下，用户在dist目录以内置管理员身份（admin）启动控制台，并查询管理员可使用的命令：

```text
[app@VM_0_1_centos dist]$ ./start.sh -kms admin Abcd1234 
=============================================================================================
Welcome to Key Manager Service console(1.0.9)!
Type 'help' or 'h' for help. Type 'quit' or 'q' to quit console.
=============================================================================================
[admin:admin]> help 
The following commands can be called by the admin
---------------------------------------------------------------------------------------------
addAdminAccount                          Add an admin account.
addVisitorAccount                        Add a visitor account.
deleteAccount                            Delete admin or visitor account.
listAccount                              Display a list of accounts created by yourself.
updatePassword                           Update the password of your own account.
restorePrivateKey                        Restore account's private key by alias.
quit(q)                                  Quit console.

---------------------------------------------------------------------------------------------
```

管理员新建访客后，用户也可以访客身份启动控制台，并查询访客可使用的命令：

```text
[app@VM_0_1_centos dist]$ ./start.sh -kms user1 123456
=============================================================================================
Welcome to Key Manager Service console(1.0.9)!
Type 'help' or 'h' for help. Type 'quit' or 'q' to quit console.
=============================================================================================
[user1:visitor]> help 
The following commands can be called by the visitor
---------------------------------------------------------------------------------------------
updatePassword                           Update the password of your own account.
uploadPrivateKey                         Upload the private key to key manager service.
listPrivateKey                           Display a list of private keys owned by yourself.
exportPrivateKey                         Export your own private key by alias.
deletePrivateKey                         Delete your own private key by alias.
quit(q)                                  Quit console.

---------------------------------------------------------------------------------------------
```

## 控制台命令

### **addAdminAccount**

管理员运行addAdminAccount，新增一个管理员子账号。参数：

- 账号名称：新增的管理员的账号名称，5-20位字母数字下划线组成，且以字符开头
- 登录密码：新增的管理员的登录密码，6-20位字母数字下划线组成
- 管理员公钥：新增的管理员（非当前管理员）用于加密其子账号托管的私钥，长度128，不区分大小写

```text
[admin:admin]> addAdminAccount testAdmin 123456 8d83963610117ed59cf2011d5b7434dca7bb570d4a16e63c66f0803f4c4b1c03a1125500e5ca699dfbb6b48d450a82a5020fcb3b43165b508c10cb1479c6ee49
Add an admin account "testAdmin" successfully.
```

### **addVisitorAccount**

管理员运行addVisitorAccount，新增一个访客子账号。参数：

- 账号名称：新增的访客的账号名称，5-20位字母数字下划线组成，且以字符开头
- 登录密码：新增的访客的登录密码，6-20位字母数字下划线组成

```text
[admin:admin]> addVisitorAccount user1 123456
Add a visitor account "user1" successfully.
```

### **deleteAccount**

管理员运行deleteAccount，删除其创建的子账号，不可删除自身账号。参数：

- 账号名称：删除的子账号名称

```text
[admin:admin]> deleteAccount testUser
Delete an account "testUser" successfully.
```

### **listAccount**

管理员运行listAccount，显示所创建的子账号列表（含自身账号）。

```text
[admin:admin]> listAccount 
The count of account created by "admin" is 3.
---------------------------------------------------------------------------------------------
|             name             |             role             |          createTime          |
|            user1             |           visitor            |     2020-05-20 16:28:10      |
|          testAdmin           |            admin             |     2020-05-20 16:27:51      |
|            admin             |            admin             |     2020-05-19 10:57:24      |
---------------------------------------------------------------------------------------------
```

### **updatePassword**

管理员运行updatePassword，修改自身账号的登录密码。参数：

- 旧登录密码：修改前自身账号的登录密码
- 新登录密码：修改后自身账号的登录密码，不能与旧登录密码重复，6-20位字母数字下划线组成

```text
[admin:admin]> updatePassword Abcd1234 123456
Update password successfully.
```

### **restorePrivateKey**

管理员运行restorePrivateKey，恢复其生成的访客托管的私钥。参数：

- 账号名称：需恢复的私钥所有者的账号名称
- 私钥别名：需恢复的私钥标识
- 管理员私钥：管理员自身妥善保管的私钥，用于解密托管的密码密文，长度64，不区分大小写
  
```text
[admin:admin]> restorePrivateKey user1 pkey1 56edd4d56db20ad3b7b387fb8963a639c020282c6a4195f7a699b1b487fb6567
The private key "pkey1" of account "user1" is 2d330f79e17dd4645cc69d46222d82b8532894d82bb14449da5f9d69930380be.
```

### **uploadPrivateKey**

访客运行uploadPrivateKey，上传需托管的私钥。参数：

- 私钥文件：存储私钥信息的文件，文件格式为p12或者pem，存储路径为dist/accounts/目录。该目录的私钥文件可使用脚本`get_account.sh`或`get_gm_account.sh`生成
- 加密密码：由访客提供用于对托管的私钥进行对称加密的密码，如果私钥文件为p12格式，该密码也用于解密p12文件
- 私钥别名：[可选] 用于唯一标识该访客下的私钥，默认为该私钥生成的以太坊地址（0x开头）
  
```text
[user1:visitor]> uploadPrivateKey 0xa424aa0a388e47bde6fb55c98ec2d6ba92e30f6b.p12 123456 pkey1
Upload a private key "pkey1" successfully.
```

### **listPrivateKey**

访客运行listPrivateKey，显示其托管的私钥列表。
  
```text
[user1:visitor]> listPrivateKey 
The count of keys uploaded by "user1" is 2.
---------------------------------------------------------------------------------------------
|                    alias                    |                 createTime                  |
| 0xe6396f0a6b2ae72a17562cccfeb3cac770806e39  |             2020-05-19 21:34:00             |
|                    pkey1                    |             2020-05-19 21:20:22             |
---------------------------------------------------------------------------------------------
```

### **exportPrivateKey**

访客运行exportPrivateKey，找回其托管的私钥。参数：

- 私钥别名：用于唯一标识该访客下的私钥
- 加密密码：由访客提供用于对托管的私钥进行对称加密的密码
  
```text
[user1:visitor]> exportPrivateKey pkey1 123456
The private key "pkey1" is 2d330f79e17dd4645cc69d46222d82b8532894d82bb14449da5f9d69930380be.
```

### **deletePrivateKey**

访客运行deletePrivateKey，删除其托管的私钥。参数：

- 私钥别名：用于唯一标识该访客下的私钥
  
```text
[user1:visitor]> deletePrivateKey key1
Delete the private key "key1" successfully.
```

### 控制台通过Nginx访问密钥管理服务

#### 1. 源码编译

##### 1.1 获取源码

```text
wget -c https://nginx.org/download/nginx-1.18.0.tar.gz
```

##### 1.2 解压并进入源码目录

```text
tar -zxvf nginx-1.18.0.tar.gz && cd nginx-1.18.0
```

##### 1.3 配置指定安装目录及启用SSL支持

```text
./configure --prefix=/data/home/app/nginx/ --with-http_ssl_module
```

注：安装目录不能为当前目录

##### 1.4 编译安装

```text
make && make install
```

##### 1.5 进入安装目录，启动、检查、停止Nginx

```text
cd /data/home/app/nginx/ 
./sbin/nginx
ps aux | grep nginx
./sbin/nginx -s stop
```

#### 2. 证书生成

##### 2.1 创建证书目录

```text
cd /data/home/app/nginx/ && mkdir ssl && cd ssl
```

##### 2.2 创建密钥

```text
openssl genrsa -des3 -out nginx_kms.key 1024
```

过程中输入密码

##### 2.3 生成证书签名请求

```text
openssl req -new -key nginx_kms.key -out nginx_kms.csr
```

过程中输入需密钥密码及按提示操作，可一路回车。

##### 2.4 生成字签名证书

```text
openssl x509 -req -days 365 -in nginx_kms.csr -signkey nginx_kms.key -out nginx_kms.crt
```

过程中输入需密钥密码

#### 3. 修改Nginx配置

修改配置文件，文件中http部分增加以下内容，指定后端服务的IP与Port，以及负载均衡策略。

```text
    upstream tomcats{
        server 192.168.0.1:9501 weight=1;
        server 192.168.0.2:9501 weight=1;
    }
```

文件中server部分增加以下内容，指定启用SSL服务及所用证书

```text
    server {
        listen       5080 ssl;
        server_name  localhost;

        ssl_certificate     /data/home/app/nginx-1.18.0/ssl/nginx_kms.crt;
        ssl_certificate_key /data/home/app/nginx-1.18.0/ssl/nginx_kms.key;
        ssl_session_cache    shared:SSL:1m;
        ssl_session_timeout  5m;
        ssl_ciphers  HIGH:!aNULL:!MD5;
        ssl_prefer_server_ciphers  on;

        #charset koi8-r;

        #access_log  logs/host.access.log  main;
```

重新加载Nginx配置文件，过程中输入需密钥密码。

```text
    ./sbin/nginx -s reload
```

#### 4. 修改控制台配置

```text
sed -i "s/serviceIP:servicePort/127.0.0.1:5080/g" applicationContext.yml
```