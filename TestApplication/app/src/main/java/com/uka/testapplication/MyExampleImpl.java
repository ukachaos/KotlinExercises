package com.uka.testapplication;

import java.util.Date;

public class MyExampleImpl implements MyExample {

    private long mDate;

    public MyExampleImpl(){
        mDate = new Date().getDate();
    }

    @Override
    public long getDate() {
        return mDate;
    }
}
