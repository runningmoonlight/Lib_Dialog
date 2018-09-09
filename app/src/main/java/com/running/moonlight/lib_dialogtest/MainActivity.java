package com.running.moonlight.lib_dialogtest;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.running.moonlight.lib_dialog.CustomDialog;
import com.running.moonlight.lib_dialog.DefinedDialog;
import com.running.moonlight.lib_dialog.base.BaseDialogFragment;
import com.running.moonlight.lib_dialog.base.InitDialogListener;
import com.running.moonlight.lib_dialog.base.ViewConvertListener;
import com.running.moonlight.lib_dialog.base.ViewHolder;
import com.running.moonlight.lib_dialog.picker.listener.InitTimeRangeListener;
import com.running.moonlight.lib_dialog.picker.listener.OnOptionSelectListener;
import com.running.moonlight.lib_dialog.picker.listener.OnOptionsSelectListener;
import com.running.moonlight.lib_dialog.picker.listener.OnTimeSelectListener;
import com.running.moonlight.lib_dialog.picker.listener.OnWheelViewSetListener;
import com.running.moonlight.lib_dialog.picker.listener.OnWheelViewsSetListener;
import com.running.moonlight.lib_dialog.picker.option.LinkedOptionsDialog;
import com.running.moonlight.lib_dialog.picker.option.SingleOptionDialog;
import com.running.moonlight.lib_dialog.picker.option.UnLinkedOptionsDialog;
import com.running.moonlight.lib_dialog.picker.time.TimeDialog;
import com.running.moonlight.lib_dialog.picker.wheel.view.WheelView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

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

    public void showOption(View view) {
        final ArrayList<String> list = new ArrayList<>(20);
        for (int i = 0; i < 20; i++) {
            list.add("数据" + i);
        }

        SingleOptionDialog<String> dialog = SingleOptionDialog.init(list)
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
                });

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
}

