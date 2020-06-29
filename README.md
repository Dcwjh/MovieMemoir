# MovieMemoir
本项目是基于Java开发的安卓app, 功能主要是提供电影搜索，电影查询，电影收藏，记录电影会议，条件查询，电影院位置map展现等功能。

# 项目简介
项目主要采用netBeans IDE 8.2自带的数据库 + glassfish4(类似tomcat), 用Android Studio编写前端和数据交互部分

API用到IMDB和google map service

# 项目截图
- 首页登录界面


<img src="./image/login.png" width = "200" height = "400" alt="首页登录界面" align="middle" />
 

- app主界面

<img src="./image/home.png" width = "200" height = "400" alt="app主界面" align="middle" />
<img src="./image/home1.png" width = "200" height = "400" alt="app主界面" align="middle" />

- 电影搜索

<img src="./image/moviesearch.png" width = "200" height = "400" alt="电影搜索" align="middle" />


- 电影详情

<img src="./image/moviedetail.png" width = "200" height = "400" alt="电影详情" align="middle" />

- 电影回忆

<img src="./image/rate.png" width = "200" height = "400" alt="电影回忆" align="middle" />


- 电影统计

<img src="./image/total1.png" width = "200" height = "400" alt="电影统计" />
<img src="./image/total2.png" width = "200" height = "400" alt="电影统计"  />


- 电影收藏

<img src="./image/shoucang.png" width = "200" height = "400" alt="电影收藏" align="middle" />


- 电影院展示

<img src="./image/map.png" width = "200" height = "400" alt="电影院展示" align="middle" />

- 用户注册

<img src="./image/register.png" width = "200" height = "400" alt="用户注册" align="middle" />


# 项目复现过程
1. 简历数据库（采用的是NetBeans上的derby）：
创建数据库使用sql.txt中的语句（附带数据）

2. 在netBeans创建项目：从数据库创建实体，创建RESTful Web service from Entity Classes.
把项目中的文件覆盖掉你的文件（里面主要添加了查询数据库的接口）

3. 用Android Studio下载项目并用gradle构建项目：构建项目之前改几个参数，确保能够正常访问

> 更改IP
位置：src/main/java/com/monash/moviememoir/networkconnect/ReportNetworkConnection.java

 private static final String BASE_URL =
            "http://****:8080/UserMovieMemoir/webresources/";
****:改成自己的IP

> IMDB API：
src/main/java/com/monash/moviememoir/networkconnect/IMDbConnect.java

在`https://imdb-api.com/ `网址中申请自己的API, 替换文件下的* `final String methodPath = "SearchMovie/********/" + keyword;`

> google map API 申请谷歌map API
替换`src/release/res/values/google_maps_api.xml`文件下的`<string name="google_maps_key" templateMergeStrategy="preserve" translatable="false">YOUR_KEY_HERE</string>
</resources>` YOUR_KEY_HERE

这样基本就可以正常运行了
项目中还有第三方平台分享也做了，但是模拟器始终安装不上微信app,导致分享失败。

> 该项目可以进一步扩展，根据用户对以往电影的评分，对比各个用户对电影的评分，可以向用户推荐电影。（问题：冷启动和矩阵稀疏，通过社交网络可以缓解这种问题（大型社交网站查询用户的爱好来填充数据，注册最好使用手机号注册，这样更好获取用户的其他数据，一边解决冷启动问题））





