package com.huawei.movie.fraction;

import com.alibaba.fastjson.JSON;
import com.huawei.movie.ResourceTable;
import com.huawei.movie.entity.CategoyEntity;
import com.huawei.movie.entity.MovieEntity;
import com.huawei.movie.http.RequestUtils;
import com.huawei.movie.http.ResultEntity;
import com.huawei.movie.provider.MovieItemProvider;
import ohos.aafwk.ability.fraction.Fraction;
import ohos.aafwk.content.Intent;
import ohos.agp.components.*;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class MovieCategoryFraction extends Fraction {
    Component component;
    CategoyEntity categoyEntity;
    @Override
    protected Component onComponentAttached(LayoutScatter scatter, ComponentContainer container, Intent intent) {
        component = scatter.parse(ResourceTable.Layout_fraction_movie_category, container, false);
        Text text = (Text) component.findComponentById(ResourceTable.Id_category_title);
        text.setText(categoyEntity.getCategory());
        return component;
    }

    public MovieCategoryFraction(CategoyEntity categoyEntity){
        this.categoyEntity = categoyEntity;
    }

    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        getTopMovieList();
    }

    /**
     * @des 按照分类获取前20条电影数据
     * @since 2022-07-15
     * */
    private void getTopMovieList(){
        Call<ResultEntity> topMovieList = RequestUtils.getInstance().getTopMovieList(categoyEntity.getClassify(),categoyEntity.getCategory());
        topMovieList.enqueue(new Callback<ResultEntity>() {
            @Override
            public void onResponse(Call<ResultEntity> call, Response<ResultEntity> response) {
                List<MovieEntity> movieEntityList = JSON.parseArray(JSON.toJSONString(response.body().getData()), MovieEntity.class);
                getContext().getUITaskDispatcher().asyncDispatch(()->{
                    ListContainer listContainer = (ListContainer)component.findComponentById(ResourceTable.Id_list_container);
                    listContainer.setItemProvider(new MovieItemProvider(movieEntityList,getContext(),getFractionAbility()));
                });
            }

            @Override
            public void onFailure(Call<ResultEntity> call, Throwable throwable) {

            }
        });
    }
}
