package com.huawei.movie.fraction;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.huawei.movie.config.Config;
import com.huawei.movie.entity.CategoyEntity;
import com.huawei.movie.entity.MovieEntity;
import com.huawei.movie.entity.UserEntity;
import com.huawei.movie.http.RequestUtils;
import com.huawei.movie.http.ResultEntity;
import ohos.aafwk.ability.fraction.Fraction;
import ohos.aafwk.ability.fraction.FractionScheduler;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.LayoutScatter;
import com.huawei.movie.ResourceTable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class HomeFraction extends Fraction {
    Component rootComponent;
    @Override
    protected Component onComponentAttached(LayoutScatter scatter, ComponentContainer container, Intent intent) {
        if(rootComponent == null){
            rootComponent = scatter.parse(ResourceTable.Layout_fraction_home, container, false);
        }
        //指定布局文件
        return rootComponent;
    }

    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        getFractionAbility().getUITaskDispatcher().asyncDispatch(()-> {
            getFractionAbility().getFractionManager()
                .startFractionScheduler()
                .add(ResourceTable.Id_avater_wrapper, new AvaterFraction())
                .add(ResourceTable.Id_avater_wrapper,new SearchFraction())
                .add(ResourceTable.Id_swriper_wrapper,new SwiperFraction("电影"))
                .submit();
        });
        getAllCategoryListByPageName();
    }

    /**
     * @des 获取首页所有分类电影
     * @since 2022-07-014
     * */
    private void getAllCategoryListByPageName(){
        Call<ResultEntity> allCategoryListByPageName = RequestUtils.getInstance().getAllCategoryListByPageName("首页");
        allCategoryListByPageName.enqueue(new Callback<ResultEntity>() {
            @Override
            public void onResponse(Call<ResultEntity> call, Response<ResultEntity> response) {
                List<CategoyEntity> categoyEntityList = JSON.parseArray(JSON.toJSONString(response.body().getData()), CategoyEntity.class);
                System.out.println(categoyEntityList);
                getContext().getUITaskDispatcher().asyncDispatch(()->{
                    getFractionAbility().getUITaskDispatcher().asyncDispatch(()-> {
                        FractionScheduler fractionScheduler = getFractionAbility().getFractionManager().startFractionScheduler();
                        for (CategoyEntity categoyEntity:categoyEntityList){
                            fractionScheduler.add(ResourceTable.Id_category_list,new CategoryFraction(categoyEntity.getCategory()));
                        }
                        fractionScheduler.submit();
                    });
                });
            }

            @Override
            public void onFailure(Call<ResultEntity> call, Throwable throwable) {

            }
        });
    }
}
