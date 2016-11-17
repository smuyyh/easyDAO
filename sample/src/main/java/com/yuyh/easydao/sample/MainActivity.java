/**
 * Copyright (C) 2016 smuyyh
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yuyh.easydao.sample;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.TextView;

import com.yuyh.easydao.DB;
import com.yuyh.easydao.exception.DBException;
import com.yuyh.easydao.interfaces.IDAO;
import com.yuyh.easydao.interfaces.IDBListener;
import com.yuyh.easydao.sample.bean.User;
import com.yuyh.easydao.sample.utils.ToastUtils;
import com.yuyh.easydao.utils.LogUtils;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private Context mContext;

    private String database = "";
    private String table = "";

    private int lastId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        ButterKnife.bind(this);

        builder = new AlertDialog.Builder(this)
                .setNegativeButton($(R.string.cancel), cancelListener);
    }

    @OnClick(R.id.createDatabase)
    public void createDatabase() {

        final EditText et = new EditText(this);
        et.setGravity(Gravity.CENTER);
        et.setHint("database name");

        dialog = builder.setTitle($(R.string.create_database))
                .setView(et)
                .setPositiveButton($(R.string.confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        database = et.getText().toString();
                        if (TextUtils.isEmpty(database)) {
                            ToastUtils.showSingleToast("Invalid database name.");
                            return;
                        }

                        try {
                            DB.getInstance(mContext).getDatabase(1, database, listener);
                            ToastUtils.showSingleToast("create/open database successed!");
                        } catch (DBException e) {
                            ToastUtils.showSingleToast(e.getMessage());
                        }
                    }
                }).create();
        dialog.show();
    }

    @OnClick(R.id.deleteDatabase)
    public void deleteDatabase() {
        final EditText et = new EditText(this);
        et.setGravity(Gravity.CENTER);
        if (!TextUtils.isEmpty(database)) {
            et.setText(database);
        } else {
            et.setHint("database name");
        }

        dialog = builder.setTitle($(R.string.delete_database))
                .setView(et)
                .setPositiveButton($(R.string.confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        database = et.getText().toString();
                        if (TextUtils.isEmpty(database)) {
                            ToastUtils.showSingleToast("Invalid database name.");
                            return;
                        }

                        boolean ret = DB.getInstance(mContext).deleteDatabase(database);
                        if (ret) {
                            database = "";
                            ToastUtils.showSingleToast("delete database successed!");
                        } else {
                            ToastUtils.showSingleToast("database doesn't exists!");
                        }
                    }
                }).create();
        dialog.show();
    }

    @OnClick(R.id.createTable)
    public void createTable() {

        if (!isDatabaseExists()) {
            return;
        }

        final EditText et = new EditText(this);
        et.setGravity(Gravity.CENTER);
        if (!TextUtils.isEmpty(table)) {
            et.setText(table);
        } else {
            et.setHint("table name");
        }

        dialog = builder.setTitle($(R.string.create_table))
                .setView(et)
                .setPositiveButton($(R.string.confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        table = et.getText().toString();
                        if (TextUtils.isEmpty(table)) {
                            ToastUtils.showSingleToast("Invalid table name.");
                            return;
                        }

                        try {
                            DB.getInstance(mContext).getDatabase(1, database, table, User.class, listener);
                            ToastUtils.showSingleToast("create/open table successed!");
                        } catch (DBException e) {
                            e.printStackTrace();
                        }
                    }
                }).create();
        dialog.show();
    }

    @OnClick(R.id.dropTable)
    public void dropTable() {
        if (!isDatabaseExists()) {
            return;
        }

        try {
            // database and table will be created!
            IDAO dao = DB.getInstance(mContext).getDatabase(1, database, listener);
            if (TextUtils.isEmpty(table) || !dao.isTableExist(table)) {
                ToastUtils.showSingleToast("table doesn't exists! \ncreate it at first!");
                return;
            }
            dao.initTable(table, User.class);
            dao.dropTable();
            table = "";
            ToastUtils.showSingleToast("drop table successed!");
        } catch (DBException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.dropAllTable)
    public void dropAllTable() {
        if (!isDatabaseExists()) {
            return;
        }

        try {
            IDAO dao = DB.getInstance(mContext).getDatabase(1, database, listener);
            dao.dropAllTable();
            table = "";
            ToastUtils.showSingleToast("drop all table successed!");
        } catch (DBException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.saveEntity)
    public void save() {
        if (!isDatabaseExists()) {
            return;
        }

        final User user = new User("yuyh", 23, false, 1.0);

        TextView tv = new TextView(this);
        tv.setText(user.toString());

        dialog = builder.setTitle($(R.string.save_entity))
                .setView(tv)
                .setPositiveButton($(R.string.confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        try {
                            IDAO<User> dao = DB.getInstance(mContext).getDatabase(1, database, listener);
                            if (TextUtils.isEmpty(table) || !dao.isTableExist(table)) {
                                ToastUtils.showSingleToast("table doesn't exists! \ncreate it at first!");
                                return;
                            }
                            dao.initTable(table, User.class);

                            dao.save(user);

                            User saveUser = dao.findLastEntity();
                            lastId = saveUser.getId();
                            ToastUtils.showSingleToast("save entity successed!\n" + saveUser.toString());
                        } catch (DBException e) {
                            e.printStackTrace();
                        }
                    }
                }).create();
        dialog.show();
    }

    @OnClick(R.id.delEntity)
    public void deleteById() {
        if (!isDatabaseExists()) {
            return;
        }

        final EditText et = new EditText(this);
        et.setGravity(Gravity.CENTER);
        if (lastId != -1) {
            et.setText(lastId + "");
        } else {
            et.setHint("ids, separated by comma(2,3,4)");
        }

        dialog = builder.setTitle($(R.string.delete_entity))
                .setView(et)
                .setPositiveButton($(R.string.confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String str = et.getText().toString();
                        String[] numsStr = str.trim().replaceAll("，", ",").split(",");
                        if (numsStr.length < 1) {
                            ToastUtils.showSingleToast("Invalid id.");
                            return;
                        }

                        try {
                            int[] nums = new int[numsStr.length];
                            for (int i = 0; i < numsStr.length; i++) {
                                nums[i] = Integer.parseInt(numsStr[i]);
                            }

                            IDAO<User> dao = DB.getInstance(mContext).getDatabase(1, database, table, User.class, listener);
                            dao.delete(nums);
                            ToastUtils.showSingleToast("delete successed!");
                        } catch (DBException e) {
                            e.printStackTrace();
                        } catch (NumberFormatException e) {
                            ToastUtils.showSingleToast(e.toString());
                        }
                    }
                }).create();
        dialog.show();
    }

    @OnClick(R.id.delAllEntity)
    public void deleteAll() {
        if (!isDatabaseExists()) {
            return;
        }

        try {
            IDAO dao = DB.getInstance(mContext).getDatabase(1, database, listener);
            if (TextUtils.isEmpty(table) || !dao.isTableExist(table)) {
                ToastUtils.showSingleToast("table doesn't exists! \ncreate it at first!");
                return;
            }
            dao.initTable(table, User.class);
            dao.deleteAll();
            lastId = -1;
            ToastUtils.showSingleToast("delete all entity successed!");
        } catch (DBException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.updateEntityById)
    public void updateLastEntity() {
        if (!isDatabaseExists()) {
            return;
        }

        TextView tv = new TextView(this);

        try {
            final IDAO<User> dao = DB.getInstance(mContext).getDatabase(1, database, listener);
            if (TextUtils.isEmpty(table) || !dao.isTableExist(table)) {
                ToastUtils.showSingleToast("table doesn't exists! \ncreate it at first!");
                return;
            }
            dao.initTable(table, User.class);

            final User user = dao.findLastEntity();
            tv.setText(user.toString());
            tv.append("\n\nafter updated:\n\n");
            user.setName("kyrie");
            user.setAge(24);
            user.setDistance(100000.00);
            tv.append(user.toString());

            dialog = builder.setTitle($(R.string.save_entity))
                    .setView(tv)
                    .setPositiveButton($(R.string.confirm), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            try {
                                dao.update(user);

                                User saveUser = dao.findLastEntity();
                                lastId = saveUser.getId();
                                ToastUtils.showSingleToast("update entity successed!\n" + saveUser.toString());
                            } catch (DBException e) {
                                e.printStackTrace();
                            }
                        }
                    }).create();
            dialog.show();
        } catch (DBException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.updateEntityByCondition)
    public void updateByCondition() {
        if (!isDatabaseExists()) {
            return;
        }

        TextView tv = new TextView(this);

        try {
            final IDAO<User> dao = DB.getInstance(mContext).getDatabase(1, database, listener);
            if (TextUtils.isEmpty(table) || !dao.isTableExist(table)) {
                ToastUtils.showSingleToast("table doesn't exists! \ncreate it at first!");
                return;
            }
            dao.initTable(table, User.class);

            final User user = dao.findLastEntity();
            tv.setText(user.toString());
            tv.append("\n\nafter updated:\n\n");
            user.setName("kyrie");
            user.setAge((int) (Math.random() * 100));
            user.setDistance(100000.00);
            tv.append(user.toString());

            final String condition = "id = " + user.getId();
            tv.append("\n\ncondition:\"" + condition + "\"\n\n");

            dialog = builder.setTitle($(R.string.save_entity))
                    .setView(tv)
                    .setPositiveButton($(R.string.confirm), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            try {
                                dao.updateByCondition(condition, user);

                                User saveUser = dao.findLastEntity();
                                lastId = saveUser.getId();
                                ToastUtils.showSingleLongToast("update entity successed!\n" + saveUser.toString());
                            } catch (DBException e) {
                                e.printStackTrace();
                            }
                        }
                    }).create();
            dialog.show();
        } catch (DBException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.findLastEntity)
    public void findLast() {
        if (!isDatabaseExists()) {
            return;
        }

        try {
            IDAO<User> dao = DB.getInstance(mContext).getDatabase(1, database, listener);
            if (TextUtils.isEmpty(table) || !dao.isTableExist(table)) {
                ToastUtils.showSingleToast("table doesn't exists! \ncreate it at first!");
                return;
            }
            dao.initTable(table, User.class);

            User user = dao.findLastEntity();
            if (user != null) {
                ToastUtils.showSingleToast(user.toString());
            } else {
                ToastUtils.showSingleToast("no data");
            }
        } catch (DBException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.findByCondition)
    public void findByCondition() {
        if (!isDatabaseExists()) {
            return;
        }

        TextView tv = new TextView(this);

        try {
            final IDAO<User> dao = DB.getInstance(mContext).getDatabase(1, database, listener);
            if (TextUtils.isEmpty(table) || !dao.isTableExist(table)) {
                ToastUtils.showSingleToast("table doesn't exists! \ncreate it at first!");
                return;
            }
            dao.initTable(table, User.class);

            final User user = dao.findLastEntity();
            if (user == null) {
                ToastUtils.showSingleToast("no data");
                return;
            }
            final String condition = "id = " + user.getId();
            tv.append("\n\ncondition:\"" + condition + "\"\n\n");

            dialog = builder.setTitle($(R.string.find_entity_by_condition))
                    .setView(tv)
                    .setPositiveButton($(R.string.confirm), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            try {
                                List<User> users = dao.findByCondition(condition);

                                if (users != null && users.size() > 0) {
                                    ToastUtils.showSingleLongToast("find by condition successed!\n" + users.get(0).toString());
                                }
                            } catch (DBException e) {
                                e.printStackTrace();
                            }
                        }
                    }).create();
            dialog.show();
        } catch (DBException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.findAllEntity)
    public void findAllEntity() {
        if (!isDatabaseExists()) {
            return;
        }

        try {
            final IDAO<User> dao = DB.getInstance(mContext).getDatabase(1, database, listener);
            if (TextUtils.isEmpty(table) || !dao.isTableExist(table)) {
                ToastUtils.showSingleToast("table doesn't exists! \ncreate it at first!");
                return;
            }
            dao.initTable(table, User.class);

            List<User> users = dao.findAll();

            if (users != null && users.size() > 0) {
                ToastUtils.showSingleLongToast("find all entity successed!\ncount=" + users.size());
            }
        } catch (DBException e) {
            e.printStackTrace();
        }
    }

    private String $(int id) {
        return getResources().getString(id);
    }

    private boolean isDatabaseExists() {
        if (TextUtils.isEmpty(database)
                || !DB.getInstance(mContext).isDatabaseExists(database)) {
            ToastUtils.showSingleToast("database doesn't exists! \ncreate it at first!");
            return false;
        }
        return true;
    }

    private DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    };

    private IDBListener listener = new IDBListener() {
        @Override
        public void onUpdate(IDAO dao, int oldVersion, int newVersion) {
            LogUtils.i("database updated: old version = " + oldVersion + " , new version = " + newVersion);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
