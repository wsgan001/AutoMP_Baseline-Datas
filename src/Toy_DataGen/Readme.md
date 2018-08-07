## 原始数据

- 路径 : Java工程根目录下./datas/toytemp/toy.in
- 格式 : 周天烁的五行数据(中心实体+不需要+样例1+样例2+不需要)，可以多组

## 配置文件

- 路径 : Java工程根目录下./src/ToyDataGen.properties
- 内容 :   
  - SubGraphStepLimit : 中心实体抽取子图的半径
  - NegativeSampleNumber : 随机抽取的负例数量，将于所有正例两两配对构成训练集

## 图谱来源

- 路径 : Java工程根目录下./src/JavaUtil.properties
- 内容 : 更改其中的数据库配置即可

## 生成ToyData格式数据

- 路径 : Java工程根目录下./datas/toydata/，一组一个文件夹，按编号命名