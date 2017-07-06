package com.example.administrator.jkbd.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.administrator.jkbd.ExamApplication;
import com.example.administrator.jkbd.R;
import com.example.administrator.jkbd.bean.Question;
import com.example.administrator.jkbd.bean.item;
import com.example.administrator.jkbd.biz.ExamBiz;
import com.example.administrator.jkbd.biz.IExamBiz;
import com.example.administrator.jkbd.view.QuestionAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.zip.CheckedInputStream;

import static android.R.attr.alertDialogIcon;
import static android.R.attr.button;
import static android.media.CamcorderProfile.get;
import static java.lang.System.load;

/**
 * nox_adb.exe connect 127.0.0.1:62001
 */

public class ExamActivity extends AppCompatActivity {
    TextView tvExamInfo,tvTime,tvExamTitle,tv0p1,tv0p2,tv0p3,tv0p4,tvLoad,tvNo;
    CheckBox cb01,cb02,cb03,cb04;
    CheckBox[] cbs=new CheckBox[4];
    LinearLayout layoutLoading,layout03,layout04;
    ImageView mImageView;
    ProgressBar dialog;
    Gallery mGallery;
    IExamBiz biz;
    QuestionAdapter mAdapter;
    boolean isLoadExamInfo=false;
    boolean isLoadQuestions=false;

    boolean isLoadExamInfoReceiver=false;
    boolean isLoadQuestionsReceiver=false;

    LoadExamBroadcast  mLoadExamBroadcast;
    LoadQuestionBroadcast mLoadQuestionBroadcast;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);
        mLoadExamBroadcast=new LoadExamBroadcast();
        mLoadQuestionBroadcast= new LoadQuestionBroadcast();
        setListener();
        biz=new ExamBiz();
        initView();
        loadData();
    }

    private void setListener() {
        registerReceiver(mLoadExamBroadcast,new IntentFilter(ExamApplication.LOAD_EXAM_INFO));
        registerReceiver(mLoadQuestionBroadcast,new IntentFilter(ExamApplication.LOAD_EXAM_QUESTION));
    }

    private void loadData() {
        layoutLoading.setEnabled(false);
        dialog.setVisibility(View.VISIBLE);
        tvLoad.setText("下载数据...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                biz.beginExam();
            }
        }).start();
    }

    private void initView() {
        layoutLoading= (LinearLayout) findViewById(R.id.layout_loading);
        layout03= (LinearLayout) findViewById(R.id.layout_03);
        layout04= (LinearLayout) findViewById(R.id.layout_04);

        dialog= (ProgressBar) findViewById(R.id.load_dialog);
        tvExamInfo = (TextView) findViewById(R.id.tv_examinfo);
        tvExamTitle = (TextView) findViewById(R.id.tv_exam_title);
        tvNo= (TextView) findViewById(R.id.tv_exam_no);

        mGallery=(Gallery) findViewById(R.id.gallery);
        tv0p1 = (TextView) findViewById(R.id.tv_op1);
        tv0p2 = (TextView) findViewById(R.id.tv_op2);
        tv0p3 = (TextView) findViewById(R.id.tv_op3);
        tv0p4 = (TextView) findViewById(R.id.tv_op4);
        cb01= (CheckBox) findViewById(R.id.cb_01);
        cb02= (CheckBox) findViewById(R.id.cb_02);
        cb03= (CheckBox) findViewById(R.id.cb_03);
        cb04= (CheckBox) findViewById(R.id.cb_04);
        cbs[0]=cb01;
        cbs[1]=cb02;
        cbs[2]=cb03;
        cbs[3]=cb04;
        tvLoad = (TextView) findViewById(R.id.tv_load);
        mImageView = (ImageView) findViewById(R.id.im_exam_image);
        layoutLoading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
            }
        });
        cb01.setOnCheckedChangeListener(listener);
        cb02.setOnCheckedChangeListener(listener);
        cb03.setOnCheckedChangeListener(listener);
        cb04.setOnCheckedChangeListener(listener);
    }

    CompoundButton.OnCheckedChangeListener listener=new CompoundButton.OnCheckedChangeListener(){
        @Override
        public void onCheckedChanged(CompoundButton buttonView,boolean isChecked){
            if (isChecked) {
                int userAnswer = 0;
                switch (buttonView.getId()) {
                    case R.id.cb_01:
                        userAnswer = 1;
                        break;
                    case R.id.cb_02:
                        userAnswer = 2;
                        break;
                    case R.id.cb_03:
                        userAnswer = 3;
                        break;
                    case R.id.cb_04:
                        userAnswer = 4;
                        break;
                }
                Log.e("checkedChanged", "usera=" + userAnswer + ",isChecked=" + isChecked);
                if (userAnswer > 0) {
                    for (CheckBox cb : cbs) {
                        cb.setChecked(false);
                    }
                    cbs[userAnswer - 1].setChecked(true);
                }
            }
        }
    };


    private void initData() {
        if (isLoadExamInfoReceiver && isLoadQuestionsReceiver){
            if (isLoadExamInfo && isLoadQuestions){
                layoutLoading.setVisibility(View.GONE);
                item item =ExamApplication.getInstance().getMitem();
                if (item !=null){
                    showData(item);
                    initTimer(item);
                }
                initGallery();

                showExam(biz.getExam());

            }else{
                layoutLoading.setEnabled(true);
                dialog.setVisibility(View.GONE);
                tvLoad.setText("下载失败,点击重新下载。");

            }
        }

    }

    private void initGallery() {
        mAdapter=new QuestionAdapter(this);
        mGallery.setAdapter(mAdapter);
        mGallery.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?>parent,View view,int position,long id){
                saveUserAnswer();
                showExam(biz.getExam(position));
            }
                                        }
        );
    }


    private void initTimer(item itemm){
    int sumTimer = itemm.getLimitTime()*60*1000;
    final long overTime=sumTimer+System.currentTimeMillis();
    final Timer timer = new Timer();
    timer.schedule(new TimerTask() {
        @Override
        public void run() {
            long  l=overTime-System.currentTimeMillis();
            final long min =(l/1000/60);
            final long sec = (l/1000%60);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvTime.setText("剩余时间："+min+"分"+sec+"秒");
                }
            });

        }
    },0,1000);
    timer.schedule(new TimerTask() {
        @Override
        public void run() {
            timer.cancel();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    commit(null);
                }
            });
        }
    },sumTimer);
}
    private void showExam(Question question) {
        Log.e("showExam","showExam,exam="+question);
        if (question !=null){
            tvNo.setText(biz.getExamIndex());
            tvExamTitle.setText(question.getQuestion());
            tv0p1.setText(question.getItem1());
            tv0p2.setText(question.getItem2());
            tv0p3.setText(question.getItem3());
            tv0p4.setText(question.getItem4());
            tvTime=(TextView)findViewById(R.id.tv_time);

            layout03.setVisibility(question.getItem3().equals("")?View.GONE:View.VISIBLE);
            cb03.setVisibility(question.getItem3().equals("")?View.GONE:View.VISIBLE);
            layout04.setVisibility(question.getItem4().equals("")?View.GONE:View.VISIBLE);
            cb04.setVisibility(question.getItem4().equals("")?View.GONE:View.VISIBLE);


            if (question.getUrl()!=null && !question.getUrl().equals("")) {
                mImageView.setVisibility(View.VISIBLE);
                Picasso.with(ExamActivity.this)
                        .load(question.getUrl())
                        .into(mImageView);
            }else{
                mImageView.setVisibility(View.GONE);
            }
        resetOptions();
            String userAnswer = question.getUserAnswer();
            if(userAnswer!=null && !userAnswer.equals("")){
                int userCB = Integer.parseInt(userAnswer)-1;
                cbs[userCB].setChecked(true);
                setOptions(true);
        }else {
                setOptions(false);
            }
    }
    }
    private void  setOptions(boolean hasAnswer){
        for(CheckBox cb:cbs){
            cb.setEnabled(!hasAnswer);
        }
    }

     private void resetOptions(){
         for(CheckBox cb:cbs){
             cb.setChecked(false);
         }
     }

     private void saveUserAnswer(){
         for(int i=0;i<cbs.length;i++){
             if(cbs[i].isChecked()){
                 biz.getExam().setUserAnswer(String.valueOf(i+1));
                 setOptions(true);
                 mAdapter.notifyDataSetChanged();
                 return;
             }
         }
         biz.getExam().setUserAnswer("");
         mAdapter.notifyDataSetChanged();
     }

    private void showData(item item) {
        tvExamInfo.setText(item.toString());
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        if (mLoadExamBroadcast!=null){
            unregisterReceiver(mLoadExamBroadcast);

        }
        if (mLoadQuestionBroadcast!=null){
            unregisterReceiver(mLoadQuestionBroadcast);

        }
    }

    public void preExam(View view) {
        saveUserAnswer();

        showExam(biz.preQuestion());
    }

    public void nextExam(View view)
    {
        saveUserAnswer();
        showExam(biz.nextQuestion());
    }
public void commit(View view){
    saveUserAnswer();
  int s=  biz.commitExam();;
    View inflate=View.inflate(this,R.layout.layout_result,null);
    TextView tvResult=(TextView) inflate.findViewById(R.id.tv_result);
    tvResult.setText("你的分数为\n" + s + "分！");
    AlertDialog.Builder buider = new AlertDialog.Builder(this);
    buider.setIcon(R.mipmap.exam_commit32x32)
            .setTitle("交卷")
            .setView(inflate)
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
    buider.create().show();
}

    class LoadExamBroadcast extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            boolean isSuccess=intent.getBooleanExtra(ExamApplication.LOAD_DATA_SUCCESS,false);
            Log.e("LoadExamBroadcast","LoadExamBroadcast,isSuccess="+isSuccess);
            if (isSuccess){
                isLoadExamInfo=true;
            }
            isLoadExamInfoReceiver=true;
            initData();
        }
    }
    class LoadQuestionBroadcast extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            boolean isSuccess=intent.getBooleanExtra(ExamApplication.LOAD_DATA_SUCCESS,false);
            Log.e("LoadQuestionBroadcast","LoadQuestionBroadcast,isSuccess="+isSuccess);
            if (isSuccess){
                isLoadQuestions=true;
            }
            isLoadQuestionsReceiver=true;
            initData();
        }
    }
}
