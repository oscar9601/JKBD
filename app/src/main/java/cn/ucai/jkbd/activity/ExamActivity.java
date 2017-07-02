package cn.ucai.jkbd.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import cn.ucai.jkbd.R;

/**
 * Created by Administrator on 2017/6/29 0029.
 */



public class ExamActivity extends AppCompatActivity {
    TextView tvExamInfo,tvExamTitle,tv0p1,tv0p2,tv0p3,tv0p4;
    ImageView mImageView;
    IExamBiz biz;
    boolean isLoadExamInfo=false;
    boolean isLoadQuestions=false;

    LoadExamBroadcast mLoadExamBroadcast;
    LoadQuestiobnBroadcast mLoadQuestiobnBroadcast;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);
        mLoadExamBroadcast=new LoadExamBroadcast();
        mLoadQuestiobnBroadcast=new LoadQuestiobnBroadcast();
        setLIstener();
        initView();
        loadData();
    }

    private void setLIstener() {
        registerReceiver(mLoadExamBroadcast,new IntentFilter(ExamApplication.LOAD_EXAM_INFO));
        registerReceiver(mLoadQuestiobnBroadcast,new IntentFilter(ExamApplication.LOAD_EXAM_QUESTION));
        mImageView=(ImageView)findViewById(R.id.tv_image);
    }

    private void loadData() {
        biz=new ExamBiz();
        new Thread(new Runnable() {
            @Override
            public void run() {

                biz.beginExam();
            }
        }).start();
    }

    private void initView() {
        tvExamInfo = (TextView) findViewById(R.id.tv_exam);
        tvExamTitle = (TextView) findViewById(R.id.tv_title);
        tv0p1 = (TextView) findViewById(R.id.tv_op1);
        tv0p2 = (TextView) findViewById(R.id.tv_op2);
        tv0p3 = (TextView) findViewById(R.id.tv_op3);
        tv0p4 = (TextView) findViewById(R.id.tv_op4);
        mImageView=(ImageView)findViewById(R.id.tv_image);
    }

    private void initData() {
        if (isLoadExamInfo && isLoadQuestions) {
            information examInfo = ExamApplication.getInsance().getIn();
            if (examInfo != null) {
                showData(examInfo);
            }
            List<Question> questionList = ExamApplication.getInsance().getQuestionList();
            if (questionList != null) {
                showExam(questionList);
            }
        }
    }
    private void showExam(List<Question> questionList) {
        Question exam = questionList.get(0);
        if (exam!=null){
            tvExamTitle.setText(exam.getQuestion());
            tv0p1.setText(exam.getItem1());
            tv0p2.setText(exam.getItem2());
            tv0p3.setText(exam.getItem3());
            tv0p4.setText(exam.getItem4());
            Picasso.with(ExamActivity.this)
                    .load(exam.getUrl())
                    .into(mImageView);

        }
    }

    private void showData(information examInfo) {
        tvExamInfo.setText(examInfo.toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mLoadExamBroadcast!=null){
            unregisterReceiver(mLoadExamBroadcast);
        }
        if(mLoadQuestiobnBroadcast!=null){
            unregisterReceiver(mLoadQuestiobnBroadcast);
        }
    }

    class LoadExamBroadcast extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            boolean isSuccess=intent.getBooleanExtra(ExamApplication.LOAD_DATA_SUCCESS,false);
            Log.e("mLoadExamBroadcast","mLoadExamBroadcast,isSuccess="+isSuccess);
            if(isSuccess){
                isLoadExamInfo=true;
            }
            initData();
        }
    }

    class LoadQuestiobnBroadcast extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            boolean isSuccess=intent.getBooleanExtra(ExamApplication.LOAD_DATA_SUCCESS,false);
            Log.e("mLoadQuestiobnBroadcast","mLoadQuestiobnBroadcast,isSuccess="+isSuccess);
            if(isSuccess){
                isLoadQuestions=true;
            }
            initData();
        }
    }

}

