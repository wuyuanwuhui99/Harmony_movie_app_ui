package com.huawei.movie.ability;

import ohos.aafwk.ability.Ability;
import ohos.aafwk.ability.DataAbilityHelper;
import ohos.aafwk.content.Intent;
import ohos.data.DatabaseHelper;
import ohos.data.dataability.DataAbilityUtils;
import ohos.data.orm.OrmPredicates;
import ohos.data.rdb.*;
import ohos.data.resultset.ResultSet;
import ohos.data.dataability.DataAbilityPredicates;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.utils.net.Uri;
import ohos.utils.PacMap;

import java.io.FileDescriptor;

public class DataAbility extends Ability {
    private RdbStore rdbStore;
    private StoreConfig storeConfig = StoreConfig.newDefaultConfig("UserStore.db");
    private static final HiLogLabel LABEL_LOG = new HiLogLabel(3, 0xD001100, "Demo");
    private RdbOpenCallback rdbOpenCallback = new RdbOpenCallback() {
        @Override
        public void onCreate(RdbStore myRdbStore) {
            myRdbStore.executeSql("create table if not exists user(user_id text primary key,password text," +
                    "create_date text,update_date text,username text,telephone text,email text,avater text," +
                    "birthday text,sex text,role text,sign text,region text,disabled integer)");
            myRdbStore.executeSql("create table if not exists token(token text primary key,create_time text,update_time text)");
            myRdbStore.executeSql("create table if not exists search_record(name text,create_time text)");
        }

        @Override
        public void onUpgrade(RdbStore rdbStore, int i, int i1) {

        }
    };
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        HiLog.info(LABEL_LOG, "DataAbility onStart");
        DatabaseHelper helper = new DatabaseHelper(this);
        rdbStore = helper.getRdbStore(storeConfig,1,rdbOpenCallback);
    }

    /**
     * @param uri
     * @param columns 查询操作需要返回的列 ["user_id","username"]
     * @param predicates 封装的查询条件，相当于tkMapper中的example对象
     * */
    @Override
    public ResultSet query(Uri uri, String[] columns, DataAbilityPredicates predicates) {
        String tableName = getTableName(uri);
        RdbPredicates rdbPredicates = DataAbilityUtils.createRdbPredicates(predicates, tableName);
        if(rdbStore != null){
            ResultSet resultSet = rdbStore.query(rdbPredicates,columns);
            return resultSet;
        }
        return null;
    }

    /**
     * @deprecated 重写inser方法
     * @params value相当于map对象，存储key-value键值对
     * */
    @Override
    public int insert(Uri uri, ValuesBucket value) {
        String tableName = getTableName(uri);
        return (int)rdbStore.insert(tableName,value);
    }

    private String getTableName(Uri uri){
        String path = uri.getLastPath();
        if("user".equalsIgnoreCase(path)){
            return "user";
        }else if("search_record".equalsIgnoreCase(path)){
            return "search_record";
        }else if("token".equalsIgnoreCase(path)){
            return "token";
        }
        return "";
    }

    @Override
    public int delete(Uri uri, DataAbilityPredicates predicates) {
        String tableName = getTableName(uri);
        RdbPredicates rdbPredicates = DataAbilityUtils.createRdbPredicates(predicates,tableName);
        return rdbStore.delete(rdbPredicates);
    }

    @Override
    public int update(Uri uri, ValuesBucket value, DataAbilityPredicates predicates) {
        String tableName = getTableName(uri);
        RdbPredicates rdbPredicates = DataAbilityUtils.createRdbPredicates(predicates,tableName);
        return rdbStore.update(value,rdbPredicates);
    }

    @Override
    public PacMap call(String method, String arg, PacMap extras) {
        return null;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }
}