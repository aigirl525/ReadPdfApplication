Main2Activity  是集成了  implementation 'com.github.barteksc:android-pdf-viewer:2.8.2'这个pdf解析器

减小引入pdf解析器导致的apk安装包过大
android {

//实际引用libs资源位置
    repositories {
        flatDir {
            dirs 'libs'
        }
    }
    splits {
        abi {
            enable true
            reset()
            include 'armeabi-v7a' //如果想包含其他cpu平台使用的so，修改这里即可
        }
    }
}

ScaleImageView  可以放大缩小的自定义view
MainActivity 展示的是pdf转成图片合成的h5，安卓这边目前没有实现pdf转h5