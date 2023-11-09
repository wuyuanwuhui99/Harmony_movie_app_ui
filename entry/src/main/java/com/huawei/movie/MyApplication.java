package com.huawei.movie;

import com.huawei.movie.entity.UserEntity;
import ohos.aafwk.ability.AbilityPackage;

public class MyApplication extends AbilityPackage {
    @Override
    public void onInitialize() {
        mApp = this;
        super.onInitialize();
    }

    private static MyApplication mApp;

    public static MyApplication getInstance(){
        return mApp;
    }

    private String token;

    private UserEntity userEntity;

    public void setToken(String token){
        this.token = token;
    }

    public String getToken(){
        return token;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }
}
