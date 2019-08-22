package com.example.hp.lianliankan;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

public class Fragment_shezhi  extends Fragment {
    public Switch aSwitch;
    public Switch aSwitch_yinxiao;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;



    public Fragment_shezhi() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_3,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        aSwitch = (Switch)getActivity().findViewById(R.id.beijingkaiguan);
        aSwitch_yinxiao = (Switch)getActivity().findViewById(R.id.yinxiaokaiguan);
        pref = this.getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);


        getData();
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                        ((MainActivity) (getActivity())).mediaPlayer.start();
                        ((MainActivity) (getActivity())).beijing1=true;
                }
                else{
                    ((MainActivity)(getActivity())).mediaPlayer.pause();
                    ((MainActivity) (getActivity())).beijing1=false;
                }
            }
        });
        aSwitch_yinxiao.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                   ((Fragment_youxi)getActivity().getSupportFragmentManager().findFragmentByTag("page1")).yinxiao=true;
                }
                else{
                    ((Fragment_youxi)getActivity().getSupportFragmentManager().findFragmentByTag("page1")).yinxiao=false;
                }
            }
        });

    }

    public void saveData(){
        editor = pref.edit();
        editor.putBoolean("beijingkaiguan",aSwitch.isChecked());
        editor.putBoolean("yinxiaokaiguan",aSwitch_yinxiao.isChecked());
        editor.apply();
    }

    public void getData(){
        boolean isRemember_beijing = pref.getBoolean("beijingkaiguan",true);
        boolean isRemember_yinxiao = pref.getBoolean("yinxiaokaiguan",true);
        aSwitch.setChecked(isRemember_beijing);
        aSwitch_yinxiao.setChecked(isRemember_yinxiao);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        saveData();
    }
}
