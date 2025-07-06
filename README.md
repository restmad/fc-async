## 注意
组件本身不做权限校验。错误列表的访问，应该由应用系统来控制。

需要在拦截器加上任务列表静态资源和接口的访问拦截。如：/async/index.html和/xy/async/page


## 异步策略
![image](https://github.com/xiongyanokok/fc-async/assets/11241127/1b7aebf1-d4f7-49ee-8830-bcfc48237ebf)

本组件多加了一个SAVE_SYNC，先保存数据库，再同步执行业务方法（比如调用某个接口、保存某个数据），如果业务方法失败，则可以使用错误列表进行重推
## 安全级别
![image](https://github.com/xiongyanokok/fc-async/assets/11241127/12432d25-b910-4475-94f6-177237b41b20)


## 效果展示
![image](https://github.com/xiongyanokok/fc-async/assets/11241127/27129d28-417d-4d0c-b0b5-6138b26e4c11)


## 数据库脚本
``` sql
CREATE TABLE `async_req` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `application_name` varchar(100) NOT NULL DEFAULT '' COMMENT '应用名称',
  `sign` varchar(50) NOT NULL DEFAULT '' COMMENT '方法签名',
  `class_name` varchar(200) NOT NULL DEFAULT '' COMMENT '全路径类名称',
  `method_name` varchar(100) NOT NULL DEFAULT '' COMMENT '方法名称',
  `async_type` varchar(50) NOT NULL DEFAULT '' COMMENT '异步策略类型',
  `exec_status` tinyint NOT NULL DEFAULT '0' COMMENT '执行状态 0：初始化 1：执行失败 2：执行成功',
  `exec_count` int NOT NULL DEFAULT '0' COMMENT '执行次数',
  `param_json` longtext COMMENT '请求参数',
  `remark` varchar(200) NOT NULL DEFAULT '' COMMENT '业务描述',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_applocation_name` (`application_name`) USING BTREE,
  KEY `idx_exec_status` (`exec_status`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='异步处理请求';

CREATE TABLE `async_log` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `async_id` bigint NOT NULL DEFAULT '0' COMMENT '异步请求ID',
  `error_data` longtext COMMENT '执行错误信息',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_async_id` (`async_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='异步处理日志';
```


## 配置
```properties
fc.async.enabled=true
fc.async.kafka.enabled=false
fc.async.datasource.url=jdbc:mysql://localhost:3306/demo?characterEncoding=utf-8&useSSL=false&zeroDateTimeBehavior=convertToNull&useTimezone=true&serverTimezone=GMT%2B8
fc.async.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
fc.async.datasource.username=javaweb
fc.async.datasource.password=javaweb
```

### 以下配置都有默认值
#### 核心线程数
fc.async.executor.thread.corePoolSize=10
#### 最大线程数
fc.async.executor.thread.maxPoolSize=50
#### 队列容量
fc.async.executor.thread.queueCapacity=10000
#### 活跃时间
fc.async.executor.thread.keepAliveSeconds=600

#### 执行成功是否删除记录：默认删除
fc.async.exec.deleted=true
 
#### 自定义队列名称前缀：默认应用名称
fc.async.topic=应用名称
 
#### 重试执行次数：默认1次
fc.async.exec.count=1
 
#### 重试最大查询数量
fc.async.retry.limit=100

#### 补偿最大查询数量
fc.async.comp.limit=100

#### 登录开关：默认关闭
fc.async.login.enabled=false


## 用法
#### 1，异步开关
async.enabled=true

#### 2，在需要异步执行的方法加注解 （必须是spring代理方法）
@AsyncExec(type = AsyncExecEnum.SAVE_ASYNC, remark = "数据字典")

#### 3，人工处理地址
http://localhost:8004/async/index.html



## 注意 
kafka 和 job  需要自行配置实现<br>

当然也可以替换掉这两个组件



