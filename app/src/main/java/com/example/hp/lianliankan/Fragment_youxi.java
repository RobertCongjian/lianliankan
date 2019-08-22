package com.example.hp.lianliankan;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class Fragment_youxi  extends Fragment{
    public int sourceId[] = {R.drawable.image_1, R.drawable.image_2, R.drawable.image_3, R.drawable.image_4, R.drawable.image_5,
            R.drawable.image_6, R.drawable.image_7, R.drawable.image_8, R.drawable.image_9, R.drawable.image_10,
            R.drawable.image_11, R.drawable.image_12, R.drawable.image_13, R.drawable.image_14, R.drawable.image_15,
            R.drawable.image_16,R.drawable.image_17,R.drawable.image_18,R.drawable.image_19,R.drawable.image_20};

    public int imageId[][] = {{R.id.t1_1, R.id.t1_2, R.id.t1_3, R.id.t1_4, R.id.t1_5, R.id.t1_6},
            {R.id.t2_1, R.id.t2_2, R.id.t2_3, R.id.t2_4, R.id.t2_5, R.id.t2_6},
            {R.id.t3_1, R.id.t3_2, R.id.t3_3, R.id.t3_4, R.id.t3_5, R.id.t3_6},
            {R.id.t4_1, R.id.t4_2, R.id.t4_3, R.id.t4_4, R.id.t4_5, R.id.t4_6},
            {R.id.t5_1, R.id.t5_2, R.id.t5_3, R.id.t5_4, R.id.t5_5, R.id.t5_6},
            {R.id.t6_1, R.id.t6_2, R.id.t6_3, R.id.t6_4, R.id.t6_5, R.id.t6_6},
            {R.id.t7_1, R.id.t7_2, R.id.t7_3, R.id.t7_4, R.id.t7_5, R.id.t7_6}};

    public int music[]={R.raw.defeat,R.raw.bgm,R.raw.xiaochu,R.raw.shengli,R.raw.diancuo};
    public int position[][]=new int[9][8];
    public int suijshu1[];//为了保证每个图形都是双数，设置两个随机数组生成图形
    public int suijishu2[];
    public int suiji_x[]=new int[7];//图片随机加载到表格的某个位置
    public int suiji_y[]=new int[6];
    public static int i = -1;//判断作用，用于从随机数数组中取出一个数
    public static int i2 = -1;
    public static int i3 = -1;//用作判断，打乱重排功能
    public static int state = 3;//难度的3中状态，1为入门，2为简单，3为困难
    public static ImageView selimage = null;//当前选中的图片
    public static int[] fx = {1,0,-1,0};//下右上左
    public static int[] fy = {0,1,0,-1};
    public ArrayList<Integer>listx=new ArrayList<>();//存放已经遍历过的横坐标
    public ArrayList<Integer>listy=new ArrayList<>();
    public static int time = 60;//游戏时间
    public TextView textView;
    public static int firstclick = 1;//标志位，第一次点击后改变
    public EditText shuru;
    public String shurushijian;
    public static int changtime = 60;
    private MediaPlayer mediaPlayer1 = new MediaPlayer();
    //private MediaPlayer mediaPlayer2 = new MediaPlayer();//背景音乐
    private MediaPlayer mediaPlayer3 = new MediaPlayer();
    private MediaPlayer mediaPlayer4 = new MediaPlayer();
    private MediaPlayer mediaPlayer5 = new MediaPlayer();
    public static int times = 0;//记录游戏好多秒

    ImageView imageView;
    Button button;//进入排行榜按钮

//    Switch aSwitch_beijing;
//    Switch aSwitch_yinxiao;
//    boolean beijing = true;
    public static boolean yinxiao;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    public ArrayList<Drawable> list = new ArrayList<>();//用于打乱重排
    public int chongpaisuiji[];

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            time--;
            times++;
            textView.setText(time+"");
            handler.postDelayed(this,1000);
            if(time==0){
                Toast toast;
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.custom,
                        (ViewGroup) getActivity().findViewById(R.id.llToast));
                ImageView image = (ImageView) layout
                        .findViewById(R.id.tvImageToast);
                image.setImageResource(R.drawable.shibai);
                toast = new Toast(getContext());
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(layout);
                toast.show();
                new Thread(shibai).start();
                handler.post(runnable);
                handler.removeCallbacks(runnable);
            }
        }
    };



//    private Runnable bgm  = new Runnable() {
//        @Override
//        public void run() {
//            while (beijing){
//                mediaPlayer2.start();
//                handler.postDelayed(this,100000);
//            }
//        }
//    };
    private Runnable shibai = new Runnable() {
        @Override
        public void run() {
            if (yinxiao){
                mediaPlayer1.start();
            }

        }
    };

    private Runnable xiaochu = new Runnable() {
        @Override
        public void run() {
            if (yinxiao){
                mediaPlayer3.start();
            }

        }
    };

    private Runnable shengli = new Runnable() {
        @Override
        public void run() {
            if (yinxiao){
                mediaPlayer4.start();
            }

        }
    };

    private Runnable diancuo = new Runnable() {
        @Override
        public void run() {
            if (yinxiao){
                mediaPlayer5.start();
            }

        }
    };

    /**初始化音乐播放器**/
    public void initMediaPlayer(){
        mediaPlayer1 = MediaPlayer.create(getContext(), music[0]);
        //mediaPlayer2 = MediaPlayer.create(getContext(), music[1]);
        mediaPlayer3 = MediaPlayer.create(getContext(), music[2]);
        mediaPlayer4 = MediaPlayer.create(getContext(), music[3]);
        mediaPlayer5 = MediaPlayer.create(getContext(), music[4]);
    }

//    public void releaseSource(MediaPlayer Player){
//        if(Player!=null){
//            Player.stop();
//            //关键语句
//            Player.reset();
//            Player.release();
//            Player = null;
//
//        }
//    }

    /**释放音乐播放器的资源**/
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mediaPlayer1!=null){
            mediaPlayer1.stop();
            mediaPlayer1.release();
        }
//        if(mediaPlayer2!=null){
//            mediaPlayer2.stop();
//            mediaPlayer2.release();
//        }
        if(mediaPlayer3!=null){
            mediaPlayer3.stop();
            mediaPlayer3.release();
        }
        if(mediaPlayer4!=null){
            mediaPlayer4.stop();
            mediaPlayer4.release();
        }
        if(mediaPlayer5!=null){
            mediaPlayer5.stop();
            mediaPlayer5.release();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        saveData();
    }

    /**构造函数**/
    public Fragment_youxi() {
    }

    /**碎片从隐藏到显示执行的动作，在设置那页设置好时间转到游戏页面执行**/
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden&&time==changtime){
            shuru = (EditText)getActivity().findViewById(R.id.shezhishijian);
            shurushijian = shuru.getText().toString();
            time= Integer.parseInt(shurushijian);
            changtime = time;
            textView.setText(changtime+"");
        }
//        if(!hidden) {
//            controlmusic();
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_1, container, false);
        setHasOptionsMenu(true);
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        textView=(TextView)getActivity().findViewById(R.id.daojishi);
        button = (Button)getActivity().findViewById(R.id.paihang);
        initMediaPlayer();

        //new Thread(bgm).start();
        pref = this.getActivity().getSharedPreferences("page1", Context.MODE_PRIVATE);

        initData(16,5);
        initgame();
        getData();
        click();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.example.paihang.ACTION_START");
                startActivity(intent);
            }
        });
    }

    /**第一次点击图片后开启计时线程**/
    public void jishi(){
        handler.postDelayed(runnable,1000);
    }

    /**创建菜单并使其显示**/
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.system_menu,menu);
        //在 onCreate() 期间调用 setHasOptionsMenu() 来指出fragment愿意添加item到选项菜
        //单(否则, fragment将接收不到对 onCreateOptionsMenu()的调用)
        menu.setGroupVisible(R.menu.system_menu, true);

    }
    /**菜单子项的点击事件**/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        i=-1;
        i2=-1;
        i3=-1;
        Toast toast = null;
        switch (item.getItemId()){
            case R.id.system_chongxinkaishi:
                if(state == 3){
                    initData(16,5);
                }
                if(state == 2){
                    initData(11,10);
                }
                if(state==1){
                    initData(7,0);
                }
                i=-1;
                i2=-1;
                break;
            case R.id.system_xuanzenandu:
                break;
            case R.id.rumen:
                if(state==1){
                    toast = Toast.makeText(getContext(),"这已经是入门难度",Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    LinearLayout toastView=(LinearLayout)toast.getView();
                    pl.droidsonroids.gif.GifImageView imageView = new pl.droidsonroids.gif.GifImageView(getContext());
                    imageView.setImageResource(R.drawable.tishi);
                    toastView.addView(imageView,0);
                    toast.show();
                }else {
                    initData(7,0);
                    state = 1;
                    i=-1;
                    i2=-1;
                }
                break;
            case R.id.jiandan:
                if(state==2){
                    toast = Toast.makeText(getContext(),"这已经是简单难度",Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    LinearLayout toastView=(LinearLayout)toast.getView();
                    pl.droidsonroids.gif.GifImageView imageView = new pl.droidsonroids.gif.GifImageView(getContext());
                    imageView.setImageResource(R.drawable.tishi);
                    toastView.addView(imageView,0);
                    toast.show();
                }else {
                    initData(11,10);
                    state = 2;
                    i=-1;
                    i2=-1;
                }
                break;
            case R.id.kunnan:
                if(state==3){
                    toast = Toast.makeText(getContext(),"这已经是困难难度",Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    LinearLayout toastView=(LinearLayout)toast.getView();
                    pl.droidsonroids.gif.GifImageView imageView = new pl.droidsonroids.gif.GifImageView(getContext());
                    imageView.setImageResource(R.drawable.tishi);
                    toastView.addView(imageView,0);
                    toast.show();
                }else {
                    initData(16,5);
                    state = 3;
                    i=-1;
                    i2=-1;
                }
                break;
            case R.id.system_daluanchongpai:
                chongpai();
                break;

            default:
        }
        return true;
    }



    /**返回一个从0到n-1的随机数数组**/
    public  static int[] randomCommon(int n){
        int Random[] = new int[n];
        for (int i = 0; i < n; i++) {
            // int ran=-1;
            while (true) {
                int ran = (int) (n * Math.random());
                for (int j = 0; j < i; j++) {
                    if (Random[j] == ran) {
                        ran = -1;
                        break;
                    }
                }
                if (ran != -1) {
                    Random[i]=ran;
                   break;
                }
            }

        }
        for(int i = 0 ; i < n ; i ++)
        {
            Log.i("aaaaaaa","随机数="+Random[i]+","+i);
        }
        return Random;
    }

    /**每次从随机数数组中返回一个值**/
    public int ran_1(int n){
        i++;
        while (i==n){
            i=0;
        }
        if(i<n){
            return suijshu1[i];
        }
        return 0;
    }

    public int ran_2(int m){
        i2++;
        while (i2==m){
            i2=0;
        }
        if(i2<m){
            return suijishu2[i2];
        }
        return 0;
    }

    public int ran_3(int p){
        i3++;
        while (i3==p){
            i3=0;
        }
        if(i3<p){
            return chongpaisuiji[i3];
        }
        return 0;
    }
    /**初始化游戏数据**/
    public void initData(int n,int m){
        firstclick=1;
        time = changtime;
        times = 0;//计时器
        textView.setText(time+"");
        handler.post(runnable);
        handler.removeCallbacks(runnable);
        state=3;
        int x = 0;
        suijshu1 = randomCommon(n);
        suijishu2 = randomCommon(m);
        suiji_x = randomCommon(7);
        suiji_y = randomCommon(6);
        for(int i=0;i<7;i++){
            for(int j=0;j<6;j++){
                imageView = (ImageView)getActivity().findViewById(imageId[suiji_x[i]][suiji_y[j]]);
                if(imageView!=null){
                    if(n==7&&m==0){
                        imageView.setImageResource(sourceId[ran_1(n)]);
                    }
                    else {
                        imageView.setImageResource(sourceId[ran_1(n)]);
                        x++;
                        if(x>n*2&&x<=42){
                            imageView.setImageResource(sourceId[ran_2(m)]);
                        }
                    }
                }
            }
        }
    }

    /**初始化数组，有图的置1，无图路径置0**/
    public void initgame(){
        for(int i=0;i<9;i++){
            for(int j=0;j<8;j++){
                position[0][j]=0;
                position[8][j]=0;
                position[i][0]=0;
                position[i][7]=0;
                if(i>0&&i<8&&j>0&&j<7){
                    position[i][j]=1;
                }
            }
        }
    }

    /**打乱重排**/
    public void chongpai(){
        for(int i=0;i<9;i++){
            for(int j=0;j<8;j++){
                if(position[i][j]==1){
                    int x=i-1;
                    int y=j-1;
                    ImageView imageView = (ImageView)getActivity().findViewById(imageId[x][y]);
                    Drawable drawable = imageView.getDrawable();
                    list.add(drawable);
                }
            }
        }
        int length = list.size();
        chongpaisuiji = randomCommon(length);
        for(int i=0;i<9;i++){
            for(int j=0;j<8;j++){
                if(position[i][j]==1){
                    int x=i-1;
                    int y=j-1;
                    imageView = (ImageView)getActivity().findViewById(imageId[x][y]);
                    if(imageView!=null){

                        imageView.setImageDrawable(list.get(ran_3(length)));
                    }
                }
            }
        }
        list.clear();
    }


    /**异常类，为跳出递归准备**/
    static class StopMsgException extends RuntimeException { }

        /**深度优先搜索遍历寻找路径,找到退出递归，消除匹配图形，否则返回0，表示失败**/
    public int DFSTraverse(int x,int y,ImageView imageView){
        int newx;
        int newy;
        int[]shuzu;
        shuzu = getselimagexy();
        position[shuzu[0]][shuzu[1]]=0;
        for(int i=0;i<4;i++){
            newx = x+fx[i];
            newy = y+fy[i];
            if(newx>=0&&newx<9&&newy>=0&&newy<8&&position[newx][newy]==0){
                position[newx][newy]=2;//标志已经走过
                listx.add(newx);
                listy.add(newy);
                Log.i("新坐标1","x="+newx+" "+"y="+newy);
                if(newx==shuzu[0]&&newy==shuzu[1]){
                    Log.i("aaaa","路径找寻成功");
//                    ((ViewGroup) imageView.getParent()).removeView(imageView);
//                    ((ViewGroup) selimage.getParent()).removeView(selimage);
                    new Thread(xiaochu).start();
                    imageView.setImageDrawable(null);
                    selimage.setImageDrawable(null);
                    selimage=null;
                    position[shuzu[0]][shuzu[1]]=0;

                    Log.i("newxy","newxy="+position[x][y]+":"+"newx="+x+"newy="+":"+y);
                    for(int k=0;k<listx.size();k++){
                        position[listx.get(k)][listy.get(k)]=0;
                    }
                    listx.clear();
                    listy.clear();
                    throw new StopMsgException(); //抛出异常,强制跳出递归
                }
                else {
                        DFSTraverse(newx,newy,imageView);
                }
            }
        }
            position[x][y]=0;
        return 0;
    }

    /**获取当前选中的图的坐标**/
    public int[] getselimagexy(){
        int x=0;
        int y=0;
        int[]shuzu=new int[2];
        for(int i=0;i<7;i++){
            for(int j=0;j<6;j++){
                if(selimage.getId()==imageId[i][j]){
                    x=i+1;
                    y=j+1;
                }
            }
        }
        shuzu[0]=x;
        shuzu[1]=y;
        Log.i("坐标","x="+x+" "+"y="+y);
        return shuzu;
    }

    /**胜负判定**/
    public void sheng(){
        int x=0;
        Toast toast;
        for(int i=0;i<9;i++){
            for(int j=0;j<8;j++){
                System.out.print(position[i][j]+" ");
                if(position[i][j]==0){
                    x++;
                    if(x==72&&time>0){
//                        toast = Toast.makeText(getContext(),"恭喜！游戏通关",Toast.LENGTH_SHORT);
//                        toast.setGravity(Gravity.CENTER,0,0);
//                        LinearLayout toastView=(LinearLayout)toast.getView();
//                        pl.droidsonroids.gif.GifImageView imageView = new pl.droidsonroids.gif.GifImageView(getContext());
//                        imageView.setImageResource(R.drawable.tishi);
//                        toastView.addView(imageView,0);
//                        toast.show();
                        handler.post(runnable);
                        handler.removeCallbacks(runnable);
                        new Thread(shengli).start();
                        handler.post(shengli);
                        handler.removeCallbacks(shengli);
                        alert_edit();
                        x=0;
                        initgame();
                    }
                }

            }
            System.out.println();
        }

    }

    /**点击图片后事务的处理**/
    public void game(ImageView view){
        int x=0;
        int y=0;
        for(int i=0;i<7;i++){
            for(int j=0;j<6;j++){
                if(imageId[i][j]==view.getId()){
                    x=i+1;
                    y=j+1;
                }
            }
        }
        if(view.getDrawable()!=null){
            if(selimage==null||view.getDrawable().getCurrent().getConstantState()!=selimage.getDrawable().getCurrent().getConstantState()||view.getId()==selimage.getId()){
                if(selimage!=null&&view.getId()!=selimage.getId()){
                    selimage.setBackground(null);
                }
                selimage = view;
                selimage.setBackgroundColor(Color.BLACK);
            }else {
                position[x][y]=0;
                selimage.setBackground(null);
                try {
                    if(DFSTraverse(x, y, view)==0){
                        new Thread(diancuo).start();
                        for(int i=0;i<7;i++){
                            for(int j=0;j<6;j++){
                                if(selimage.getId()==imageId[i][j]){
                                    position[i+1][j+1]=1;
                                }
                            }
                        }
                        selimage.setBackground(null);
                        selimage=view;
                        selimage.setBackgroundColor(Color.BLACK);
                        for(int i=0;i<7;i++){
                            for(int j=0;j<6;j++){
                                if(selimage.getId()==imageId[i][j]){
                                    position[i+1][j+1]=1;
                                }
                            }
                        }
                    }
                }catch (StopMsgException e){
                }

            }

        }

    }
    /**点击响应**/
    public void click(){
            for(int i=0;i<7;i++){
                for(int j=0;j<6;j++){
                    imageView = (ImageView)getActivity().findViewById(imageId[i][j]);
                    if(imageView!=null){
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(firstclick==1){
                                    jishi();
                                    firstclick = 0;
                                }
                                ImageView view = (ImageView)v;
                                game(view);
                                sheng();
                            }
                        });
                    }
                }
            }

    }

    /**发送广播的函数**/
    public void sendbroadcast(String name){
        Intent intent = new Intent();
        intent.putExtra("name",name);
        intent.putExtra("zhuheyu","恭喜通关");
        intent.putExtra("shichang",times+"");
        //Toast.makeText(getContext(), times+"", Toast.LENGTH_LONG).show();
        SimpleDateFormat formatter   =   new   SimpleDateFormat   ("MM月dd日");
        Date curDate =  new Date(System.currentTimeMillis());
        String   str   =   formatter.format(curDate);
        //Toast.makeText(getContext(), str, Toast.LENGTH_LONG).show();
        intent.putExtra("dangqianshijian",str);
        intent.setAction("paihangbang");
        BroadCastManager.getInstance().sendBroadCast(getActivity(),intent);
    }

    /**游戏成功执行，弹出对话框，要求输入姓名，点确定后发送广播**/
    public void alert_edit(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());//对话框构建器
        View view2 = View.inflate(getContext(), R.layout.dialog, null);
        final EditText et = (EditText) view2.findViewById(R.id.dialog);

                builder.setTitle("恭喜通关，请输入昵称")
                .setIcon(R.drawable.image_1)
                .setView(et)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                        //按下确定键后的事件
                        String name = et.getText().toString().trim();
                        //Toast.makeText(getContext(), name, Toast.LENGTH_LONG).show();
                        sendbroadcast(name);
                    }
                }).setNegativeButton("取消",null).show();
    }

    /**对游戏中声音的控制,老的想法，通过在设置页面获取信息，
     * 在转到游戏页面的 onHiddenChanged()方法里能成功获取，设置关闭。
     * 现在操作放在设置页面，更符合逻辑**/
//    public void controlmusic(){
//        aSwitch_beijing = (Switch)getActivity().findViewById(R.id.beijingkaiguan);
//        aSwitch_yinxiao = (Switch)getActivity().findViewById(R.id.yinxiaokaiguan);
//        beijing = aSwitch_beijing.isChecked();
//        yinxiao = aSwitch_yinxiao.isChecked();
//        if(!beijing){
//            mediaPlayer2.pause();
//        }
//        if(beijing){
//            mediaPlayer2.start();
//        }
//    }

    /**下面两个函数用户偏好设置，保存和取出对音效的开或关**/
    public void saveData(){
        editor = pref.edit();
        editor.putBoolean("yinxiao_music",yinxiao);
        editor.apply();
    }

    public void getData(){
       boolean yinxiao_ = pref.getBoolean("yinxiao_music",true);
       yinxiao = yinxiao_;
    }


}
