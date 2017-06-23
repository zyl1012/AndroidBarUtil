# AndroidBarUtil
A util for setting status bar and navigation bar style on Android App.

# Example
compile 'com.zyl1012.util:AndroidBarUtil:1.0.0'<br/>
BarUtil.setStausBar<br/>
BarUtil.setNavigationBar<br/>
BarUtil.setStatusAndNavigationBar<br/>

BarUtil.setStatusAndNavigationBar(this, toobarColor, false, false, toobarColor, false, false);
### Kitat
![](https://github.com/zyl1012/AndroidBarUtil/blob/master/screenshots/kitat_color.png)
### Lollipop
![](https://github.com/zyl1012/AndroidBarUtil/blob/master/screenshots/lollipop_color.png)

BarUtil.setStatusAndNavigationBar(this, toobarColor, true, false, toobarColor, true, false);
### Kitat
![](https://github.com/zyl1012/AndroidBarUtil/blob/master/screenshots/kitat_color_translucent.png)
### Lollipop
![](https://github.com/zyl1012/AndroidBarUtil/blob/master/screenshots/lollipop_color_translucent.png)

BarUtil.setStatusAndNavigationBar(this, Color.TRANSPARENT, true, true, Color.TRANSPARENT, true, true);
### Kitat
![](https://github.com/zyl1012/AndroidBarUtil/blob/master/screenshots/kitat_color_transparent.png)
### Lollipop
![](https://github.com/zyl1012/AndroidBarUtil/blob/master/screenshots/lollipop_color_transparent.png)

# License
Apache 2.0
