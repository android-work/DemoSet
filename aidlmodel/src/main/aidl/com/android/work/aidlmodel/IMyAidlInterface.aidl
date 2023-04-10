// IMyAidlInterface.aidl
package com.android.work.aidlmodel;

import com.android.work.aidlmodel.AidlBean;
import android.os.ParcelFileDescriptor;
// Declare any non-default types here with import statements

interface IMyAidlInterface {

    void readStr(String str);
    int backStr(int value);
    AidlBean backBean();
    oneway void opration();

    void sendData(in ParcelFileDescriptor pfd);
}