package com.huawei.movie.slice;

import com.alibaba.fastjson.JSON;
import com.huawei.movie.ResourceTable;
import com.huawei.movie.entity.MovieEntity;
import com.huawei.movie.http.RequestUtils;
import com.huawei.movie.http.ResultEntity;
import com.huawei.movie.provider.MovieItemProvider;
import com.huawei.movie.provider.SearchMovieRowProvider;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.ListContainer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class SearchAbilitySlice extends AbilitySlice {
    MovieEntity movieEntity;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_search);
        String movieItem = getAbility().getIntent().getStringParam("movieItem");
        movieEntity = JSON.parseObject(movieItem, MovieEntity.class);
        getRecommend();
    }

    /**
     * @desc 获取推荐影片
     * @since 2022-07-25
     */
    private void getRecommend(){
        Call<ResultEntity> getRecommendCall = RequestUtils.getInstance().getRecommend(movieEntity.getClassify());
        getRecommendCall.enqueue(new Callback<ResultEntity>() {
            @Override
            public void onResponse(Call<ResultEntity> call, Response<ResultEntity> response) {
                List<MovieEntity> movieEntityList = JSON.parseArray(JSON.toJSONString(response.body().getData()), MovieEntity.class);
                getContext().getUITaskDispatcher().asyncDispatch(()->{
                    ListContainer listContainer = (ListContainer)findComponentById(ResourceTable.Id_search_movie_list);
                    listContainer.setItemProvider(new SearchMovieRowProvider(movieEntityList,SearchAbilitySlice.this,SearchAbilitySlice.this,listContainer));
                });
            }

            @Override
            public void onFailure(Call<ResultEntity> call, Throwable throwable) {

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
