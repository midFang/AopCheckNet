package com.fangsf.aopchecknet;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * Created by fangsf on 2018/10/25.
 * Useful:  处理切点,  基本都是固定的写法
 */
@Aspect  /* 这个注解不能少 使用AspectJ注解 */
public class SectionAspect {


    /**
     * 1, 找到,checkNet切点,    * *(..)  代表可以处理所有的方法
     */
    @Pointcut("execution(@com.fangsf.aopchecknet.CheckNet * *(..))")
    public void checkNetBehavior() {

    }

    /**
     * 2, 处理切面
     */
    @Around("checkNetBehavior()")
    public Object checkNet(ProceedingJoinPoint point) throws Throwable {
        Log.e("SectionAspect", "checkNet");

        // 将所有的方法, 在这里抽取出来公共的部分,做统一的处理,可以检测网络, 检测权限, 埋点分析,日志上传,检测是否登录

        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        // 获取方法上面的注解
        CheckNet checkNet = methodSignature.getMethod().getAnnotation(CheckNet.class);
        if (checkNet != null) {
            // 获取到context对象, 检测是否有网络
            Object pointThis = point.getThis();
            Context context = getContext(pointThis);
            if (context != null) {
                if (!isNetworkAvailable(context)) {
                    // 没有网络,不再执行操作了,
                    Toast.makeText(context, "请检测您的网络", Toast.LENGTH_SHORT).show();
                    return null;
                }
            }

        }


        return point.proceed();  // 代表处理了方法
    }


    /**
     * 通过对象获取上下文
     *
     * @param object
     * @return
     */
    private Context getContext(Object object) {
        if (object instanceof Activity) {
            return (Activity) object;
        } else if (object instanceof Fragment) {
            Fragment fragment = (Fragment) object;
            return fragment.getActivity();
        } else if (object instanceof View) {
            View view = (View) object;
            return view.getContext();
        }
        return null;
    }

    /**
     * 检查当前网络是否可用
     *
     * @return
     */
    private static boolean isNetworkAvailable(Context context) {
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


}
