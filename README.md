仿新浪微博客户端应用
==
    使用新浪微博提供的API，仿新浪微博客户端的一款应用，目前已经实现的功能：
    【2017-8-1】
    1.微博登录
    2.关注微博的显示
    3.热门微博显示
    4.文字微博发布
    5.图片微博发布
    6.微博转发
    7.微博用户查看
## 登录
    根据新浪微博API文档使用Retrofit发送网络请求，使用WebView进入登录界面登录
![image](https://github.com/codeCacher/Picture/blob/7d0195bcefaa01359840811da3c963bc346c2bcd/thumb/Screenshot_2017-08-01-16-36-11.png?raw=true)![image](https://github.com/codeCacher/Picture/blob/7d0195bcefaa01359840811da3c963bc346c2bcd/thumb/Screenshot_2017-08-01-16-36-35.png?raw=true)
## 主页
    用户关注微博和热门微博的展示，优化了图片加载线程逻辑，使滑动更加流畅。
    
![image](https://github.com/codeCacher/Picture/blob/7d0195bcefaa01359840811da3c963bc346c2bcd/thumb/Screenshot_2017-08-01-16-37-08.png)

    普通图片和gif图片显示，可显示普通图片，长图和GIF图片，支持图片的放大查看。
    
![image](https://raw.githubusercontent.com/codeCacher/Picture/7a1d9b60cecd882211c8d7950920d722b323a23c/thumb/Screenshot_2017-08-02-14-32-56.png)

## 微博正文

    微博正文，可显示表情，支持网页连接，并显示该微博相关的评论内容。
    
![image](https://raw.githubusercontent.com/codeCacher/Picture/14680a24ceea93a8c02feb45452842b48b155329/thumb/Screenshot_2017-08-02-14-42-18.png)

## 微博发送和转发

    支持文字微博发送，图片文字混合微博的发送，可以从手机相册中选择图片或调用系统相机拍摄照片。微博转发。
    
![image](https://github.com/codeCacher/Picture/blob/master/thumb/Screenshot_2017-08-01-16-38-51.png)![image](https://github.com/codeCacher/Picture/blob/master/thumb/Screenshot_2017-08-01-16-38-39.png?raw=true)![image](https://raw.githubusercontent.com/codeCacher/Picture/c4cc3e93d70242e235a750daf978cffc31685934/thumb/Screenshot_2017-08-02-14-48-42.png)

## 用户信息

    展示微博用户的基本信息，微博和相册，向下滚动时顶部状态栏会滚动隐藏，粘滞式的下拉刷新。
    
![image](https://github.com/codeCacher/Picture/blob/master/thumb/Screenshot_2017-08-01-16-37-38.png?raw=true)![image](https://github.com/codeCacher/Picture/blob/master/thumb/Screenshot_2017-08-01-16-37-33.png?raw=true)

### 附上演示视频
https://github.com/codeCacher/Picture/tree/master/video
