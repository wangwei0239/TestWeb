package com.example.wangwei.testweb;

import android.app.Application;
import android.util.Log;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

/**
 * Created by wangwei on 16/3/31.
 */
public class AppApplication extends Application{
    private DbManager.DaoConfig daoConfig;
    public DbManager.DaoConfig getDaoConfig() {
        return daoConfig;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("TAG", "START");
        x.Ext.init(this);//Xutils初始化
        daoConfig = new DbManager.DaoConfig()
                .setDbName("lyj_db")//创建数据库的名称
                .setDbVersion(4)//数据库版本号
                .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                    @Override
                    public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                        // TODO: ...
                        Log.i("TAG", "upgrad");
                        // db.addColumn(...);
                        // db.dropTable(...);
                        // ...
                    }
                });//数据库更新操作

        DbManager db = x.getDb(daoConfig);
        LYJPerson person1=new LYJPerson();
        person1.setName("liyuanjinglyj");
        person1.setAge("1");
        LYJPerson person2=new LYJPerson();
        person2.setName("xutilsdemo");
        person2.setAge("2");
        LYJPerson person3=new LYJPerson();
        person3.setName("q");
        person3.setAge("3");
        LYJPerson person4=new LYJPerson();
        person4.setName("w");
        person4.setAge("4");
        LYJPerson person5=new LYJPerson();
        person5.setName("e");
        person5.setAge("5");
        LYJPerson person6=new LYJPerson();
        person6.setName("r");
        person6.setAge("6");
        LYJPerson person7=new LYJPerson();
        person7.setName("t");
        person7.setAge("7");
        LYJPerson person8=new LYJPerson();
        person8.setName("y");
        person8.setAge("8");
        LYJPerson person9=new LYJPerson();
        person9.setName("u");
        person9.setAge("9");
//        try {
//            db.save(person1);
//            db.save(person2);
//            db.save(person3);
//            db.save(person4);
//            db.save(person5);
//            db.save(person6);
//            db.save(person7);
//            db.save(person8);
//            db.save(person9);
//        } catch (DbException e) {
//            e.printStackTrace();
//        }
    }
}
