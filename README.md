# postman文件转excel文件工具

## 简介

此工具用于postman文件内容转换excel格式文件输出。



## 安装

### 前提条件

- Java JDK版本 (例如: JDK 8)

- Maven
- 配置config.yml

### 编译与运行

直接运行main方法即可



## config.yml文件描述

```yml
#postmanJson输入文件的路径
postmanJsonPath: C://Users/Administrator/Desktop/bps测试/云化流程组件.postman_collection-v3.json
#excel输出文件的路径
excelPath: D://bps接口.xlsx
#目前只实现了postman文件中接口的name、path、requestParams输出,如需增加其他字段输出则在源码的基础上拓展代码实现即可。
#下面用以配置name、path、requestParams列在excel中输出的列数
columnConfig:
  name: 0
  path: 1
  requestParams: 2
```

