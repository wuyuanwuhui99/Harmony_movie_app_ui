package com.huawei.movie.fraction;

import com.alibaba.fastjson.JSON;
import com.huawei.movie.ResourceTable;
import com.huawei.movie.config.Config;
import com.huawei.movie.entity.CategoyEntity;
import com.huawei.movie.entity.MovieEntity;
import com.huawei.movie.entity.UserMsgEntity;
import com.huawei.movie.http.RequestUtils;
import com.huawei.movie.http.ResultEntity;
import com.huawei.movie.provider.MovieItemProvider;
import ohos.aafwk.ability.fraction.Fraction;
import ohos.aafwk.ability.fraction.FractionScheduler;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.*;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class MyFraction extends Fraction {
    Component component;
    @Override
    protected Component onComponentAttached(LayoutScatter scatter, ComponentContainer container, Intent intent) {
        //指定布局文件
        component = scatter.parse(ResourceTable.Layout_fraction_my, container, false);
        return component;
    }

    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        getFractionAbility().getUITaskDispatcher().asyncDispatch(()-> {
            getFractionAbility().getFractionManager()
                    .startFractionScheduler()
                    .add(ResourceTable.Id_my_avater_layout, new AvaterFraction("big"))
                    .submit();
        });
        setUserName();
        getUserMsg();
        getPlayRecord();
        useUserPage();
    }

    private void setUserName(){
        Text userName = (Text) component.findComponentById(ResourceTable.Id_user_name);
        userName.setText(Config.userEntity.getUsername());
        if(Config.userEntity.getSign()!=null){
            Text sign = (Text) component.findComponentById(ResourceTable.Id_sign);
            sign.setText(Config.userEntity.getSign());
        }
    }

    /**
     * @des 获取用户四个指标
     * @since 2022-07-17
     * */
    private void getUserMsg(){
        Call<ResultEntity> userMsgCall = RequestUtils.getInstance().getUserMsg();
        userMsgCall.enqueue(new Callback<ResultEntity>() {
            @Override
            public void onResponse(Call<ResultEntity> call, Response<ResultEntity> response) {
                UserMsgEntity userMsgEntity = JSON.parseObject(JSON.toJSONString(response.body().getData()), UserMsgEntity.class);
                getContext().getUITaskDispatcher().asyncDispatch(()->{
                    ((Text)component.findComponentById(ResourceTable.Id_my_age_count)).setText(userMsgEntity.getUserAge());
                    ((Text)component.findComponentById(ResourceTable.Id_my_favorite_count)).setText(userMsgEntity.getFavoriteCount());
                    ((Text)component.findComponentById(ResourceTable.Id_my_play_count)).setText(userMsgEntity.getPlayRecordCount());
                    ((Text)component.findComponentById(ResourceTable.Id_my_view_count)).setText(userMsgEntity.getViewRecordCount());
                });
            }

            @Override
            public void onFailure(Call<ResultEntity> call, Throwable throwable) {

            }
        });
    }

    /**
     * @des 获取用户播放记录
     * @since 2022-07-17
     * */
    private void getPlayRecord(){
        Call<ResultEntity> playRecordCall = RequestUtils.getInstance().getPlayRecord();
        playRecordCall.enqueue(new Callback<ResultEntity>() {
            @Override
            public void onResponse(Call<ResultEntity> call, Response<ResultEntity> response) {
                List<MovieEntity> movieEntityList = JSON.parseArray(JSON.toJSONString(response.body().getData()), MovieEntity.class);
                getContext().getUITaskDispatcher().asyncDispatch(()->{
                    ListContainer listContainer = (ListContainer)component.findComponentById(ResourceTable.Id_play_record_list);
                    MovieItemProvider provider = new MovieItemProvider(movieEntityList,getContext(),getFractionAbility());
                    listContainer.setItemProvider(provider);
                });
            }

            @Override
            public void onFailure(Call<ResultEntity> call, Throwable throwable) {

            }
        });
    }

    /**
     * @des 跳转到用户信息编辑页面
     * @since 2022-08-06
     * */
    private void useUserPage(){
        Image editImg = (Image) component.findComponentById(ResourceTable.Id_icon_edit);
        editImg.setClickedListener(listener->{
            //跳转到哪个页面中（意图）
            Intent intent = new Intent();
            //包含了页面跳转的信息
            Operation operation = new Intent.OperationBuilder()
                    //要跳转到哪个设备上，如果传递一个空的内容，表示跳转到本机
                    .withDeviceId("")
                    //要跳转到哪个应用上，小括号里面可以写包名
                    .withBundleName("com.huawei.movie")
                    //要跳转的页面
                    .withAbilityName("com.huawei.movie.ability.UserAbility")
                    //表示将上面的三个信息进行打包
                    .build();
            //把打包之后的operation设置到意图当中
            intent.setOperation(operation);
            //跳转页面
            getFractionAbility().startAbility(intent);
        });
    }
}
