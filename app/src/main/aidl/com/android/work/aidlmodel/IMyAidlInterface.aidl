// IMyAidlInterface.aidl
package com.android.work.aidlmodel;

import com.android.work.aidlmodel.AidlBean;
// Declare any non-default types here with import statements

interface IMyAidlInterface {

    void readStr(String str);
    int backStr(int value);
    AidlBean backBean();
    oneway void opration();
}