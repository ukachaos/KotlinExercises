package com.uka.testapplication;

import android.app.Application;

public class MyApplication extends Application {

    private MyComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        component = DaggerMyComponent.builder().myModule(new MyModule()).build();
    }

    public MyComponent getComponent(){
        return component;
    }
}
