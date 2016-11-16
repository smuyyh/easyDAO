package com.yuyh.easydao.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.yuyh.easydao.DB;
import com.yuyh.easydao.sample.bean.User;
import com.yuyh.easydao.exception.DBException;
import com.yuyh.easydao.interfaces.IDAO;
import com.yuyh.easydao.interfaces.IDBListener;
import com.yuyh.easydao.utils.LogUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        User user = new User("yuyh", 23, false, 10.0);

        try {
            IDAO dao = DB.getInstance(this).getDatabase(1, "user", "user", User.class, new IDBListener<User>() {
                @Override
                public void onUpdate(IDAO<User> dao, int oldVersion, int newVersion) {
                    LogUtils.i("database updated: old version = " + oldVersion + " , new version = " + newVersion);
                }
            });
            LogUtils.i("save entity");
            dao.save(user);

            LogUtils.i("find last entity");
            user = (User) dao.findLastEntity();

            LogUtils.i("update entity");
            user.setName("haha");
            dao.update(user);


        } catch (DBException e) {
            LogUtils.e(e.toString());
        }
    }
}
