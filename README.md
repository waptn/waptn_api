##  腾鸟解析接口协议

### 1 签名
每一次接口调用，都需要加上接口签名（sign）字段，服务器用该字段值鉴定接口确实由授权客户端发起。

签名算法涉及到以下两个参数：

参数|意义|长度|是否必填
---|---|---|---
secretKey | 客户端密钥| 10 |是
timestamp | 当前时间戳，包括毫秒 | 13 | 是

将这两个参数及**具体接口指定参与排序的字段的值**以如下方式拼接后，取MD5值，即：
```
MD5(SecretKey + timestamp + interfaceValue)
```
提示：在视频解析接口中，interfaceValue的值即为videoPageUrl字段的值，即视频链接

### 2 接口
#### 接口地址：
```
http://www.waptn.com/api/index.php

###### 接口方式：POST
###### 接口Content-Type：application/json;charset=UTF-8
###### 接口形式：JSON

###### 本接口参与接口加密的字段为：
```
videoPageUrl
```

###### 接口入参：

参数|意义|长度|是否必填
---|---|---|---
clientId|客户ID||是
timestamp|当前时间戳||是
sign|接口加密值||是
videoPageUrl | 视频页面地址 ||是
platformCode | 视频平台编码，该字段非必填，但建议传值。如果没有该字段值，我方将以自己的平台匹配规则匹配该URL所属视频平台。但我方的规则必然存在滞后或不完善 | 10 | 否

##### 接口示例
```
{
	"clientId": "A08CFA1C13377F4AE94B33C759F639C7",
	"timestamp": 1547377778335,
	"sign": "5cde1430464d6cbd440c28acdc9c6e71",
	"videoPageUrl": "http://v.douyin.com/NXaEU6/"
}
```

###### 接口出参：
参数|意义|类型|长度|是否必填
---|---|---|---|---
code | 处理结果编码 | 字符串 | |是
msg | 处理结果消息 | 字符串| | 否
data | 处理结果数据 | 对象 | |成功时是
data.title| 视频标题 | 字符串| |否
data.coverUrl| 视频封面URL| 字符串| |否
data.videoUrl| 视频文件URL| 字符串| |成功时是
data.balanceTimes| 剩余解析次数| 数字 ||是
