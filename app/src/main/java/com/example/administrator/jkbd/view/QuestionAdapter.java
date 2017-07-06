package com.example.administrator.jkbd.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.jkbd.ExamApplication;
import com.example.administrator.jkbd.R;
import com.example.administrator.jkbd.bean.Question;

import java.util.List;

/**
 * Created by Administrator on 2017/7/5 0005.
 */

public class QuestionAdapter extends BaseAdapter{
   Context mContext;
    List<Question> examList;

    public QuestionAdapter(Context mContext) {
        this.mContext = mContext;
        examList= ExamApplication.getInstance().getmQuestionList();
    }

    @Override
    public int getCount() {
        return examList==null?0:examList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=View.inflate(mContext, R.layout.item_question,null );
        TextView tvNo =(TextView)view.findViewById(R.id.tv_no);
        ImageView ivQuestion = (ImageView) view.findViewById(R.id.iv_question);
        String ua =examList.get(position).getUserAnswer();
        String ra=examList.get(position).getAnswer();
        if(ua!=null&&!ua.equals("")){

            ivQuestion.setImageResource(ua.equals(ra)
                    ?R.mipmap.answer24x24
            :R.mipmap.err);

        }else{
            ivQuestion.setImageResource(R.mipmap.qz);
        }

        tvNo.setText("第"+(position)+"题");
        return view;
    }
}
