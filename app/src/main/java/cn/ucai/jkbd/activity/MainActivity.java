package cn.ucai.jkbd.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import cn.ucai.jkbd.R;

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
        startActivity(new Intent(MainActivity.this,ExamActivity.class));
    }
    public void exit(View view){
        finish();
    }
}
