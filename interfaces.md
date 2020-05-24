# FISCO BCOS 密钥管理服务（FISCO-Key-Manager Service）接口设计

##  <span id="catalog_top">目录</span>
- [1.帐号管理模块](#1)
  - [1.1.账号登录](#1.1)
  - [1.2.新增子帐号](#1.2)
  - [1.3.删除子帐号](#1.3)
  - [1.4.查询子帐号列表](#1.4)
  - [1.5.更改当前密码](#1.5)
  - [1.6.获取用于加密子账号托管私钥的公钥](#1.6)
- [2.密钥管理模块](#2)
  - [2.1.新增私钥](#2.1)
  - [2.2.删除私钥](#2.2)
  - [2.3.查询私钥列表](#2.3)
  - [2.4.查询指定私钥](#2.4)
- [3.错误码说明](#3)

## <span id="1">1 帐号管理模块</span>  [top](#catalog_top)

### <span id="1.1">1.1 账号登录</span>  [top](#catalog_top)

#### 1.1.1 传输协议规范

* 网络传输协议：使用HTTPS协议
* 请求地址：`/account/login`
* 请求方式：POST
* 请求头：Content-type: application/x-www-form-urlencoded
* 返回格式：JSON

#### 1.1.2 参数信息详情

| 序号 | 输入参数      | 类型    | 可为空 | 备注                                                 |
| ---- | ------------- | ------- | ------ | ---------------------------------------------------- |
| 1    | account       | String  | 否     | 帐号名称，要求5-20位字母数字下划线组成，且以字符开头 |
| 2    | accountPwd    | String  | 否     | 登录密码，要求6-20位字母数字下划线组成               |
| 序号 | 输出参数      | 类型    |        | 备注                                                 |
| 1    | code          | Int     | 否     | 返回码，0：成功 其它：失败                           |
| 2    | message       | String  | 否     | 描述                                                 |
| 3    | data          | object  | 否     | 返回信息实体                                         |
| 3.1  | accountStatus | Integer | 否     | 帐号状态                                             |
| 3.2  | account       | String  | 否     | 帐号名称                                             |
| 3.3  | roleName      | String  | 否     | 角色名称                                             |
| 3.4  | token         | String  | 否     | 登录标识                                             |

### 1.1.3 入参示例

`https://127.0.0.1:9501/FISCO-Key-Manager/account/login`

```
{
    "account": "admin",
    "accountPwd": "Abcd1234"
}
```

#### 1.1.4 出参示例

* 成功：
```
{
    "code": 0,
    "data": {
        "accountStatus": 1,
        "roleName": "admin",
        "account": "admin",
        "token": "c3348a56159739dde190a622830e892bea05e9d2206c43fc1bdcdd07fdc79eb4"
    },
    "message": "success"
}
```

* 失败：
```
{
    "code": 100000,
    "message": "system exception",
    "data": null
}
```


### <span id="1.2">1.2 新增子帐号</span>  [top](#catalog_top)

#### 1.2.1 传输协议规范

* 网络传输协议：使用HTTPS协议
* 请求地址：`/account/addAccount`
* 请求方式：POST
* 请求头：Content-type: application/json
* 返回格式：JSON

#### 1.2.2 参数信息详情

| 序号 | 输入参数      | 类型          | 可为空 | 备注                                                 |
| ---- | ------------- | ------------- | ------ | ---------------------------------------------------- |
| 1    | account       | String        | 否     | 帐号名称，要求5-20位字母数字下划线组成，且以字符开头 |
| 2    | accountPwd    | String        | 否     | 登录密码，要求6-20位字母数字下划线组成               |
| 3    | roleId        | int           | 否     | 所属角色                                             |
| 4    | publicKey     | String        | 是     | 用于加密子账号托管的私钥                             |
| 序号 | 输出参数      | 类型          |        | 备注                                                 |
| 1    | code          | Int           | 否     | 返回码，0：成功 其它：失败                           |
| 2    | message       | String        | 否     | 描述                                                 |
| 3    | data          | object        | 否     | 返回信息实体                                         |
| 3.1  | account       | String        | 否     | 帐号名称                                             |
| 3.2  | accountPwd    | String        | 是     | 登录密码                                             |
| 3.3  | roleId        | Integer       | 否     | 所属角色                                             |
| 3.4  | roleName      | String        | 否     | 角色名称                                             |
| 3.5  | roleNameZh    | String        | 否     | 角色中文名                                           |
| 3.6  | accountStatus | Integer       | 否     | 帐号状态                                             |
| 3.7  | description   | String        | 是     | 备注                                                 |
| 3.8  | createTime    | LocalDateTime | 否     | 创建时间                                             |
| 3.9  | modifyTime    | LocalDateTime | 否     | 修改时间                                             |
| 3.10 | email         | String        | 是     | 用户邮箱                                             |
| 3.11 | publicKey     | String        | 否     | 用于加密子账号托管的私钥                             |
| 3.12 | creator       | String        | 否     | 创建者账号                                           |

### 1.2.3 入参示例

`https://127.0.0.1:9501/FISCO-Key-Manager/account/addAccount`

```
{
    "account": "user1",
    "accountPwd": "Abcd1234",
    "roleId": 100001,
    "publicKey": "8d83963610117ed59cf2011d5b7434dca7bb570d4a16e63c66f0803f4c4b1c03a1125500e5ca699dfbb6b48d450a82a5020fcb3b43165b508c10cb1479c6ee49"
}
```

#### 1.2.4 出参示例

* 成功：
```
{
    "code": 0,
    "message": "success",
    "data": {
        "account": "user1",
        "accountPwd": null,
        "roleId": 100001,
        "roleName": "visitor",
        "roleNameZh": "普通用户",
        "accountStatus": 1,
        "description": null,
        "createTime": "2020-05-19 19:15:01",
        "modifyTime": "2020-05-19 19:15:01",
        "email": null,
        "publicKey": "8d83963610117ed59cf2011d5b7434dca7bb570d4a16e63c66f0803f4c4b1c03a1125500e5ca699dfbb6b48d450a82a5020fcb3b43165b508c10cb1479c6ee49",
        "creator": "admin"
    }
}
```

* 失败：
```
{
    "code": 100000,
    "message": "system exception",
    "data": null
}
```

### <span id="1.3">1.3 删除子帐号</span>  [top](#catalog_top)

#### 1.3.1 传输协议规范

* 网络传输协议：使用HTTPS协议
* 请求地址：`account/deleteAccount/{account}`
* 请求方式：DELETE
* 返回格式：JSON

#### 1.3.2 参数信息详情

| 序号 | 输入参数 | 类型   | 可为空 | 备注                       |
| ---- | -------- | ------ | ------ | -------------------------- |
| 1    | account  | String | 否     | 帐号名称                   |
| 序号 | 输出参数 | 类型   |        | 备注                       |
| 1    | code     | Int    | 否     | 返回码，0：成功 其它：失败 |
| 2    | message  | String | 否     | 描述                       |
| 3    | data     | object | 是     | 返回信息实体（空）         |

#### 1.3.3 入参示例

`https://127.0.0.1:9501/FISCO-Key-Manager/deleteAccount/user1`

#### 1.3.4 出参示例

* 成功：
```
{
    "code": 0,
    "message": "Success",
    "data": null
}
```

* 失败：
```
{
    "code": 100000,
    "message": "system exception",
    "data": null
}
```

###  <span id="1.4">1.4 查询子帐号列表</span>  [top](#catalog_top)

#### 1.4.1 传输协议规范

* 网络传输协议：使用HTTPS协议
* 请求地址: `/account/accountList/{pageNumber}/{pageSize}`
* 请求方式：GET
* 返回格式：JSON

#### 1.4.2 参数信息详情

| 序号   | 输入参数      | 类型          | 可为空 | 备注                       |
| ------ | ------------- | ------------- | ------ | -------------------------- |
| 1      | pageSize      | Int           | 否     | 每页记录数                 |
| 2      | pageNumber    | Int           | 否     | 当前页码                   |
|        | 输出参数      | 类型          |        | 备注                       |
| 1      | code          | Int           | 否     | 返回码，0：成功 其它：失败 |
| 2      | message       | String        | 否     | 描述                       |
| 3      | totalCount    | Int           | 否     | 总记录数                   |
| 4      | data          | List          | 是     | 信息列表                   |
| 4.1    |               | Object        |        | 信息对象                   |
| 4.1.1  | account       | String        | 否     | 帐号名称                   |
| 4.1.2  | accountPwd    | String        | 是     | 登录密码                   |
| 4.1.3  | roleId        | Integer       | 否     | 所属角色                   |
| 4.1.4  | roleName      | String        | 否     | 角色名称                   |
| 4.1.5  | roleNameZh    | String        | 否     | 角色中文名                 |
| 4.1.6  | accountStatus | Integer       | 否     | 帐号状态                   |
| 4.1.7  | description   | String        | 是     | 备注                       |
| 4.1.8  | createTime    | LocalDateTime | 否     | 创建时间                   |
| 4.1.9  | modifyTime    | LocalDateTime | 否     | 修改时间                   |
| 4.1.10 | email         | String        | 是     | 用户邮箱                   |
| 4.1.11 | publicKey     | String        | 否     | 用于加密子账号托管的私钥   |
| 4.1.12 | creator       | String        | 否     | 创建者账号                 |

#### 1.4.3 入参示例

`https://127.0.0.1:9501/FISCO-Key-Manager/account/accountList/1/10`

#### 1.4.4 出参示例

* 成功：
```
{
    "code": 0,
    "message": "success",
    "data": [
        {
            "account": "user12",
            "accountPwd": null,
            "roleId": 100001,
            "roleName": "visitor",
            "roleNameZh": "普通用户",
            "accountStatus": 1,
            "description": null,
            "createTime": "2020-05-19 19:22:33",
            "modifyTime": "2020-05-19 19:22:33",
            "email": null,
            "publicKey": "8d83963610117ed59cf2011d5b7434dca7bb570d4a16e63c66f0803f4c4b1c03a1125500e5ca699dfbb6b48d450a82a5020fcb3b43165b508c10cb1479c6ee49",
            "creator": "admin"
        },
        {
            "account": "admin",
            "accountPwd": null,
            "roleId": 100000,
            "roleName": "admin",
            "roleNameZh": "管理员",
            "accountStatus": 1,
            "description": null,
            "createTime": "2020-05-19 10:57:24",
            "modifyTime": "2020-05-19 10:57:24",
            "email": null,
            "publicKey": "8d83963610117ed59cf2011d5b7434dca7bb570d4a16e63c66f0803f4c4b1c03a1125500e5ca699dfbb6b48d450a82a5020fcb3b43165b508c10cb1479c6ee49",
            "creator": null
        }
    ],
    "totalCount": 2
}
```

* 失败：
```
{
    "code": 100000,
    "message": "system exception",
    "data": null
}
```

### <span id="1.5">1.5 更新当前密码</span>  [top](#catalog_top)

#### 1.5.1 传输协议规范
* 网络传输协议：使用HTTPS协议
* 请求地址：`/account/updatePassword`
* 请求方式：PUT
* 请求头：Content-type: application/json
* 返回格式：JSON

#### 1.5.2 参数信息详情

| 序号 | 输入参数       | 类型   | 可为空 | 备注                       |
| 1    | oldAccountPwd | String | 否  | 旧密码          |
| 2    | newAccountPwd | String | 否  | 新密码，要求6-20位字母数字下划线组成 |
| ---- | ------------- | ------ | --- | ------------------------------------ |
| 序号 | 输出参数      | 类型   |     | 备注                                 |
| 1    | code          | Int    | 否  | 返回码，0：成功 其它：失败           |
| 2    | message       | String | 否  | 描述                                 |
| 3    | data          | object | 否  | 返回信息实体（空）                   |


### 1.5.3 入参示例

`https://127.0.0.1:9501/FISCO-Key-Manager/account/updatePassword`

```
{
    "oldAccountPwd": "Abcd1234",
    "newAccountPwd": "12345678"
}
```

#### 1.5.4 出参示例
* 成功：
```
{
    "code": 0,
    "message": "success"
    "data": null
}
```

* 失败：
```
{
    "code": 100000,
    "message": "system exception",
    "data": null
}
```

### <span id="1.6">1.6 获取用于加密子账号托管私钥的公钥</span>  [top](#catalog_top)

#### 1.6.1 传输协议规范

* 网络传输协议：使用HTTPS协议
* 请求地址：`/account/getPublicKey`
* 请求方式：GET
* 请求头：Content-type: application/json
* 返回格式：JSON

#### 1.6.2 参数信息详情

| 序号 | 输入参数  | 类型   | 可为空 | 备注                       |
| ---- | --------- | ------ | ------ | -------------------------- |
| 序号 | 输出参数  | 类型   |        | 备注                       |
| 1    | code      | Int    | 否     | 返回码，0：成功 其它：失败 |
| 2    | message   | String | 否     | 描述                       |
| 3    | data      | object | 否     | 返回信息实体               |
| 3.1  | account   | String | 否     | 加密帐号（管理员）名称     |
| 3.2  | publicKey | String | 否     | 用于加密子账号托管的私钥   |

### 1.6.3 入参示例

`https://127.0.0.1:9501/FISCO-Key-Manager/account/getPublicKey`

#### 1.6.4 出参示例

* 成功：
```
{
    "code": 0,
    "message": "success",
    "data": {
        "account": "admin",
        "publicKey": "8d83963610117ed59cf2011d5b7434dca7bb570d4a16e63c66f0803f4c4b1c03a1125500e5ca699dfbb6b48d450a82a5020fcb3b43165b508c10cb1479c6ee49"
    }
}
```

* 失败：
```
{
    "code": 100000,
    "message": "system exception",
    "data": null
}
```

## <span id="2">2 密钥管理模块</span>  [top](#catalog_top)

### <span id="2.1">2.1 新增私钥</span>  [top](#catalog_top)

#### 2.1.1 传输协议规范

* 网络传输协议：使用HTTPS协议
* 请求地址：`/escrow/addKey`
* 请求方式：POST
* 请求头：Content-type: application/json
* 返回格式：JSON

#### 2.1.2 参数信息详情

| 序号 | 输入参数   | 类型          | 可为空 | 备注                       |
| ---- | ---------- | ------------- | ------ | -------------------------- |
| 1    | cipherText | String        | 否     | 经账号创建者公钥加密       |
| 2    | keyAlias   | String        | 否     | 私钥别名                   |
| 3    | privateKey | String        | 否     | 经账号提供的密码加密       |
| 序号 | 输出参数   | 类型          |        | 备注                       |
| 1    | code       | Int           | 否     | 返回码，0：成功 其它：失败 |
| 2    | message    | String        | 否     | 描述                       |
| 3    | data       | object        | 否     | 返回信息实体               |
| 3.1  | account    | String        | 否     | 帐号名称                   |
| 3.2  | cipherText | Integer       | 否     | 经账号创建者公钥加密       |
| 3.3  | createTime | LocalDateTime | 否     | 私钥托管时间               |
| 3.4  | keyAlias   | String        | 否     | 私钥别名                   |
| 3.5  | privateKey | String        | 否     | 经账号提供的密码加密       |

### 1.1.3 入参示例

`https://127.0.0.1:9501/FISCO-Key-Manager/escrow/addKey`
```
{
    "cipherText":"048A292A94A6DDF84006C074B63627A7FAC1CD4B84EFC556124C1258CFEDC402285A66F9AB27310FA5E253D65038A664A649C35F259882E9678034928158AA90DD518C78A6B81F3A7075E74DC9320E32DB25596249EB1AC404955AC715E3812C0B61204939E8AE5CE430DBBDD014F96DA42B824C994266B2CD7A49AC92254EC2534D6AAB79F4E36367EB3EDEE6461A7A26A1A7038B",
    "keyAlias":"key1",
    "privateKey":"F2764D0F7118080EABC9236830BC714B2B249AE209C6D969E9E953D7283B42E9C9600DA7F5447158C83410CC5E91514C05B8234003465978C924D7F505221CFACB53B966BB008522E33737F44C63B4E7"
}
```

#### 1.1.4 出参示例

* 成功：
```
{
    "code": 0,
    "message": "success",
    "data": {
        "account": "user1",
        "keyAlias": "key1",
        "cipherText": "048A292A94A6DDF84006C074B63627A7FAC1CD4B84EFC556124C1258CFEDC402285A66F9AB27310FA5E253D65038A664A649C35F259882E9678034928158AA90DD518C78A6B81F3A7075E74DC9320E32DB25596249EB1AC404955AC715E3812C0B61204939E8AE5CE430DBBDD014F96DA42B824C994266B2CD7A49AC92254EC2534D6AAB79F4E36367EB3EDEE6461A7A26A1A7038B",
        "privateKey": "F2764D0F7118080EABC9236830BC714B2B249AE209C6D969E9E953D7283B42E9C9600DA7F5447158C83410CC5E91514C05B8234003465978C924D7F505221CFACB53B966BB008522E33737F44C63B4E7",
        "createTime": "2020-05-19 20:53:33"
    }
}
```

* 失败：
```
{
    "code": 100000,
    "message": "system exception",
    "data": null
}
```

### <span id="2.2">2.2 删除私钥</span>  [top](#catalog_top)

#### 2.2.1 传输协议规范

* 网络传输协议：使用HTTPS协议
* 请求地址：`/escrow/deleteKey/{account}/{keyAlias}`
* 请求方式：DELETE
* 返回格式：JSON

#### 2.2.2 参数信息详情

| 序号 | 输入参数 | 类型   | 可为空 | 备注                       |
| ---- | -------- | ------ | ------ | -------------------------- |
| 1    | account  | String | 否     | 帐号名称                   |
| 2    | keyAlias | String | 否     | 私钥别名                   |
| 序号 | 输出参数 | 类型   |        | 备注                       |
| 1    | code     | Int    | 否     | 返回码，0：成功 其它：失败 |
| 2    | message  | String | 否     | 描述                       |
| 3    | data     | object | 是     | 返回信息实体（空）         |

#### 2.2.3 入参示例

`https://127.0.0.1:9501/FISCO-Key-Manager/escrow/deleteKey/user1/key1`

#### 2.2.4 出参示例

* 成功：
```
{
    "code": 0,
    "message": "Success",
    "data": null
}
```

* 失败：
```
{
    "code": 100000,
    "message": "system exception",
    "data": null
}
```

###  <span id="2.3">2.3 查询私钥列表</span>  [top](#catalog_top)

#### 2.3.1 传输协议规范

* 网络传输协议：使用HTTPS协议
* 请求地址: `/escrow/keyList/{pageSize}/{pageNumber}`
* 请求方式：GET
* 返回格式：JSON

#### 2.3.2 参数信息详情

| 序号  | 输入参数   | 类型          | 可为空 | 备注                       |
| ----- | ---------- | ------------- | ------ | -------------------------- |
| 1     | pageSize   | Int           | 否     | 每页记录数                 |
| 2     | pageNumber | Int           | 否     | 当前页码                   |
|       | 输出参数   | 类型          |        | 备注                       |
| 1     | code       | Int           | 否     | 返回码，0：成功 其它：失败 |
| 2     | message    | String        | 否     | 描述                       |
| 3     | totalCount | Int           | 否     | 总记录数                   |
| 4     | data       | List          | 是     | 信息列表                   |
| 4.1   |            | Object        |        | 信息对象                   |
| 4.1.1 | account    | String        | 否     | 帐号名称                   |
| 4.1.2 | cipherText | Integer       | 否     | 经账号创建者公钥加密       |
| 4.1.3 | createTime | LocalDateTime | 否     | 私钥托管时间               |
| 4.1.4 | keyAlias   | String        | 否     | 私钥别名                   |
| 4.1.5 | privateKey | String        | 否     | 经账号提供的密码加密       |

#### 2.3.3 入参示例

`https://127.0.0.1:9501/FISCO-Key-Manager/escrow/keyList/1/10`

#### 2.3.4 出参示例

* 成功：
```
{
    "code": 0,
    "message": "success",
    "data": [
        {
            "account":"user1",
            "cipherText":"048A292A94A6DDF84006C074B63627A7FAC1CD4B84EFC556124C1258CFEDC402285A66F9AB27310FA5E253D65038A664A649C35F259882E9678034928158AA90DD518C78A6B81F3A7075E74DC9320E32DB25596249EB1AC404955AC715E3812C0B61204939E8AE5CE430DBBDD014F96DA42B824C994266B2CD7A49AC92254EC2534D6AAB79F4E36367EB3EDEE6461A7A26A1A7038B",
            "createTime":"2020-05-14T20:00:39",
            "keyAlias":"key1",
            "privateKey":"F2764D0F7118080EABC9236830BC714B2B249AE209C6D969E9E953D7283B42E9C9600DA7F5447158C83410CC5E91514C05B8234003465978C924D7F505221CFACB53B966BB008522E33737F44C63B4E7"
        }
    ],
    "totalCount": 1
}
```

* 失败：
```
{
    "code": 100000,
    "message": "system exception",
    "data": null
}
```

###  <span id="2.4">2.4 查询指定私钥</span>  [top](#catalog_top)

#### 2.4.1 传输协议规范

* 网络传输协议：使用HTTPS协议
* 请求地址: `/escrow/queryKey/{account}/{keyAlias}`
* 请求方式：GET
* 返回格式：JSON

#### 2.4.2 参数信息详情

| 序号 | 输入参数   | 类型          | 可为空 | 备注                       |
| ---- | ---------- | ------------- | ------ | -------------------------- |
| 1    | account    | String        | 否     | 帐号名称                   |
| 2    | keyAlias   | String        | 否     | 私钥别名                   |
|      | 输出参数   | 类型          |        | 备注                       |
| 1    | code       | Int           | 否     | 返回码，0：成功 其它：失败 |
| 2    | message    | String        | 否     | 描述                       |
| 3    | data       | object        | 否     | 返回信息实体               |
| 3.1  | account    | String        | 否     | 帐号名称                   |
| 3.2  | cipherText | Integer       | 否     | 经账号创建者公钥加密       |
| 3.3  | createTime | LocalDateTime | 否     | 私钥托管时间               |
| 3.4  | keyAlias   | String        | 否     | 私钥别名                   |
| 3.5  | privateKey | String        | 否     | 经账号提供的密码加密       |

#### 2.4.3 入参示例

`https://127.0.0.1:9501/FISCO-Key-Manager/escrow/queryKey/user1/key1`

#### 2.4.4 出参示例

* 成功：
```
{
    "code": 0,
    "message": "success",
    "data": {
        "account":"user1",
        "cipherText":"048A292A94A6DDF84006C074B63627A7FAC1CD4B84EFC556124C1258CFEDC402285A66F9AB27310FA5E253D65038A664A649C35F259882E9678034928158AA90DD518C78A6B81F3A7075E74DC9320E32DB25596249EB1AC404955AC715E3812C0B61204939E8AE5CE430DBBDD014F96DA42B824C994266B2CD7A49AC92254EC2534D6AAB79F4E36367EB3EDEE6461A7A26A1A7038B",
        "createTime":"2020-05-14T20:00:39",
        "keyAlias":"key1",
        "privateKey":"F2764D0F7118080EABC9236830BC714B2B249AE209C6D969E9E953D7283B42E9C9600DA7F5447158C83410CC5E91514C05B8234003465978C924D7F505221CFACB53B966BB008522E33737F44C63B4E7"
    }
}
```

* 失败：
```
{
    "code": 100000,
    "message": "system exception",
    "data": null
}
```

## <span id="3">3 错误码说明</span>  [top](#catalog_top)

| code   | message                                | type                          | description           |
| ------ | -------------------------------------- | ----------------------------- | --------------------- |
| 0      | success                                | return success                | 成功                  |
| 100000 | system exception                       | system exception              | 系统错误              |
| 200000 | database exception                     | business exception - database | 数据库操作异常        |
| 200100 | account info already exists            | business exception - account  | 该账号已注册          |
| 200101 | account info not exists                | business exception - account  | 该账号未注册          |
| 200102 | account name empty                     | business exception - account  | 账号名称为空          |
| 200103 | invalid account name                   | business exception - account  | 该账号不存在          |
| 200104 | invalid account format                 | business exception - account  | 账号名称格式错误      |
| 200105 | invalid password format                | business exception - account  | 登录密码格式错误      |
| 200106 | password error                         | business exception - account  | 登录密码错误          |
| 200107 | the new password cannot be same as old | business exception - account  | 新旧密码不能一致      |
| 200108 | role id cannot be empty                | business exception - account  | 角色标识不能为空      |
| 200109 | invalid role id                        | business exception - account  | 无效的角色标识        |
| 200110 | lack of access to the account          | business exception - account  | 无访问该账号权限      |
| 200200 | key info already exists                | business exception - key      | 该私钥已托管          |
| 200201 | key info not exists                    | business exception - key      | 该私钥未托管          |
| 200202 | key aliases empty                      | business exception - key      | 私钥标识不能为空      |
| 200203 | lack of access to the key              | business exception - key      | 无访问该私钥权限      |
| 200300 | invalid token                          | business exception - token    | 该Token不存在         |
| 200301 | token expire                           | business exception - token    | 该Token超时           |
| 300000 | user not logged in                     | auth exception                | 匿名用户无操作权限    |
| 300001 | access denied                          | auth exception                | 管理员/账号无操作权限 |
| 400000 | param exception                        | param exception               | 参数校验错误          |