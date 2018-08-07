0.txt
query: Google
preference: google的子公司
num:24
ps:
examples 25496 522144 1392341

1.txt
query: Hotel California
preference: 与其共同作者最多的音乐
num:16

2.txt
query: Suriname
preference:	说共同语言的Place(3481468)
num:15

3.txt
query: Vagn_Walfrid_Ekman
preference: 与他一个领域的科学家
num:60

4.txt
query: Vagn_Walfrid_Ekman
preference: 相同母校相同国籍（nationnality）的人
num:32
ps:因为类别不统一的原因，效果贼差
pss:以上问题已完美解决

5.txt
query: Nobel physics prize
preference: 校友获得人次最多的学校
num:31

6.txt
query: LeBron James
preference: 与詹姆斯被同一个球队选中的NBA现役球员
num:12

7.txt
query: LeBron James
preference: 与詹姆斯相同位置相同选秀球队的球员
num:21
ours:10/10 13/20
relsim:1/10 3/20

8.txt
query: James Horner
preference: 霍纳为其电影作曲最多的导演
num:27

9.txt
query: James Horner
preference: 霍纳为其电影作曲最多的发行商
num:12

10.txt
query: Francis Ford Coppola
preference: 参演其导演电影次数最多的演员
num:15

11.txt
query: Francis Ford Coppola
preference: 参演其编剧电影次数最多的演员
num:13

12.txt
query: Viking Press
preference: 作品在该出版社出版最多的作者
num:26

13.txt
query: Viking Press
preference: 该出版社出版最多的作品类型
num:26

14.txt
query: William Goldman
preference: 参演其编剧的电影次数最多的演员
num:10

15.txt
query: The Dark Knight Trilogy
preference: 与其共同演员最多的电影
num:20
ours: 10/10 19/20
relsim: 10/10 19/20
ps:这里20个数据里可能有一个错的

*16.txt
query: The Dark Knight Trilogy
preference: 相关合作人员最多的电影
num:10

17.txt
query: Francis Ford Coppola
preference: 由其编剧的电影
num: 18

18.txt
query: The Dark Knight Trilogy
preference: 与该电影相同摄影师
num: 18

19.txt
query: The Dark Knight Trilogy
preference: 相同导演的电影
num: 12

20.txt
query: carrot cake
preference: 原料最相近的食物（hetesim）
num: 16

21.txt
query: Viking Press
preference: 与该出版社出版作品类型最相近的出版社(hetesim)
num: 20
ps: 这个问题太难了，在服务器上跑的时候把阈值改成了0.13，例子用的703093，1390030,1743524
服务器上跑出的结果：910418, 21289, 988472, 249861, 1743524, 960650, 1390030, 488063, 444755, 464666, 2159803, 716722, 1503343, 2778634, 573418, 2585117, 1408506, 835865, 2077015, 1418477
准确率只有一半左右，不过看了一下学出来的权重还是比较合理的。
mp:[[3481579, -3480832, 3481545, 3480866, 3481453, -3480866, 3481545, 3480832, 3481928], [3481928, -3480832, 3481506, 3480832, 3481928], [3481453, -3480832, 3481545, 3480832, 3481928]]

22.txt
query: Jay Z
preference: 与Jay Z音乐类型最相似的20个歌手
num:20
ps：难度极高，后面实验可以考虑取30条meta path

23.txt
query: Mawson Peak
preference: 与Mawson Peak同一类型的山
num:61

24.txt
query: Oregon wine
preference: 该酒庄的葡萄
num: 70

25.txt
query: Oregon wine
preference: 与该酒庄生产葡萄最相似的酒庄(hetesim)
num: 20
ps
完美： 10/10 18/20
relsim: 3/10 3/20
例子：1137600 1338828 600376

26.txt
query: The Sopranos
preference: 其剧集
num: 86

27.txt
query: Christian Bale
preference: 与其合作最多的演员
num: 14

28.txt
query: Christian Bale
preference: 与其合作最多的导演
num：34

29.txt
query: Loire_Valley_(wine)
preference 与该酒庄生产葡萄最相似的酒庄(hetesim)
num: 20

30.txt
query: William Shatner
preference: 参演电影导演导演的电影(pathCount)
num:53

31.txt
query: William Shatner
preference: 导演电影的演员参演的电影(PCRW + hetesim)
num:12

32.txt
query: Elton John
preference: 所唱的歌的作者写的歌
num:85

33.txt
query: Blink (web engine)
preference: 同一编程语言，同一开发者的软件
num:18

34.txt
query: Blink (web engine)
preference: 同一编程语言同样操作系统同样license的软件
num:11

35.txt
query: Michael Jordan
preference: 与乔丹在同一个联盟且毕业于同一个学校的球员
num: 18

36.txt
query: Christian Bale
preference: 与其参演的电影有共同演员和共同导演的电影
num: 102

37.txt
query: Oregon wine
preference: 生产有相同葡萄且同一个subregion的酒庄
num: 15

38.txt
query: Last Action Hero
preference: 和其有同样演员同样发行商的电影
num: 25

39.txt
query: Francis Ford Coppola
preference: 自编自导的电影
num: 14

40.txt
query: Francis Ford Coppola
preference: 其同时是导演和制作人的电影
num: 10

41.txt
query: Google
preference: 开发商和作者均为谷歌的软件
num: 11

42.txt
query: Kyrie Irving
preference: 与欧文同一队选中的在同一个联盟打球的球员
num: 12

43.txt
query: Tad Murty
preference: 与Tad Murty同一领域的科学家
num: 60

44.txt
query: Sugar cookie
preference: 与其原料最相近的食物(hetesim)
num: 27

45.txt
query: Mount Lurus
preference: 与Mount Lurus同一类型的山
num:61

46.txt
query: Canelé
preference: 与其原料最相近的食物(hetesim)
num: 10

47.txt
query: Jae Crowder
preference: 与克劳德相同位置相同选秀球队的球员
num: 21

48.txt
query: Pastry
preference: 与其原料最相近的食物(hetesim)
num: 11

49.txt
query: Walter Munk
preference: 与Walter Munk同一个领域的科学家
num: 60