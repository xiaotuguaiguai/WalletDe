
# WalletDe
使用spring boot 构建 的usdt钱包服务端控制器

### step1:
在httpprcutil类中设置你的钱包地址端口以及访问密码
```java
 private static final String RPC_HOST = "your wallet host,eg:199.9.9.9";
    private static final String RPC_PORT = "your wallet port,eg:9527";
    private String RPC_USER = "your prc user";
    private String RPC_PASSWORD = "you are rpc password ";
```
### setp2:
在WalletDeApplicationTests类中调试你的方法。