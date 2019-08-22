package com.example.hp.lianliankan;


import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;


public class paihangbang extends AppCompatActivity {
    int ids[]={R.id.id_0,R.id.id_1,R.id.id_2,R.id.id_3,R.id.id_4,R.id.id_5,R.id.id_6,R.id.id_7,R.id.id_8,R.id.id_9,R.id.id_10};
    int names[]={R.id.name_0,R.id.name_1,R.id.name_2,R.id.name_3,R.id.name_4,R.id.name_5,R.id.name_6,R.id.name_7,R.id.name_8,R.id.name_9,R.id.name_10};
    int shichangs[]={R.id.shichang_0,R.id.shichang_1,R.id.shichang_2,R.id.shichang_3,R.id.shichang_4,R.id.shichang_5,R.id.shichang_6,R.id.shichang_7,R.id.shichang_8,R.id.shichang_9,R.id.shichang_10};
    int shijians[]={R.id.shijian_0,R.id.shijian_1,R.id.shijian_2,R.id.shijian_3,R.id.shijian_4,R.id.shijian_5,R.id.shijian_6,R.id.shijian_7,R.id.shijian_8,R.id.shijian_9,R.id.shijian_10};
    int cards[]={R.id.card_0,R.id.card_1,R.id.card_2,R.id.card_3,R.id.card_4,R.id.card_5,R.id.card_6,R.id.card_7,R.id.card_8,R.id.card_9,R.id.card_10};
    public String name="";//排行榜的名字
    public String shichang="";//游戏耗时
    public String shijian="";//上榜时间
    TextView textView_id;
    TextView textView_name;
    TextView textView_shichang;
    TextView textView_shijian;
    CardView cardView;
    private MyDatabaseHelper dbHelper;
    private static int i = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paihangbang);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }
        dbHelper = new MyDatabaseHelper(this,"Paiming.db",null,1);
        dbHelper.getWritableDatabase();//创建数据库
          getxinxi();//获取传过来的信息并且插入数据库
          getData();//从数据库中取出数据并初始化控件显示
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**获取传过来的信息**/
    public void getxinxi(){
            Intent intent = getIntent();
            if(intent.getAction()=="com.fromMain"){
                name = intent.getStringExtra("name");
                //Toast.makeText(context,name,Toast.LENGTH_SHORT).show();
                shichang = intent.getStringExtra("shichang");
                //Toast.makeText(context,shichang,Toast.LENGTH_SHORT).show();
                shijian = intent.getExtras().get("dangqianshijian").toString();
                insertData(name,Integer.parseInt(shichang),shijian);
            }

    }

    /**获取显示控件的实例**/
    public void initxinxi(int id_,int name_id,int shichang_id,int shijian_id,int card_id){
        textView_id = (TextView)findViewById(id_);
        textView_name = (TextView)findViewById(name_id);
        textView_shichang = (TextView)findViewById(shichang_id);
        textView_shijian = (TextView)findViewById(shijian_id);
        cardView = (CardView)findViewById(card_id);

    }

    /**设置信息显示**/
    public void setxinxi(String str1,String str2,String str3,String str4){
        textView_id.setText(str1);
        textView_name.setText(str2);
        textView_shichang.setText(str3);
        textView_shijian.setText(str4);
        cardView.setVisibility(View.VISIBLE);
    }

    /**在数据库插入数据**/
    public void insertData(String name,int shichang,String shijian){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name",name);
        contentValues.put("ctime",shichang);
        contentValues.put("date",shijian);
        db.insert("Paihang",null,contentValues);
        contentValues.clear();
    }

    /**按用时长短取出数据**/
    public void getData(){
       SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("Paihang",null,null,null,null,null,"ctime");
        if(cursor.moveToFirst()){
            do{
                i++;
                initxinxi(ids[i],names[i],shichangs[i],shijians[i],cards[i]);
                String name = cursor.getString(cursor.getColumnIndex("name"));
                int ctime = cursor.getInt(cursor.getColumnIndex("ctime"));
                String date = cursor.getString(cursor.getColumnIndex("date"));
                setxinxi(i+1+"",name,ctime+"s",date);
            }while (i<10&&cursor.moveToNext());
        }
        cursor.close();
        i=-1;
    }
}
