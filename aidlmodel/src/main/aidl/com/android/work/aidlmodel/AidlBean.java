package com.android.work.aidlmodel;

import android.os.Parcel;
import android.os.Parcelable;

public class AidlBean implements Parcelable {
    String a1;
    String a2;
    int a3;
    boolean a4;

    public String getA1() {
        return a1;
    }

    public String getA2() {
        return a2;
    }

    public int getA3() {
        return a3;
    }

    public boolean isA4() {
        return a4;
    }

    public AidlBean(String a1, String a2, int a3, boolean a4) {
        this.a1 = a1;
        this.a2 = a2;
        this.a3 = a3;
        this.a4 = a4;
    }

    protected AidlBean(Parcel in) {
        a1 = in.readString();
        a2 = in.readString();
        a3 = in.readInt();
        a4 = in.readByte() != 0;
    }

    public static final Creator<AidlBean> CREATOR = new Creator<AidlBean>() {
        @Override
        public AidlBean createFromParcel(Parcel in) {
            return new AidlBean(in);
        }

        @Override
        public AidlBean[] newArray(int size) {
            return new AidlBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(a1);
        dest.writeString(a2);
        dest.writeInt(a3);
        dest.writeByte((byte) (a4 ? 1 : 0));
    }
}
