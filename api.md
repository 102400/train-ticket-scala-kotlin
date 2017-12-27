# 通用
- /api
- request:
```json
{
  "clientType": "android | ios | web | ...",
  "phone": "noToken时传空",
  "token": "noToken时传空",
  "data": {
    "...": "..."
  }
}
```
- response:
```json
{
  "code": "",
  "message": "",
  "data": {
    "...": "..."
  }
}
```

# 账户

## 注册
- POST /account/register
- request:
```json
{
  "phone": "手机号",
  "password": "密码"
}
```
- response:
```json
{
}
```

## 登陆
- POST /account/login
- request:
```json
{
  "phone": "手机号",
  "password": "密码"
}
```
- response:
```json
{
  "token": ""
}
```

## 验证Token

- POST /account/verifyToken
- request:
```json
""
```
- response:
```json
{
}
```

# 图

## 地点列表
- POST /graph/locationList
- request:
```json
""
```
- response:
```json
["地点名称"]
```

## 起始地点可到达的地点列表
- POST /graph/startToLocationList
- request:
```json
{
  "startLocation": "起始地点",
  "canTransfer": "能否换乘"
}
```
- response:
```json
["地点名称"]
```

## 起始地点到结束地点所有车次详细内容
- POST /graph/startToEndTrainNumberList
- request:
```json
{
  "startLocation": "起始地点",
  "endLocation": "结束地点",
  "canTransfer": "能否换乘(无效参数)",
  "year": "乘车年",
  "month": "",
  "day": ""
}
```
- response:
```json
[{
  "lineName": "线路名",
  "trainNumber": "车次",
  "trainName": "车型",
  "startTime": "开车时间",
  "distance": "距离 km",
  "travelTime": "到达终点耗时 min",
  "endTime": "到达时间",
  "price": "价格"
}]
```

# 订单

## 提交订单
- POST /order/submitOrder
- request:
```json
{
  "lineName": "线路名",
  "trainNumber": "车次",
  "trainName": "车型",
  "startTime": "开车时间",
  "distance": "距离 km",
  "travelTime": "到达终点耗时 min",
  "endTime": "到达时间",
  "price": "价格"
}
```
- response:
```json
""
```

## 订单列表
- POST /order/orderList
- request:
```json
""
```
- response:
```json
[{
  "lineName": "线路名",
  "trainNumber": "车次",
  "trainName": "车型",
  "startTime": "开车时间",
  "distance": "距离 km",
  "travelTime": "到达终点耗时 min",
  "endTime": "到达时间",
  "price": "价格"
}]
```