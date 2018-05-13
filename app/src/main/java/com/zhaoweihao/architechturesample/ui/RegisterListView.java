package com.zhaoweihao.architechturesample.ui;

import android.app.ListActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.zhaoweihao.architechturesample.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class RegisterListView extends ListActivity implements View.OnClickListener {
    ImageView iv_title;
    Intent intent;
    EditText et_search;
    TextView tv_tip;
    int UniversityListLength;
    int checknum, checknum1, checknum2;
    String selectedUniversity,selectedFaculty,selectedYear;
    int mode;
    String filename, result,searchFlag;
    Handler handler;
    Timer timer;
    TimerTask timerTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_list_view);
        initView();
        timer.schedule(timerTask,200,200);//延时200ms，每隔200毫秒执行一次run方法
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title:
                finish();
                break;
        }
    }

    public void initView() {

        intent = getIntent();

        searchFlag="";

        Bundle bundle = intent.getExtras();

        iv_title = (ImageView) findViewById(R.id.iv_title);
        et_search = (EditText) findViewById(R.id.et_search);
        tv_tip = (TextView) findViewById(R.id.tv_tip);

        iv_title.setOnClickListener(this);
        Resources resources = this.getResources();
        switch (bundle.getString("pic")) {
            case "first":
                Drawable btnDrawable1 = resources.getDrawable(R.drawable.registerlistviewtitle);
                iv_title.setBackground(btnDrawable1);
                et_search.setVisibility(View.VISIBLE);
                mode = 1;
                checknum = bundle.getInt("checknum", -1);
                 selectedUniversity=bundle.getString("selectedUniversity");
                showList(1);
                break;
            case "second":
                Drawable btnDrawable2 = resources.getDrawable(R.drawable.registerlistviewtitle1);
                iv_title.setBackground(btnDrawable2);
                et_search.setVisibility(View.VISIBLE);
                et_search.setHint("请输入您的院系拼音前两个缩写");
                tv_tip.setVisibility(View.INVISIBLE);
                mode = 2;
                checknum1 = bundle.getInt("checknum1", -1);
                selectedFaculty=bundle.getString("selectedFaculty");
                showList(2);
                break;
            case "third":
                Drawable btnDrawable3 = resources.getDrawable(R.drawable.registerlistviewtitle2);
                iv_title.setBackground(btnDrawable3);
                et_search.setVisibility(View.GONE);
                tv_tip.setText("请选择您的入学年份");
                mode = 3;
                checknum2 = bundle.getInt("checknum2", -1);
                selectedYear=bundle.getString("selectedYear");
                showList(3);
                break;
        }


        handler= new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1){
                showSearchList(mode,searchFlag);
                }
                super.handleMessage(msg);
            }

         };
    timer= new Timer();
    timerTask = new TimerTask() {
        @Override
        public void run() {
            Message message = new Message();
            if(!et_search.getText().toString().equals("")&&!et_search.getText().toString().equals(searchFlag)){
                searchFlag=et_search.getText().toString();
                message.what =1;
            }

            handler.sendMessage(message);
        }
    };



    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        super.onListItemClick(l, v, position, id);
        l.getItemAtPosition(position);
        Intent intent1 = new Intent();
        Bundle bundle = new Bundle();
        switch (mode) {
            case 1:
                if(searchFlag.equals("")){
                    bundle.putString("backInformation", searchName(position));
                }else{
                    bundle.putString("backInformation", searchNameinSearchList(position));
                }
                bundle.putInt("checknum", position);
                break;
            case 2:
                if(searchFlag.equals("")){
                    bundle.putString("backInformation", searchName(position));
                }else{
                    bundle.putString("backInformation", searchNameinSearchList(position));
                }
                bundle.putInt("checknum1", position);
                break;
            case 3:
                if(searchFlag.equals("")){
                    bundle.putString("backInformation", searchName(position));
                }else{
                    bundle.putString("backInformation", searchNameinSearchList(position));
                }
                bundle.putInt("checknum2", position);
                break;
        }

        intent1.putExtras(bundle);
        setResult(RESULT_OK, intent1);
        finish();

    }

    public void showList(int mode) {
        switch (mode) {
            case 1:
                filename = "university.json";
                break;
            case 2:
                filename = "faculty.json";
                break;
            case 3:
                filename = "year.json";
                break;
        }
        // 关联Layout中的ListView
        ListView vncListView = (ListView) findViewById(android.R.id.list);
        // 生成动态数组，加入数据
        ArrayList<HashMap<String, Object>> remoteWindowItem = new ArrayList<HashMap<String, Object>>();

        try {
            InputStreamReader isr = new InputStreamReader(getAssets().open(filename), "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = br.readLine()) != null) {
                builder.append(line);
            }
            br.close();
            isr.close();
            JSONObject root = new JSONObject(builder.toString());
            JSONArray array = root.getJSONArray("info");
            UniversityListLength = array.length();
            for (int i = 0; i < array.length(); i++) {
                JSONObject lan = array.getJSONObject(i);

                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("name", lan.getString("name1"));
                switch (mode) {
                    case 1:
                        if (selectedUniversity.equals(lan.getString("name1"))) {
                            map.put("checkstate", R.drawable.registerlistviewframe);
                        } else {
                            map.put("checkstate", R.drawable.registerlistviewframe1);
                        }
                        break;
                    case 2:
                        if (selectedFaculty.equals(lan.getString("name1"))) {
                            map.put("checkstate", R.drawable.registerlistviewframe);
                        } else {
                            map.put("checkstate", R.drawable.registerlistviewframe1);
                        }
                        break;
                    case 3:
                        if (selectedYear.equals(lan.getString("name1"))) {
                            map.put("checkstate", R.drawable.registerlistviewframe);
                        } else {
                            map.put("checkstate", R.drawable.registerlistviewframe1);
                        }
                        break;
                }
                remoteWindowItem.add(map);

               /* String str2 = ed_username.getText().toString();
               if (str1.equals(str2)) {
                    tv_school.setText(lan.getString("sex"));
                    break;
                } else {
                    tv_school.setText("查不到相关数据");
                }*/
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        // 生成适配器的Item和动态数组对应的元素
        SimpleAdapter listItemAdapter = new SimpleAdapter(
                this,
                remoteWindowItem,//数据源
                R.layout.activity_register_list_view_frame,//ListItem的XML实现
                new String[]{"name", "checkstate"},
                new int[]{R.id.tv_name, R.id.iv_checkstate}
        );

        vncListView.setAdapter(listItemAdapter);
    }

    public void showSearchList(int mode,String searchFlag1) {
        switch (mode) {
            case 1:
                filename = "university.json";
                break;
            case 2:
                filename = "faculty.json";
                break;
            case 3:
                filename = "year.json";
                break;
        }
        // 关联Layout中的ListView
        ListView vncListView = (ListView) findViewById(android.R.id.list);
        // 生成动态数组，加入数据
        ArrayList<HashMap<String, Object>> remoteWindowItem = new ArrayList<HashMap<String, Object>>();

        try {
            InputStreamReader isr = new InputStreamReader(getAssets().open(filename), "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = br.readLine()) != null) {
                builder.append(line);
            }
            br.close();
            isr.close();
            JSONObject root = new JSONObject(builder.toString());
            JSONArray array = root.getJSONArray("info");
            UniversityListLength = array.length();
            for (int i = 0; i < array.length(); i++) {
                JSONObject lan = array.getJSONObject(i);
                int countSearchListLength=0;
                if(searchFlag1.equals(lan.getString("name"))){
                    countSearchListLength++;
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("name", lan.getString("name1"));
                    switch (mode) {
                        case 1:
                            if (selectedUniversity.equals(lan.getString("name1"))) {
                                map.put("checkstate", R.drawable.registerlistviewframe);
                            } else {
                                map.put("checkstate", R.drawable.registerlistviewframe1);
                            }
                            break;
                        case 2:
                            if (selectedFaculty.equals(lan.getString("name1"))) {
                                map.put("checkstate", R.drawable.registerlistviewframe);
                            } else {
                                map.put("checkstate", R.drawable.registerlistviewframe1);
                            }
                            break;
                        case 3:
                            if (selectedYear.equals(lan.getString("name1"))) {
                                map.put("checkstate", R.drawable.registerlistviewframe);
                            } else {
                                map.put("checkstate", R.drawable.registerlistviewframe1);
                            }
                            break;
                    }
                    remoteWindowItem.add(map);
                }
                UniversityListLength=countSearchListLength;



               /* String str2 = ed_username.getText().toString();
               if (str1.equals(str2)) {
                    tv_school.setText(lan.getString("sex"));
                    break;
                } else {
                    tv_school.setText("查不到相关数据");
                }*/
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        // 生成适配器的Item和动态数组对应的元素
        SimpleAdapter listItemAdapter = new SimpleAdapter(
                this,
                remoteWindowItem,//数据源
                R.layout.activity_register_list_view_frame,//ListItem的XML实现
                new String[]{"name", "checkstate"},
                new int[]{R.id.tv_name, R.id.iv_checkstate}
        );

        vncListView.setAdapter(listItemAdapter);
    }

    public String searchName(int position) {
        try {
            switch (mode) {
                case 1:
                    filename = "university.json";
                    break;
                case 2:
                    filename = "faculty.json";
                    break;
                case 3:
                    filename = "year.json";
                    break;
            }
            InputStreamReader isr = new InputStreamReader(getAssets().open(filename), "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = br.readLine()) != null) {
                builder.append(line);
            }
            br.close();
            isr.close();
            JSONObject root = new JSONObject(builder.toString());
            JSONArray array = root.getJSONArray("info");
            UniversityListLength = array.length();
            for (int i = 0; i < array.length(); i++) {
                JSONObject lan = array.getJSONObject(i);

                if (position == i) {
                    result = lan.getString("name1");
                }

            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String searchNameinSearchList(int position) {
        try {
            switch (mode) {
                case 1:
                    filename = "university.json";
                    break;
                case 2:
                    filename = "faculty.json";
                    break;
                case 3:
                    filename = "year.json";
                    break;
            }
            InputStreamReader isr = new InputStreamReader(getAssets().open(filename), "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = br.readLine()) != null) {
                builder.append(line);
            }
            br.close();
            isr.close();
            JSONObject root = new JSONObject(builder.toString());
            JSONArray array = root.getJSONArray("info");
            int count=0;
            for (int i = 0; i <  array.length(); i++) {
                JSONObject lan = array.getJSONObject(i);

                if(lan.getString("name").equals(searchFlag)){
                    if (position==count++) {
                        result = lan.getString("name1");
                    }
                }
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

}
