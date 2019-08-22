package com.example.hp.lianliankan;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.BottomNavigationView;
import android.os.Bundle;
import android.view.MenuItem;



public class MainActivity extends AppCompatActivity {
    Fragment_shezhi fragment_shezhi = null;
    Fragment_shuoming fragment_shuoming = null;
    Fragment_youxi fragment_youxi = null;
    private FragmentTransaction transaction;
    private Fragment fragment;//当前fragment
    private LocalReceiver localReceiver;
    public MediaPlayer mediaPlayer = new MediaPlayer();//背景音乐播放
    public static boolean beijing1;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        initFragment();
        getBroadcast();
        mediaPlayer = MediaPlayer.create(this,R.raw.bgm);
        getData();
        new Thread(bgm).start();

    }

    public void saveData(){
        editor = pref.edit();
        editor.putBoolean("beijing_music",beijing1);
        editor.apply();
    }

    public void getData(){
        boolean beijing1_ = pref.getBoolean("beijing_music",true);
        beijing1=beijing1_;
    }





    //Handler handler1 = new Handler();
    private Runnable bgm  = new Runnable() {
        @Override
        public void run() {
            if(beijing1){
                mediaPlayer.start();
                mediaPlayer.setLooping(true);
            }
        }
    };



    @Override
    protected void onDestroy() {
        saveData();
        BroadCastManager.getInstance().unregisterReceiver(this,localReceiver);
            if(mediaPlayer!=null){
                mediaPlayer.stop();
                mediaPlayer.release();
            }

        super.onDestroy();
    }

    /**初始化Fragment**/
    public void initFragment(){
        fragment_youxi = new Fragment_youxi();
        fragment_shuoming = new Fragment_shuoming();
        fragment_shezhi = new Fragment_shezhi();
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.Fragmentlayout, fragment_youxi,"page1")
                .commit();
        fragment =fragment_youxi;
        getSupportFragmentManager().beginTransaction().add(R.id.Fragmentlayout,fragment_shezhi,"page3").hide(fragment_shezhi).commit();
    }

    /**选择Fragment**/
    private void switchfragment(Fragment mfragment,String page){
        if(fragment!=mfragment){
            if(!mfragment.isAdded()){
                getSupportFragmentManager().beginTransaction().hide(fragment)
                        .add(R.id.Fragmentlayout,mfragment,page).commit();
            }
            getSupportFragmentManager().beginTransaction().hide(fragment).show(mfragment).commit();
        }
        fragment = mfragment;
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_youxi:
                    switchfragment(fragment_youxi,"page1");
                    return true;
                case R.id.navigation_shuoming:
                    switchfragment(fragment_shuoming,"page2");
                    return true;
                case R.id.navigation_shezhi:
                    switchfragment(fragment_shezhi,"page3");
                    return true;
            }
            return false;
        }
    };

    public void getBroadcast(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("paihangbang");
        localReceiver = new LocalReceiver();
        BroadCastManager.getInstance().registerReceiver(this,localReceiver,intentFilter);//注册广播
    }

    class LocalReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            String name = intent.getStringExtra("name");
            //Toast.makeText(context,name,Toast.LENGTH_SHORT).show();
            String shichang = intent.getStringExtra("shichang");
            //Toast.makeText(context,shichang,Toast.LENGTH_SHORT).show();
            String dangqianshijian = intent.getExtras().get("dangqianshijian").toString();
            //Toast.makeText(context,dangqianshijian,Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(MainActivity.this,paihangbang.class);
            intent1.putExtra("name",name);
            intent1.putExtra("shichang",shichang);
            intent1.putExtra("dangqianshijian",dangqianshijian);
            intent1.setAction("com.fromMain");
            startActivity(intent1);

        }
    }



}
