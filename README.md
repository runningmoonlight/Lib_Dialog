# Lib_Dialog使用文档

### 1.概述
&emsp;&emsp;`Lib_Dialog`是通过封装`DialogFragment`实现的`Dialog`模块，使用链式调用对`Dialog`进行设置，可替代`PopupWindow`，方便灵活。  
&emsp;&emsp;`Lib_Dialog`有良好的扩展性，有两个方便使用继承类：一个是`CustomDialog`，继承于`BaseDialogFragment`，用于实现布局简单的`自定义View`的`Dialog`；一个是`DefinedDialog`，直接继承于`DialogFragment`，用于直接使用系统的`Dialog`和已定义好的`Dialog`（已定义好的`Dialog`本可以直接继承`DialogFragment`来扩展，`DefinedDialog`统一了调用风格。  
&emsp;&emsp;在封装的基础上重写了选项选择器和时间选择器，根据业务需求，选项选择器分别有单项选择器、多项（最多三项）不联动选择器和多项联动选择器。  

### 2.CustomDialog
`CustomDialog`使用示例：  
<pre>
public void showCustom(View view) {
        CustomDialog.init()
                .setLayoutId(R.layout.dialog_confirm)//布局
                .setConvertListener(new ViewConvertListener() {//设置UI的样式
                    @Override
                    public void convertView(ViewHolder holder, final BaseDialogFragment dialogFragment) {
                        holder.setText(R.id.title, "提示");
                        holder.setTextColor(R.id.title, getResources().getColor(R.color.title_red));
                        holder.setText(R.id.message, "您已支付成功！");
                        holder.setBackgroundColor(R.id.message, Color.GRAY);
                        holder.setOnClickListener(R.id.cancel, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogFragment.dismiss();
                            }
                        });
                        holder.setOnClickListener(R.id.ok, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(MainActivity.this, "onClicked Confirm",
                                        Toast.LENGTH_SHORT).show();
                                dialogFragment.dismiss();
                            }
                        });
                    }
                })
	//                .setStyleId(R.style.LibDialog)//风格
	//                .setAnimStyleId(R.style.DefaultAnimation)//动画
	//                .setWidth(WindowManager.LayoutParams.MATCH_PARENT)//宽度，可以是MATCH_PARENT、WRAP_CONTENT和具体数值
	//                .setHeight(WindowManager.LayoutParams.WRAP_CONTENT)//高度，同上
	//                .setHMargin(200)//水平方向边距
	//                .setHMargin(200)//垂直方向边距
                .setOutCancelable(false)//点击外部是否隐藏
                .setX(100)//相对于原始位置的x坐标偏移
                .setY(200)//y坐标偏移
                .setGravity(Gravity.START|Gravity.TOP)//显示位置
                .setDimAmount(0.4f)//灰度值
                .showDialog(getSupportFragmentManager(), "custom");
    }  
</pre>
&emsp;&emsp;其中，`ViewConvertListener`用于设置`Dialog`中view的样式，包括字体大小、字体颜色、背景、点击事件。  
注意，`init()`放在最前面，子类的方法优先在前面调用，如上，`setLayoutId()`和`setConvertListener()`是`CustomDialog`的方法，需要放在前面，其它属性的设置方法是`BaseDialogFragment`所有，放在后面。  

### 3.DefinedDialog
`DefinedDialog`使用示例：  
<pre>
public void showSystem(View view) {
        DefinedDialog.init()
                .setInitDialogListener(new InitDialogListener() {//返回已定义的dialog
                    @Override
                    public Dialog create() {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("系统dialog")
                                .setMessage("测试已有的dialog")
                                .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(MainActivity.this, "点击取消", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .setNegativeButton("测试", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(MainActivity.this, "test", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .setCancelable(false);
                        return builder.create();
                    }
                })
                .showDialog(getSupportFragmentManager(), "system");
    }  
</pre>
&emsp;&emsp;`DefinedDialog`只有以上代码中的方法，`Dialog`的设置全部由定义好的`Dialog`自己封装。
注意，`DefinedDialog`必须设置`InitDialogListener`，用来创建已有的`Dialog`，否则，会抛出错误。  

### 4.WheelView
&emsp;&emsp;`WheelView`是一个3D滚动控件，在原有基础上了做了一些改动，完善接口封装，可以单独使用，可通过xml布局文件设置`WheelView`的属性，见`R.styleable.WheelView`。也可以通过代码设置，见下：  
<pre>
    private String mLabel;// 附加单位
    private int mTextSize;// 选项的文字大小
    private int mOutTextColor = 0xffa8a8a8;
    private int mCenterTextColor = 0xff2a2a2a;
    private int mDividerColor = 0xffd5d5d5;

    boolean mLoop;// item是否循环
    private int mGravity = Gravity.CENTER;
    private float mLineSpaceMultiplier = LINE_SPACE_MULTIPLIER;// 条目间距倍数，限制为1.0~2.0
</pre>  
&emsp;&emsp;实现原理见[Android-PickerView系列之源码解析篇（二）](https://blog.csdn.net/qq_22393017/article/details/59488906)  

### 5.SingleOptionDialog 
&emsp;&emsp;单项选择器，因为项目中单项选择的需求较多，所以单独分出来写，这样代码也简介清晰。  
使用示例：  
<pre>
public void showOption(View view) {
        final ArrayList<String> list = new ArrayList<>(20);
        for (int i = 0; i < 20; i++) {
            list.add("数据" + i);
        }

        SingleOptionDialog.init(list)
                .setTitle("单项选择器")//设置标题
                .setSelectPosition(5)//设置居中项
                .setConvertListener(new ViewConvertListener() {//设置UI样式
                    @Override
                    public void convertView(ViewHolder holder, BaseDialogFragment dialogFragment) {
                        holder.setTextColor(R.id.tv_confirm, Color.YELLOW);
                        holder.setTextColor(R.id.tv_title, Color.LTGRAY);
                    }
                })
                .setWheelViewSetListener(new OnWheelViewSetListener() {//设置WheelView样式
                    @Override
                    public void setWheelView(WheelView wheelView) {
                        wheelView.setTextSize(30);
                        wheelView.setDividerColor(Color.RED);
                        wheelView.setCenterTextColor(Color.RED);
                    }
                })
                .setOptionSelectListener(new OnOptionSelectListener() {//设置选中回调
                    @Override
                    public void onOptionSelect(int option) {
                        Toast.makeText(MainActivity.this, list.get(option), Toast.LENGTH_SHORT).show();
                    }
                })
                .showDialog(getSupportFragmentManager(), "option");
    }
</pre>  
&emsp;&emsp;可通过`ViewConvertListener`设置包裹`WheelView`的外部布局的样式，还有`OnWheelViewSetListener`用于设置`WheelView`的属性，`OnOptionSelectListener`用于设置点击右上角确认按钮时的回调。  

### 6.LinkedOptionsDialog
&emsp;&emsp;多项联动选择器，最多三项，主要用于选择省市县地址。  
使用示例：  
<pre>
public void showLinkedOptions(View view) {
        final ArrayList<String> list1 = new ArrayList<>(20);
        final ArrayList<ArrayList<String>> list2 = new ArrayList<>(20);
        final ArrayList<ArrayList<ArrayList<String>>> list3 = new ArrayList<>(20);
        for (int i = 0; i < 20; i++) {
            list1.add("一级" + i);
            list2.add(new ArrayList<String>(20));
            list3.add(new ArrayList<ArrayList<String>>(20));

            for (int j = 0; j < 20; j++) {
                list2.get(i).add("二级" + "-" + i + "-" + j);
                list3.get(i).add(new ArrayList<String>(20));

                for (int k = 0; k < 20; k++) {
                    list3.get(i).get(j).add("三级" + "-" + i + "-" + j + "-" + k);
                }
            }
        }

        LinkedOptionsDialog.init(list1, list2, list3)
                .setTitle("三级联动")
                .setWheelViewsSetListener(new OnWheelViewsSetListener() {//设置WheelView样式
                    @Override
                    public void setWheelViews(WheelView wheelView1, WheelView wheelView2, WheelView wheelView3) {
                        wheelView1.setOutTextColor(Color.RED);
                    }
                })
                .setOptionsSelectListener(new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int position1, int position2, int position3) {
                    Toast.makeText(MainActivity.this,
                            list1.get(position1) + "~" + list2.get(position1).get(position2) + "~" + list3.get(position1).get(position2).get(position3),
                            Toast.LENGTH_SHORT).show();
                    }
                })
                .showDialog(getSupportFragmentManager(), "threeLinked");

    }
</pre>  
&emsp;&emsp;其中，`LinkedOptionsDialog`可通过`ViewConvertListener`设置包裹`WheelView`的外部UI的样式，通过`OnWheelViewsSetListener`设置`WheelView`的属性，通过`OnOptionsSelectListener`设置点击右上角确认按钮时的回调。  
&emsp;&emsp;注意，`LinkedOptionsDialog`通过泛型控制实体类形参，对于数字或`String`，使用`init(List,List,List)`做初始化；对于其他实体类，需要实现`IPickerViewEntity`接口,用于在获取绘制在`WheelView`中的内容，还需要实现`Serializable`接口，因为保存信息的时候需要序列化（说明，`ArrayList`不支持`Parcelable`序列化，为了简单统一，选项选择器中的实体类都使用`Serializable`）,初始化使用`init()`方法，然后调用`setOptionsList(ArrayList, ArrayList, ArrayList)`方法设置实体类数据列表。  

### 7.UnLinkedOptionsDialog  
&emsp;&emsp;多项非联动选择器，最多三项，目前展业项目没有用到。  
使用示例：
<pre>
public void showUnLinkedOptions(View view) {
        final ArrayList<String> list1 = new ArrayList<>(20);
        final ArrayList<String> list2 = new ArrayList<>(20);
        final ArrayList<String> list3 = new ArrayList<>(20);

        for (int i = 0; i < 20; i++) {
            list1.add("一级数据" + i);
            list2.add("二级数据" + i);
            list3.add("三级数据" + i);
        }

        UnLinkedOptionsDialog.init(list1, list2, list3)
                .setTitle("三级非联动")
                .setSelectPositions(2, 3, 4)
                .setOptionsSelectListener(new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int position1, int position2, int position3) {
                        Toast.makeText(MainActivity.this, list1.get(position1) + list2.get(position2) + list3.get(position3),
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .showDialog(getSupportFragmentManager(), "threeUnLinked");
    }
</pre>

&emsp;&emsp;`UnLinkedOptionsDialog`使用类似于`LinkedOptionsDialog`，更加简单。  

### 8.TimeDialog
&emsp;&emsp;时间选择器，支持年月日时分秒，默认只显示年月日。其中年月日可以设置起始和结束时间边界，默认1900~2100；年月日时分秒可以设置选中居中的值，默认当前时间。  
使用示例：  
<pre>
    public void showTime(View view) {
        TimeDialog.init()//默认时间为当前时间
                .setTitle("时间选择器")
                .setMvVisibility(true, true, true, true, true, true)//设置显示的WheelView，对应年月日时分秒
                .setInitTimeRangeListener(new InitTimeRangeListener() {//设置时间范围，只支持年月日
                    @Override
                    public Calendar getStartCalendar() {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.YEAR, 2010);
                        calendar.set(Calendar.MONTH, 0);
                        calendar.set(Calendar.DAY_OF_MONTH, 10);
//                        calendar.set(Calendar.HOUR_OF_DAY, 8);
//                        calendar.set(Calendar.MINUTE, 20);
//                        calendar.set(Calendar.SECOND, 10);
                        return calendar;//2010.1.10 8:20:10
                    }

                    @Override
                    public Calendar getEndCalendar() {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.YEAR, 2020);
                        calendar.set(Calendar.MONTH, 9);
                        calendar.set(Calendar.DAY_OF_MONTH, 30);
//                        calendar.set(Calendar.HOUR_OF_DAY, 22);
//                        calendar.set(Calendar.MINUTE, 30);
//                        calendar.set(Calendar.SECOND, 30);
                        return calendar;//2020.10.30 22:30:30
                    }

                    @Override
                    public Calendar getSelectedCalendar() {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.YEAR, 2019);
                        calendar.set(Calendar.MONTH, 9);
                        calendar.set(Calendar.DAY_OF_MONTH, 18);
                        calendar.set(Calendar.HOUR_OF_DAY, 12);
                        calendar.set(Calendar.MINUTE, 30);
                        calendar.set(Calendar.SECOND, 25);
                        return calendar;//2019.10.18 12:30:25
                    }
                })
//                .setSelectedTime(Calendar)//另一个设置居中时间的方法
//                .setRangeTime(Calendar, Calendar)//另一个设置时间范围的方法
                .setOnTimeSelectListener(new OnTimeSelectListener() {//设置回调
                    @Override
                    public void onTimeSelect(Date date) {
                        DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Toast.makeText(MainActivity.this, DATE_FORMAT.format(date), Toast.LENGTH_SHORT).show();
                    }
                })
                .showDialog(getSupportFragmentManager(), "time");

    }
</pre>  
&emsp;&emsp;`TimeDialog`可以设置包裹`WheelView`外部的UI，也可以设置`WheelView`的属性。
&emsp;&emsp;另外，`InitTimeRangeListener`用于设置起始时间和结束时间的边界，以及居中选中时间。`OnTimeSelectListener`用于回调右上角点击确认事件，返回一个`Date`。  

### 9.一些注意事项  
1. 当`WheelView`中绘制的内容过长时，可能会和`Label`重叠，可能会超出`WheelView`的宽度；
2. 目前默认`Lable`固定绘制在居中`WheelView`的右侧，如果想让每个`WheelView`都带`Label`的话，只能把`Label`添加到实体类中；  
3. `WheelView`默认展示11个`Item`，不可修改；这样，`WheelView`的高度由文字大小和间距决定。文字很大时，UI会有影响。  
  
### 10.最后
&emsp;&emsp;有问题请联系邮箱*lihengd@yonyou.com*，欢迎交流，提出修改建议。


