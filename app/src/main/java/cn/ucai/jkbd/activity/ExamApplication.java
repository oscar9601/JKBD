package cn.ucai.jkbd.activity;

import android.app.Application;
import android.util.Log;

import com.example.administrator.jkbd.bean.Question;
import com.example.administrator.jkbd.bean.all;
import com.example.administrator.jkbd.bean.item;
import com.example.administrator.jkbd.utils.OkHttpUtils;
import com.example.administrator.jkbd.utils.ResultUtils;

import java.util.List;

import static com.example.administrator.jkbd.utils.ResultUtils.getListResultFromJson;

public class ExamApplication  extends Application{
    public static String LOAD_EXAM_INFO="load_exam_info";
    public static String LOAD_EXAM_QUESTION="load_exam_questions";
    public static String LOAD_DATA_SUCCESS="load_data_success";
    information in;
    private static ExamApplication instance;
    List<Question> QuestionList;
    IExamBiz biz;

    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
    }

    public information getIn() {
        return in;
    }

    public void setIn(information in) {
        this.in = in;
    }

    public List<Question> getQuestionList() {
        return QuestionList;
    }

    public void setInformationList(List<Question> QuestionList) {
        this.QuestionList = QuestionList;
    }
    public  static  ExamApplication getInsance(){
        return  instance;
    }

}
