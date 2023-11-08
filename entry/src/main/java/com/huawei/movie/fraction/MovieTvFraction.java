package com.huawei.movie.fraction;

import com.alibaba.fastjson.JSON;
import com.huawei.movie.ResourceTable;
import com.huawei.movie.entity.CategoyEntity;
import com.huawei.movie.http.RequestUtils;
import com.huawei.movie.http.ResultEntity;
import ohos.aafwk.ability.fraction.Fraction;
import ohos.aafwk.ability.fraction.FractionScheduler;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.LayoutScatter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class MovieTvFraction extends Fraction {
    @Override
    protected Component onComponentAttached(LayoutScatter scatter, ComponentContainer container, Intent intent) {
        //指定布局文件
        return scatter.parse(ResourceTable.Layout_fraction_movie_tv, container, false);
    }

    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        getFractionAbility().getUITaskDispatcher().asyncDispatch(()-> {
            getFractionAbility().getFractionManager()
                    .startFractionScheduler()
                    .add(ResourceTable.Id_tv_avater_wrapper, new MovieAvaterFraction())
                    .add(ResourceTable.Id_tv_avater_wrapper,new MovieSearchFraction("电视剧"))
                    .add(ResourceTable.Id_tv_swriper_wrapper,new MovieSwiperFraction("电视剧"))
                    .submit();
        });
        getAllCategoryListByPageName();
    }

    /**
     * @des 获取首页所有分类电影
     * @since 2022-07-16
     * */
    private void getAllCategoryListByPageName(){
        Call<ResultEntity> allCategoryListByPageNameCall = RequestUtils.getInstance().getAllCategoryListByPageName("电视剧");
        allCategoryListByPageNameCall.enqueue(new Callback<ResultEntity>() {
            @Override
            public void onResponse(Call<ResultEntity> call, Response<ResultEntity> response) {
                List<CategoyEntity> categoyEntityList = JSON.parseArray(JSON.toJSONString(response.body().getData()), CategoyEntity.class);
                System.out.println(categoyEntityList);
                getContext().getUITaskDispatcher().asyncDispatch(()->{
                    getFractionAbility().getUITaskDispatcher().asyncDispatch(()-> {
                        FractionScheduler fractionScheduler = getFractionAbility().getFractionManager().startFractionScheduler();
                        for (CategoyEntity categoyEntity:categoyEntityList){
                            fractionScheduler.add(ResourceTable.Id_tv_category_list,new MovieCategoryFraction(categoyEntity));
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
