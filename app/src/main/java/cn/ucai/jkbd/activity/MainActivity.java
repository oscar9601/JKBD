package cn.ucai.jkbd.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import cn.ucai.jkbd.R;
import cn.ucai.jkbd.bean.information;
import cn.ucai.jkbd.utils.OkHttpUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

}
    @Override
    public void onBackPressed() {
        Intent intent=new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }
    public void test(View view){
        OkHttpUtils<information>utils = new OkHttpUtils<>(getApplicationContext());
         String url="http://101.251.196.90:8080/JztkServer/examInfo";
        utils.url(url)
                .targetClass(information.class)
                .execute(new OkHttpUtils.OnCompleteListener<information>() {
                    @Override
                    public void onSuccess(information result) {
                        Log.e("main","result="+result);

                    }

                    @Override
                    public void onError(String error) {
                        Log.e("main","error"+error);
                    }
                })
        startActivity(new Intent(MainActivity.this,ExamActivity.class));
    }
    public void exit(View view){
        finish();
    }
}
