# passwordtext 是一个特殊的输入框，和EditText很像，但是它具有方格分割内容，适用于密码、验证码等分割显示
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
 ####Add it in your root build.gradle at the end of repositories:
 
 
 ```groovy
 allprojects {
 		repositories {
 			
 			maven { url 'https://jitpack.io' }
 		}
 	}
 	
 	
 
 ```
 ####Add the dependency
 ```groovy
 dependencies {
 	        implementation 'com.github.liliuchen:passwordtext:v0.0.2'
 	}
 ```
 

