package com.zhaoweihao.architechturesample.activity;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.activity.example.ValidationMesgService;
import com.zhaoweihao.architechturesample.base.BaseActivity;
import com.zhaoweihao.architechturesample.bean.ValidationMesgEvent;
import com.zhaoweihao.architechturesample.database.Memorandum;
import com.zhaoweihao.architechturesample.database.User;
import com.zhaoweihao.architechturesample.fragment.MessageFragment;
import com.zhaoweihao.architechturesample.ui.BottomNavigationViewHelper;
import com.zhaoweihao.architechturesample.fragment.HomeFragment;
import com.zhaoweihao.architechturesample.fragment.PersonalFragment;
import com.zhaoweihao.architechturesample.fragment.NoteFragment;
import com.zhaoweihao.architechturesample.ui.DotView;
//import com.zhaoweihao.architechturesample.timeline.HomeFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author
 * @description 主页（包括首页，消息，笔记，个人）
 * @time 2019/1/28 14:28
 */
public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    private static final String KEY_BOTTOM_NAVIGATION_VIEW_SELECTED_ID = "KEY_BOTTOM_NAVIGATION_VIEW_SELECTED_ID";

    private HomeFragment mHomeFragment;
    private PersonalFragment mPersonalFragment;
    private MessageFragment mMessageFragment;
    private NoteFragment mNoteFragment;
    private DotView[] dotViews;
    private  DotView dotView;

    private BottomNavigationView mBottomNavigationView;
    private Handler mainHandler = new Handler(Looper.getMainLooper());
    private int expireMemorandum = 0;
    private int validationNum = 0;
    private int expireMemorandumNotifiedNum = 0;
    List<Memorandum> memorandumExpire = new ArrayList<>();
    private Handler handler;
    private Timer timer;
    private TimerTask timerTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dotView = new DotView(MainActivity.this, null);
        startService(new Intent(this, ValidationMesgService.class));
        initViews();
        EventBus.getDefault().register(this);

        initFragments(savedInstanceState);
        detectExpiredMemorandum();

        if (savedInstanceState != null) {
            int id = savedInstanceState.getInt(KEY_BOTTOM_NAVIGATION_VIEW_SELECTED_ID, R.id.nav_home);
            switch (id) {
                case R.id.nav_home:
                    showFragment(mHomeFragment);
                    break;
                case R.id.nav_message:
                    showFragment(mMessageFragment);
                    break;
                case R.id.nav_note:
                    showFragment(mNoteFragment);
                    break;
                case R.id.nav_info:
                    showFragment(mPersonalFragment);
                    break;
            }
        } else {
            showFragment(mHomeFragment);
        }

        mBottomNavigationView.setOnNavigationItemSelectedListener((menuItem -> {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            FragmentManager fm = getSupportFragmentManager();
            switch (menuItem.getItemId()) {
                case R.id.nav_home:
                    showFragment(mHomeFragment);
                    break;
                case R.id.nav_message:
                    showFragment(mMessageFragment);
                    break;
                case R.id.nav_note:
                    showFragment(mNoteFragment);
                    break;
                case R.id.nav_info:
                    showFragment(mPersonalFragment);
                    break;

                default:
                    break;
            }
            ft.commit();
            return true;
        }));


    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
        handler.removeMessages(11);
        handler.removeMessages(12);
    }

    private void initViews() {
        mBottomNavigationView = findViewById(R.id.bottom_nav);
        BottomNavigationViewHelper.disableShiftMode(mBottomNavigationView);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_BOTTOM_NAVIGATION_VIEW_SELECTED_ID, mBottomNavigationView.getSelectedItemId());
        FragmentManager fm = getSupportFragmentManager();
        if (mHomeFragment.isAdded()) {
            fm.putFragment(outState, HomeFragment.class.getSimpleName(), mHomeFragment);
        }
        if (mMessageFragment.isAdded()) {
            fm.putFragment(outState, MessageFragment.class.getSimpleName(), mMessageFragment);
        }
        if (mNoteFragment.isAdded()) {
            fm.putFragment(outState, MessageFragment.class.getSimpleName(), mNoteFragment);
        }
        if (mPersonalFragment.isAdded()) {
            fm.putFragment(outState, PersonalFragment.class.getSimpleName(), mPersonalFragment);
        }
    }

    private void initFragments(Bundle savedInstanceState) {
        FragmentManager fm = getSupportFragmentManager();
        if (savedInstanceState == null) {
            mHomeFragment = HomeFragment.newInstance();
            mPersonalFragment = PersonalFragment.newInstance();
            mNoteFragment = NoteFragment.newInstance();
            mMessageFragment = MessageFragment.newInstance();
        } else {
            mHomeFragment = (HomeFragment) fm.getFragment(savedInstanceState, HomeFragment.class.getSimpleName());
            mMessageFragment = (MessageFragment) fm.getFragment(savedInstanceState, MessageFragment.class.getSimpleName());
            mPersonalFragment = (PersonalFragment) fm.getFragment(savedInstanceState, PersonalFragment.class.getSimpleName());
            mNoteFragment = (NoteFragment) fm.getFragment(savedInstanceState, NoteFragment.class.getSimpleName());
        }
        if(savedInstanceState==null){
            if (!mHomeFragment.isAdded()) {
                fm.beginTransaction()
                        .add(R.id.container, mHomeFragment, HomeFragment.class.getSimpleName())
                        .commit();
            }
        }


        if (!mMessageFragment.isAdded()) {
            fm.beginTransaction()
                    .add(R.id.container, mMessageFragment, MessageFragment.class.getSimpleName())
                    .commit();
        }
        if (!mNoteFragment.isAdded()) {
            fm.beginTransaction()
                    .add(R.id.container, mNoteFragment, NoteFragment.class.getSimpleName())
                    .commit();
        }

        if (!mPersonalFragment.isAdded()) {
            fm.beginTransaction()
                    .add(R.id.container, mPersonalFragment, PersonalFragment.class.getSimpleName())
                    .commit();
        }
    }

    private void showFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        if (fragment instanceof HomeFragment) {
            fm.beginTransaction()
                    .show(mHomeFragment)
                    .hide(mPersonalFragment)
                    .hide(mNoteFragment)
                    .hide(mMessageFragment)
                    .commit();
        } else if (fragment instanceof PersonalFragment) {
            fm.beginTransaction()
                    .show(mPersonalFragment)
                    .hide(mHomeFragment)
                    .hide(mNoteFragment)
                    .hide(mMessageFragment)
                    .commit();
        } else if (fragment instanceof NoteFragment) {
            fm.beginTransaction()
                    .show(mNoteFragment)
                    .hide(mPersonalFragment)
                    .hide(mHomeFragment)
                    .hide(mMessageFragment)
                    .commit();
        } else if (fragment instanceof MessageFragment) {
            fm.beginTransaction()
                    .show(mMessageFragment)
                    .hide(mHomeFragment)
                    .hide(mNoteFragment)
                    .hide(mPersonalFragment)
                    .commit();
        }
    }
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onMessageEvent(ValidationMesgEvent event) {

        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                validationNum = event.getMesgNum();
                initUnReadMessageViews();
            }

        });
    }

    private void detectExpiredMemorandum() {

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 11) {
                    detectedExpired(memorandumExpire);
                }
                if (msg.what == 12) {
                    initUnReadMessageViews();
                }
                super.handleMessage(msg);
            }
        };
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                List<Memorandum> memorandumTemp = new ArrayList<>();
                memorandumTemp.clear();
                memorandumExpire.clear();
                int expireMemoNotifiedNow = expireMemorandumNotifiedNum;
                int expireNotifiedTemp = 0;
                memorandumTemp.addAll(DataSupport.where("UserId=?", "" + DataSupport.findLast(User.class).getUserId()).find(Memorandum.class));
                for (Memorandum md : memorandumTemp) {
                    if (!md.getNotify() && ifIsExPire(md.getDate().getTime())) {
                        memorandumExpire.add(md);
                    }
                    if (md.getNotify() && ifIsExPire(md.getDate().getTime())) {
                        expireNotifiedTemp++;
                    }
                }

                if (memorandumExpire.size() > 0) {
                    message.what = 11;
                } else {
                    if (expireNotifiedTemp != expireMemoNotifiedNow) {
                        Log.v("tanxinkuihhh", "expire");
                        expireMemorandumNotifiedNum = expireNotifiedTemp;
                        message.what = 12;
                    }
                }
//                Log.v("tanxinkuihhh", expireMemoNotifiedNow + "en:" + expireNotifiedTemp + "kk:" + expireMemorandumNotifiedNum);
                handler.sendMessage(message);
            }
        };
        timer.schedule(timerTask, 200, 200);
    }

    private Boolean ifIsExPire(long endTime) {
        Date date = new Date();
        long longExpend = endTime - date.getTime();
        if (longExpend < 0) {
            return true;
        } else {
            return false;
        }
    }

    private void detectedExpired(List<Memorandum> dectExpire) {
        expireMemorandum = dectExpire.size();
        initUnReadMessageViews();
        for (Memorandum md : dectExpire) {
            Memorandum memorandumt = new Memorandum();
            int userId = DataSupport.findLast(User.class).getUserId();
            memorandumt.setUserId(userId);
            memorandumt.setTitle(md.getTitle());
            memorandumt.setTag(md.getTag());
            memorandumt.setContent(md.getContent());
            memorandumt.setDate(md.getDate());
            memorandumt.setNotify(true);
            memorandumt.update(md.getId());
            sendSimplestNotificationWithAction(md.getTitle(), md.getId() + 50);
            Log.v("tanxinkuiExpired", md.toString());
        }
    }

    private void sendSimplestNotificationWithAction(String s, int id) {
        NotificationManager mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //获取PendingIntent
        Intent mainIntent = new Intent(this, MessageMemorandumActivity.class);
        PendingIntent mainPendingIntent = PendingIntent.getActivity(this, id, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //创建 Notification.Builder 对象
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.countdown)
                //点击通知后自动清除
                .setAutoCancel(true)
                .setContentTitle("待办事项")
                .setContentText(s)
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                .setContentIntent(mainPendingIntent);
        //发送通知
        mNotifyManager.notify(id, builder.build());
    }

    private void initUnReadMessageViews() {
        Log.v("tanxred", "youhanbeeen11");
        //初始化红点view
        BottomNavigationMenuView menuView = null;
        for (int i = 0; i < mBottomNavigationView.getChildCount(); i++) {
            View child = mBottomNavigationView.getChildAt(i);
            if (child instanceof BottomNavigationMenuView) {
                menuView = (BottomNavigationMenuView) child;
                break;
            }
        }
        if (menuView != null) {
            int dp8 = getResources().getDimensionPixelSize(R.dimen.space_8);
            dotViews = new DotView[menuView.getChildCount()];
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView.LayoutParams params = new BottomNavigationItemView.LayoutParams(i == menuView.getChildCount() - 1 ? dp8 : dp8 * 2, 0);
                params.gravity = Gravity.CENTER_HORIZONTAL;
                params.leftMargin = dp8 * 3;
                params.topMargin = dp8 / 2;
                RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params2.leftMargin = 170;
                BottomNavigationItemView itemView = (BottomNavigationItemView) menuView.getChildAt(i);

                // dotView.setBackgroundColor(Color.RED);
                int finaltotal = validationNum + expireMemorandumNotifiedNum;
                if (finaltotal >= 0) {
                    try {
                        itemView.removeView(dotView);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (i == 1) {
                        itemView.addView(dotView, params2);
                    }
                    if (i < menuView.getChildCount() - 1) {
                        Log.v("tanxinkuihhh", "expire:" + expireMemorandumNotifiedNum);
                        Log.v("tanxinkuihhh", "expire:" + finaltotal);
                        dotView.setUnReadCount(finaltotal);
                    }
                    dotViews[i] = dotView;
                }
            }
        }
    }
}
