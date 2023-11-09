package com.huawei.movie.slice;

import com.alibaba.fastjson.JSON;
import com.huawei.movie.MyApplication;
import com.huawei.movie.ResourceTable;
import com.huawei.movie.entity.UserEntity;
import com.huawei.movie.http.RequestUtils;
import com.huawei.movie.http.ResultEntity;
import com.huawei.movie.utils.Common;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.TextField;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieLoginAbilitySlice extends AbilitySlice {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_movie_login);
        useLogin();
    }



    /**
     * @desc 点击登录
     * @since 2022-08-09
     * */
    private void useLogin(){
        TextField userText = (TextField) findComponentById(ResourceTable.Id_login_user);
        UserEntity userEntity = MyApplication.getInstance().getUserEntity();
        userText.setText(userEntity.getUserId());
        MyApplication.getInstance().setUserEntity(null);
        Button loginBtn = (Button) findComponentById(ResourceTable.Id_login_btn);
        loginBtn.setClickedListener(listener->{
            TextField passwordText = (TextField) findComponentById(ResourceTable.Id_login_password);
            String userId = userText.getText();
            String password = passwordText.getText();
            if(userId== null || "".equals(userId)){
                Common.showToast(ResourceTable.String_login_user_hint,this);
            }else if(password == null || "".equals(password)){
                Common.showToast(ResourceTable.String_login_password_hint,this);
            }else{
                UserEntity userEntity1 = new UserEntity();
                userEntity1.setUserId(userId);
                userEntity1.setPassword(Common.encryptToMD5(password));
                Call<ResultEntity> loginCall = RequestUtils.getInstance().login(userEntity1);
                loginCall.enqueue(new Callback<ResultEntity>() {
                    @Override
                    public void onResponse(Call<ResultEntity> call, Response<ResultEntity> response) {
                        UserEntity myUserEntity = JSON.parseObject(JSON.toJSONString(response.body().getData()),UserEntity.class);
                        getUITaskDispatcher().asyncDispatch(()->{
                            if(myUserEntity != null){
                                Common.showToast(ResourceTable.String_login_success, MovieLoginAbilitySlice.this);
                                MyApplication.getInstance().setUserEntity(myUserEntity);
                                present(new MovieMainAbilitySlice(),new Intent());
                            } else {
                                Common.showToast(ResourceTable.String_login_fail, MovieLoginAbilitySlice.this);
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<ResultEntity> call, Throwable throwable) {

                    }
                });
            }
        });
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }
}
