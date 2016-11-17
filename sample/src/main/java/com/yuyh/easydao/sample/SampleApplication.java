package com.yuyh.easydao.sample;

import android.app.Application;
import android.content.Context;

/**
 * @author yuyh.
 * @date 2016/11/17.
 */
public class SampleApplication extends Application {

    public static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
    }
}
