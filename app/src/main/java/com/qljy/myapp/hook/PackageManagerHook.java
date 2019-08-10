package com.qljy.myapp.hook;

import android.content.Context;
import android.content.pm.PackageInfo;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 使用反射和代理控制PackageManager
 */
public class PackageManagerHook {
    public static  void hook(final Context context){
        try {
            //获取ActivityThread的类
            Class<?> activityThreadClz=Class.forName("android.app.ActivityThread");
            //获取ActivityThread类中currentActivityThread的方法名
            Method currentActivityThread=activityThreadClz.getMethod("currentActivityThread");
            //ActivityThread类中currentActivityThread的方法是个静态方法所以不需要invoke到Object上
            Object activityThread=currentActivityThread.invoke(null);

            //这里获取的方法就是提供外部调用PackageManager的方法
            Method getPackageManager=activityThreadClz.getMethod("getPackageManager");
            //真正的packageManager对象
            Object pkgManager=getPackageManager.invoke(activityThread);

            //取得IPackageManager的类型
            Class<?> iPackageManagerClz=Class.forName("android.content.pm.IPackageManager");
            //获取sPackageManager准备替换
            Field packageManagerField=activityThreadClz.getDeclaredField("sPackageManager");
            packageManagerField.setAccessible(true);
            //替换activityThread中的sPackageManager为代理后的对象
            packageManagerField.set(activityThread, Proxy.newProxyInstance(
                    context.getClassLoader(),
                    new Class[]{iPackageManagerClz}, new InvocationHandler() {
                        @Override
                        public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
                            //先执行真正pkgManager里面的方法,pkgManager暴露的仅仅是IPackageManager的一个实现的子集
                            Object result=method.invoke(pkgManager,objects);
                            if ("getPackageInfo".equals(method.getName())){
                                //方法名对的上进行投入数处理
                                PackageInfo pkgInfo= (PackageInfo) result;
                                assert pkgInfo != null;
                                pkgInfo.versionName="5.01222A";
                            }
                            return  result;

                        }
                    }
            ));

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}
