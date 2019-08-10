package com.qljy.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.qljy.myapp.handler.ClickProxyHandler;
import com.qljy.myapp.listener.MyClickerListeneer;

import java.lang.reflect.Proxy;

public class MainActivity extends AppCompatActivity {
    private TextView textView;
    private View.OnClickListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView=findViewById(R.id.text_view);
        listener= view -> {
            try {
                PackageInfo packageInfo=getPackageManager().getPackageInfo(getPackageName(),0) ;
                Log.e("TAG", packageInfo.versionName);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        };
        textView.setOnClickListener(listener);
        //textView.setOnClickListener(generateListener());

    }

    private View.OnClickListener generateListener() {
        return (View.OnClickListener) Proxy.newProxyInstance(getClassLoader(),new Class[]{View.OnClickListener.class},new ClickProxyHandler(new MyClickerListeneer()));
    }
}
