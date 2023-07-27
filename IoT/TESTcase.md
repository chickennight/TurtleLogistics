## TEST CASE
GET_URL : http://i9a204.p.ssafy.io:8080/order/start
POST_URL : http://i9a204.p.ssafy.io:8080/order/update : {order_num, type, result} type(0:포장, 1:분류), result(0:성공,1:에러)
기기고장 : type:0 result:1


### SuperVisor 
```
1. GET -> /mod/ord/motor/power -> /sup/ord/sch/info, /sup/ord/veri/info, /sup/div/veri/info
2. SUB(/div/res,/ord/res) -> POST(/log)
3. SUB(/ord/res) -> /mod/div/motor/power
```

### Ord Verifier
```

```

### Div Veirifier

### Ord,Div Motor
```
1. /log , 연결되면 오는지
2. /mod/ord/motor/power { "power" : "1" } -> /mod/ord/motor/power { "power" : "-1" } -> /mod/ord/motor/speed {"speed":"100"} -> 
 /mod/ord/motor/power { "power" : "1"} ->  /mod/ord/motor/speed {"speed":"100"}
```
### Ord Sch

### Div Servo

