##  腾鸟解析接口协议

### 1 签名
接口调用需加上接口签名（sign）字段，服务器用该字段值鉴定接口确实由授权客户端发起。
#### 签名计算方式：
```
sign = md5(link + timestamp + clientSecretKey)
```
参数|意义|长度|是否必填
---|---|---|---
link | 视频分享链接| 255 |是
timestamp | 当前时间戳，包括毫秒 | 13 | 是
clientSecretKey | 客户端密钥| 32 |是

```
注意：clientSecretKey可以在管理后台查看，请妥善保管；
```

### 2 接口
#### 接口地址：
```
http://www.waptn.com/api/index.php
```
###### 接口方式：POST
###### 接口Content-Type：application/json;charset=UTF-8
###### 接口形式：JSON

###### 接口输入：

参数|意义|长度|是否必填
---|---|---|---
link | 视频分享链接 |255|是
clientId|客户ID|16|是
timestamp|当前时间戳|13|是
sign|接口签名值|32|是

##### 接口示例
```
{
	"link": "http://v.xxxx.com/hq8aox/",
	"clientId": "114164da47b8f2fd",
	"timestamp": 1561799269000,
	"sign": "024190bc0b1911a070ca8b5a82292cf0"
}
```
```
注意：clientId可以在管理后台查看，sign按签名方法进行签名（sign = md5(link + timestamp + clientSecretKey)）；
```

###### 接口输出：
参数|意义|类型|长度|是否必含
---|---|---|---|---
retCode | 输出结果码 | 数字 |11 |是
retDesc | 输出结果消息 | 字符串| 255| 否
data | 处理结果数据 | 对象 | |是
data.link| 视频分享地址| 字符串 |255|是
data.title| 视频标题 | 字符串| 255|否
data.cover| 视频封面URL| 字符串| 255|否
data.video| 视频文件URL| 字符串|255 |成功时是
data.count| 剩余次数| 数字|11 |是

```
注意：retCode成功时为200，失败时一般为401；retDesc成功时为ok，失败时为失败原因（中文）；
```

