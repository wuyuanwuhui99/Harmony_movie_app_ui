package com.huawei.movie.fraction;

import com.alibaba.fastjson.JSON;
import com.huawei.movie.ResourceTable;
import com.huawei.movie.entity.KeyWordEntity;
import com.huawei.movie.http.RequestUtils;
import com.huawei.movie.http.ResultEntity;
import ohos.aafwk.ability.fraction.Fraction;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.Text;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFraction extends Fraction {
    Component component;
    String classify;
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
    }

    public SearchFraction(String classify){
        this.classify = classify;
    }

    private void getKeyWord(){
        Call<ResultEntity> keyWordCall = RequestUtils.getInstance().getKeyWord(classify);
        keyWordCall.enqueue(new Callback<ResultEntity>() {

            @Override
            public void onResponse(Call<ResultEntity> call, Response<ResultEntity> response) {
                KeyWordEntity keyWordEntity = JSON.parseObject(JSON.toJSONString(response.body().getData()), KeyWordEntity.class);
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
}
