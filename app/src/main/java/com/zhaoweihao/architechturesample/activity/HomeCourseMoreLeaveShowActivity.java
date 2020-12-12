package com.zhaoweihao.architechturesample.activity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.base.BaseActivity;
import com.zhaoweihao.architechturesample.bean.Leave;
import com.zhaoweihao.architechturesample.bean.RestResponse;
import com.zhaoweihao.architechturesample.database.User;
import com.zhaoweihao.architechturesample.util.Constant;

import org.litepal.crud.DataSupport;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.zhaoweihao.architechturesample.util.HttpUtil.sendGetRequest;
import static com.zhaoweihao.architechturesample.util.HttpUtil.sendPostRequest;
import static com.zhaoweihao.architechturesample.util.Utils.log;
/**
*@description 首页-课程-详细界面-更多-请假-查看详细的请假条
*@author
*@time 2019/1/28 12:58
*/
public class HomeCourseMoreLeaveShowActivity extends BaseActivity implements View.OnClickListener {
    private static final Class thisClass = HomeCourseMoreLeaveShowActivity.class;
    private Integer[] ids = new Integer[]{R.id.tv_leave_show_content, R.id.tv_leave_show_faculty,
            R.id.tv_leave_show_classnum, R.id.tv_leave_show_name,
            R.id.tv_leave_show_stumun, R.id.tv_leave_show_state,
            R.id.tv_leave_show_comment};
    private TextView[] tvs = new TextView[ids.length];
    private Button bt_leaveshowsubmit;
    private int leaveId;
    private int tecId;
    private int status;
    private ImageView iv_leaveshowreturntohome;
    private EditText et_leave_show_message;
    private RadioGroup rg_show_leave_rBtnGrp;
    private RadioButton rb_show_leave_RBtn1, rb_show_leave_RBtn2;
    private int positionmode;
    private Leave leave;

    private RelativeLayout relativeLayout;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_show);
        initView();

        //获取请假信息
        User user3 = DataSupport.findLast(User.class);
        Intent intent = getIntent();
//        Bundle bundle = intent.getExtras();
        leave = (Leave) intent.getSerializableExtra("leave");

        if (user3.getStudentId() == null && !(user3.getTeacherId() == null)) {
            positionmode = 2;//代表教师
            submit();
        } else if (!(user3.getStudentId() == null) && user3.getTeacherId() == null) {
            positionmode = 1;//代表学生
            submit();
        }
    }

    public void initView() {

        relativeLayout = findViewById(R.id.ry_leaveshowmain);
        swipeRefreshLayout = findViewById(R.id.refresh_layout);

        swipeRefreshLayout.setEnabled(true);
        swipeRefreshLayout.setRefreshing(true);

        for (int i = 0; i < ids.length; i++) {
            tvs[i] = findViewById(ids[i]);
        }
        iv_leaveshowreturntohome = findViewById(R.id.iv_leaveshowreturntohome);
        et_leave_show_message = findViewById(R.id.et_leave_show_message);

        rg_show_leave_rBtnGrp = findViewById(R.id.rg_show_leave_rBtnGrp);
        rb_show_leave_RBtn1 = findViewById(R.id.rb_show_leave_RBtn1);
        rb_show_leave_RBtn2 = findViewById(R.id.rb_show_leave_RBtn2);

        rg_show_leave_rBtnGrp.setOnCheckedChangeListener(
                (radioGroup, i) -> {
                    switch (i) {
                        case R.id.rb_show_leave_RBtn1:
                            status = 3;
                            break;
                        case R.id.rb_show_leave_RBtn2:
                            status = 2;
                            break;
                    }
                }
        );


        //提交审批请假条
        bt_leaveshowsubmit = findViewById(R.id.bt_leaveshowsubmit);
        bt_leaveshowsubmit.setOnClickListener(v -> {
            if (status == 0) {
                Toast.makeText(HomeCourseMoreLeaveShowActivity.this, "请选择同意或拒绝！", Toast.LENGTH_SHORT).show();
            } else if (status == 2 || status == 3) {
                confirm();
            }

        });

        iv_leaveshowreturntohome.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_leaveshowreturntohome:
                finish();
                break;
        }
    }

    //从学生学号，获取到学生的所有请假条数组，在所有请假条中获取该学生某个请假条，num为0代表为该学生的所有的请假条中的第一条
    //mode如果为1代表是学生，为2代表是教师
    public void submit() {
        tvs[0].setText("\u3000\u3000" + "我因" + leave.getContent() + "无法按时上" +
                "课，特此向您请假，请假时间是：" + leave.getStartDate() + "第" + leave.getStartNum() + "节课" + "到"
                + leave.getEndDate() + "第" + leave.getEndNum() + "节课。"
                + "恳求您的批准！");
        tvs[3].setText(leave.getStudentId());
        leaveId = leave.getId();
        tecId = leave.getTecId();
        if (positionmode == 1) {
            if (leave.getStatus() != 1) {
                if (leave.getStatus() == 3) {
                    tvs[5].setText("教师已批准！");
                } else if (leave.getStatus() == 2) {
                    tvs[5].setText("教师已拒绝！");
                }
                et_leave_show_message.setVisibility(View.GONE);
                rg_show_leave_rBtnGrp.setVisibility(View.GONE);
                bt_leaveshowsubmit.setVisibility(View.INVISIBLE);
                if (!(leave.getTecAdvise() == null)) {
                    tvs[6].setVisibility(View.VISIBLE);
                    tvs[6].setText("教师留言：" + leave.getTecAdvise());
                }
            } else {
                et_leave_show_message.setVisibility(View.GONE);
                rg_show_leave_rBtnGrp.setVisibility(View.GONE);
                bt_leaveshowsubmit.setVisibility(View.INVISIBLE);
                tvs[5].setText("等待教师审批!");
                tvs[6].setVisibility(View.INVISIBLE);
            }
        } else if (positionmode == 2) {
            if (leave.getStatus() != 1) {
                if (leave.getStatus() == 3) {
                    tvs[5].setText("教师已批准！");
                } else if (leave.getStatus() == 2) {
                    tvs[5].setText("教师已拒绝！");
                }
                et_leave_show_message.setVisibility(View.GONE);
                rg_show_leave_rBtnGrp.setVisibility(View.GONE);
                bt_leaveshowsubmit.setVisibility(View.INVISIBLE);
                if (!(leave.getTecAdvise() == null)) {
                    tvs[6].setVisibility(View.VISIBLE);
                    tvs[6].setText("教师留言：" + leave.getTecAdvise());
                }
            } else {
                tvs[5].setText("等待教师审批!");
                tvs[6].setVisibility(View.INVISIBLE);
            }
        }
        queryStudentInformation(leave.getStuId());

    }

    //通过学生的在数据库中的ID查询学生的其他从请假表未知但需要的信息，包括院系，班级，姓名等
    public void queryStudentInformation(int stuId) {
        /**
         * @username 用户名
         * @password 密码
         * @studentId 学号
         * @teacherId 教师编号
         * @classId 班级编号
         * @department 学院
         * @education 学历 int
         * @date 入学时间
         * @school 学校
         * @sex 性别 int
         * @name 真实姓名
         */

        //发送post请求注册
        String after = Constant.QUERY_LEAVE_BY_STUDENT_ID_URL + stuId;

        sendGetRequest(after, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //发送请求失败，有可能是网络断了或者其他的
                //Toast.makeText(MainRegisterActivity.this,"发送请求失败，请检查网络！", Toast.LENGTH_SHORT).show();
                log(thisClass, "发送请求失败，请检查网络");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                //Gson解析数据 json -> 对象
                try {
                    RestResponse restResponse = new Gson().fromJson(body, RestResponse.class);
                    log(thisClass, restResponse.getCode().toString());
                    //状态码500表示失败，打印错误信息
                    if (restResponse.getCode() == Constant.FAIL_ORIGIN_CODE) {
                        log(thisClass, restResponse.getMsg());
                    }
                    //200代表成功，打印成功信息
                    if (restResponse.getCode() == Constant.SUCCESS_CODE) {
                        log(thisClass, "已成功注册");
                        com.zhaoweihao.architechturesample.database.User user1 = new Gson().fromJson(restResponse.getPayload().toString(), com.zhaoweihao.architechturesample.database.User.class);
                        runOnUiThread(() -> {
                            tvs[1].setText(user1.getDepartment());
                            tvs[2].setText(user1.getClassId() + "班");
                            tvs[4].setText(user1.getName());
                            // 加载完后再展示
                            swipeRefreshLayout.setRefreshing(false);
                            swipeRefreshLayout.setEnabled(false);
                            relativeLayout.setVisibility(View.VISIBLE);
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                log(thisClass, body);
            }
        });
    }

    public void confirm() {

        String suffix = Constant.CONFIRM_LEAVE_URL;
        // 组装confirm对象
        com.zhaoweihao.architechturesample.bean.LeaveShow leaveShow1 = new com.zhaoweihao.architechturesample.bean.LeaveShow();
        leaveShow1.setLeaveId(leaveId);
        leaveShow1.setTecId(tecId);
        leaveShow1.setStatus(status);
        leaveShow1.setTecAdvise(et_leave_show_message.getText().toString());

        //转换为json
        String json = new Gson().toJson(leaveShow1);


        sendPostRequest(suffix, json, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //网络错误处理
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                //解析json数据组装RestResponse对象
                RestResponse restResponse = new Gson().fromJson(body, RestResponse.class);
                if (restResponse.getCode() == Constant.FAIL_ORIGIN_CODE) {
                    log(thisClass, "审批失败");
                }
                // code 200等于登录成功
                if (restResponse.getCode() == Constant.SUCCESS_CODE) {
                    //切换主进程更新UI
                    runOnUiThread(() -> {
                        Toast.makeText(HomeCourseMoreLeaveShowActivity.this, "已经审批成功！", Toast.LENGTH_SHORT).show();
                        et_leave_show_message.setVisibility(View.GONE);
                        rg_show_leave_rBtnGrp.setVisibility(View.GONE);
                        bt_leaveshowsubmit.setVisibility(View.INVISIBLE);
                        if (status == 3) {
                            tvs[5].setText("教师已批准！");
                        } else if (status == 2) {
                            tvs[5].setText("教师已拒绝！");
                        }
                        if (!et_leave_show_message.getText().toString().equals("")) {
                            tvs[6].setText("教师留言：" + et_leave_show_message.getText().toString());
                        }

                    });
                    //首先获取payload (Object) , toString()转换成json
                    //接着用gson将json组装起来
                    log(thisClass, "成功审批");
                }
            }
        });

    }
}
