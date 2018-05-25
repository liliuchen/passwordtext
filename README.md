# passwordtext 是一个特殊的输入框，和EditText很像，但是它具有方格分割内容，适用于密码、验证码等分割显示#
[![](https://jitpack.io/v/liliuchen/passwordtext.svg)](https://jitpack.io/#liliuchen/passwordtext)
![](https://github.com/liliuchen/passwordtext/blob/master/app/Screenshots/23456.gif)
![](https://github.com/liliuchen/passwordtext/blob/master/app/Screenshots/234562.gif)
 

```xml
 <com.lucenlee.library.RectPasswordText
        android:layout_width="match_parent"
        android:layout_height="48dp"
        android:paddingBottom="5dp"
        android:paddingLeft="5dp"
        android:paddingRight="5dp"
        android:paddingTop="5dp"
        android:textSize="16sp"
        app:prt_length="8" 
        app:prt_rectangle_color_focus="#F00"
        app:prt_proclaimed_show="true"
        app:prt_margin="10dp" />
        
      
 ```
 ```xml
  <declare-styleable name="RectPasswordText">
         <!--密码的长度-->
         <attr name="prt_length" format="integer"></attr>
         <!--矩形框的聚焦颜色-->
         <attr name="prt_rectangle_color_focus" format="reference|color"></attr>
         <!--矩形框无焦点的颜色-->
         <attr name="prt_rectangle_color_no_focus" format="reference|color"></attr>
         <!--矩形之间的间隔-->
         <attr name="prt_margin" format="dimension"></attr>
         <!--边框的宽度-->
         <attr name="prt_border_width" format="dimension"></attr>
         <!--是否明文显示-->
         <attr name="prt_proclaimed_show" format="boolean"></attr>
     </declare-styleable>
 
 ```
 
 #分割线的使用
 
 ![](https://github.com/liliuchen/passwordtext/blob/master/app/Screenshots/20180525160903.png)
 
 ```xml
 <com.lucenlee.library.LineView
         android:layout_width="match_parent"
         android:layout_height="5dp"
         android:layout_marginTop="5dp"
         app:lv_line_start_color="#F00"
         app:lv_line_end_color="#0F0"
         app:lv_line_style="doubleFullLine"
         app:lv_orientation="horizontal" />
 ```
 
 ##LineView属性##
 ```xml
  <declare-styleable name="LineView">
         <!--起始颜色-->
         <attr name="lv_line_start_color" format="color" />
         <!--结束颜色-->
         <attr name="lv_line_end_color" format="color" />
         <!--线的类型-->
         <attr name="lv_line_style">
             <!--圆形虚线-->
             <flag name="circleImaginary" value="0" />
             <!--矩形虚线-->
             <flag name="rectangularImaginary" value="1" />
             <!--平行四边形-->
             <flag name="rhomboidImaginary" value="2" />
             <!--线点交替-->
             <flag name="linePointImaginary" value="4" />
             <!--线正方形交替-->
             <flag name="lineSquareImaginary" value="5" />
             <!--线点点交替-->
             <flag name="lineDoublePointImaginary" value="6" />
             <!--单实线-->
             <flag name="singleFullLine" value="8" />
             <!--双实线-->
             <flag name="doubleFullLine" value="9" />
             <!--自定义-->
             <flag name="customLine" value="10" />
         </attr>
         <!--自定义形状，当lv_line_style为自定义时，才会生效-->
         <attr name="lv_custom_shape" format="reference" />
         <!--绘制方向-->
         <attr name="lv_orientation">
             <flag name="horizontal" value="0" />
             <flag name="vertical" value="1" />
         </attr>
 
     </declare-styleable>
 ```
 
 
 ###Add it in your root build.gradle at the end of repositories:###
 
 
 ```groovy
 allprojects {
 		repositories {
 			
 			maven { url 'https://jitpack.io' }
 		}
 	}
 	
 	
 
 ```
 ###Add the dependency
 ```groovy
 dependencies {
 	        implementation 'com.github.liliuchen:passwordtext:v0.0.2'
 	}
 ```
 
