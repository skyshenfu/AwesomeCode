package com.qljy.myapp.handler;

import android.view.View;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ClickProxyHandler implements InvocationHandler {
    private  View.OnClickListener mListener;

    //构造函数，传入需要代理的对象
    public ClickProxyHandler(View.OnClickListener listener) {
        this.mListener=listener;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        return method.invoke(mListener,objects);
    }
}
