# GongGeWidget
宫格控件


布局中引用：
# 1、宽高wrap_content，表示父控件最大限制为屏幕宽高
# 2、direction：表示排版方向，start 左对齐/上对齐；end 右对齐/下对齐；between 等大小均分某方向尺寸
# 3、column：列数，只在orientation：horizontal 且 direction：between 生效
# 4、row：行数，只在orientation：vertical 且 direction：between 生效
# 5、orientation：horizontal 水平方向；vertical 垂直方向
# 6、horizontalSpace：水平间距
# 7、verticalSpace：垂直间距

<com.android.work.widgetmodel.widget.GongGeWidget
                    android:id="@+id/gongGe"
                    android:layout_width="wrap_content" 
                    android:layout_height="wrap_content"
                    android:background="#fffccfff"
                    app:direction="start"
                    app:horizontalSpace="10dp"
                    app:orientation="horizontal"
                    app:verticalSpace="10dp">
                    
                    ....子控件
                    
</com.android.work.widgetmodel.widget.GongGeWidget
