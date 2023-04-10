package com.android.work.demoset.design.create;

/**
 * 以下3中单例模式，均可在反射来破坏唯一性
 *
 * 枚举单例除外，由于枚举类是不让反射、序列化的，所以导致枚举唯一性无法被破坏
 */
public class SingleDesign {

    private SingleDesign(String des){
        this.des = des;
    }
    private String des;

    public void getDes(){
        System.out.println("============="+des+"==============");
    }

    /**
     * 懒汉式
     *
     * 由于是加了锁，导致创建变得慢
     */
    private static SingleDesign mSingleDesign;

    public static SingleDesign getInstance(){
        if(mSingleDesign == null){
            synchronized (SingleDesign.class){
                if(mSingleDesign == null){
                    mSingleDesign = new SingleDesign("我是懒汉式单例");
                }
            }
        }
        return mSingleDesign;
    }

    /**
     * 饿汉式
     *
     * 由于在单例中声明并new单例类对象，可能会导致单例类被加载导致单例对象被创建而未被引用，造成内存浪费
     */
    private static final SingleDesign singleDesign = new SingleDesign("我是饿汉式单例");

    public static SingleDesign getSingleDesign(){
        return singleDesign;
    }

    /**
     * 饿汉式，静态内部类
     *
     * 由于没有在单例类中局部声明静态内部类对象，所以不用担心单例类被加载后，导致静态内部类被虚拟机加载而不用的情况
     */
    public static SingleDesign getInstance2(){
        return InnerSingleDesign.singleDesign;
    }

    private static class InnerSingleDesign{
        private static final SingleDesign singleDesign = new SingleDesign("饿汉式，静态内部类");
    }
}
