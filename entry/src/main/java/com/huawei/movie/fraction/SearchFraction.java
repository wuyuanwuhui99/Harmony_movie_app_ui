package com.huawei.movie.fraction;

import com.alibaba.fastjson.JSON;
import com.huawei.movie.ResourceTable;
import com.huawei.movie.ability.SearchAbility;
import com.huawei.movie.entity.MovieEntity;
import com.huawei.movie.http.RequestUtils;
import com.huawei.movie.http.ResultEntity;
import ohos.aafwk.ability.fraction.Fraction;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.*;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFraction extends Fraction {
    Component component;
    String classify;
    MovieEntity keyWordEntity;

    @Override
    protected Component onComponentAttached(LayoutScatter scatter, ComponentContainer container, Intent intent) {
        component = scatter.parse(ResourceTable.Layout_fraction_search, container, false);
        //指定布局文件
        return component;
    }

    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        getKeyWord();
        addClickListener();
    }

    public SearchFraction(String classify){
        this.classify = classify;
    }

    private void getKeyWord(){
        Call<ResultEntity> keyWordCall = RequestUtils.getInstance().getKeyWord(classify);
        keyWordCall.enqueue(new Callback<ResultEntity>() {

            @Override
            public void onResponse(Call<ResultEntity> call, Response<ResultEntity> response) {
                keyWordEntity = JSON.parseObject(JSON.toJSONString(response.body().getData()), MovieEntity.class);
                getContext().getUITaskDispatcher().asyncDispatch(()->{
                    Text text = (Text) component.findComponentById(ResourceTable.Id_search_text);
                    text.setText(keyWordEntity.getMovieName());
                });
            }

            @Override
            public void onFailure(Call<ResultEntity> call, Throwable throwable) {

            }
        });
    }

    private void addClickListener(){
        DirectionalLayout layout = (DirectionalLayout) component.findComponentById(ResourceTable.Id_search_layout);
        layout.setClickedListener(listener->{
            Intent intent = new Intent();
            //包含了页面跳转的信息
            Operation operation = new Intent.OperationBuilder()
                    //要跳转到哪个设备上，如果传递一个空的内容，表示跳转到本机
                    .withDeviceId("")
                    //要跳转到哪个应用上，小括号里面可以写包名
                    .withBundleName(getBundleName())
                    //要跳转的页面
                    .withAbilityName(SearchAbility.class.getName())
                    //表示将上面的三个信息进行打包
                    .build();
            //把打包之后的operation设置到意图当中
            intent.setOperation(operation);
            intent.setParam("movieItem",JSON.toJSONString(keyWordEntity));
            //跳转页面
            getFractionAbility().startAbility(intent);
        });

    }
}
