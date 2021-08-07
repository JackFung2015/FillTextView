# FillTextView
文本行尾部填充屏幕TextView
### 1.为什么开发这样一个自定义控件？
Android原生TextView控件有个特点：在一行末尾如果文本带有符号，即使剩余空间可以绘制文字也会提前换行，导致行末尾留有空白，当文本行数过多，末尾的大面积空白会导致界面很不美观。
### 2.页面效果
<img src="https://github.com/JackFung2015/FillTextView/blob/master/app/image/Screenshot_2021-08-08-01-16-30-643_com.fung.jpg" width=300>

从效果图中我们可以看到，上方自定义TextView每行末尾都没留下空白，原生TextView第一行末尾莫名多了一个汉字大小空白。文本绘制效果较原生控件绘制效果区别不大。

### 3.使用方式
```
 <com.fung.view.FillTextView
        android:id="@+id/ft"
        android:background="#ff8970"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:padding="2dp"
        ft:text="自定义TextView：经南京市委批准，南京市纪委监委对南京禄口国际机场疫情防控不力问题进行调查并开展问责。根据目前调查情况，先期对相关人员进行处理，通报如下："
        ft:textColor="@color/black"
        ft:textSize="20sp"/>
```
目前该自定义View仅支持设置文本、文本颜色、文本大小三个属性，像官方控件支持的行间距、最大行数等属性都还没有实现，需要的朋友可以自行扩展。

