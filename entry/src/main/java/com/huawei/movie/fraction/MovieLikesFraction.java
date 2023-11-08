package com.huawei.movie.fraction;

import com.alibaba.fastjson.JSON;
import com.huawei.movie.ResourceTable;
import com.huawei.movie.entity.MovieEntity;
import com.huawei.movie.http.RequestUtils;
import com.huawei.movie.http.ResultEntity;
import com.huawei.movie.provider.MovieItemProvider;
import ohos.aafwk.ability.fraction.Fraction;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.ListContainer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class MovieLikesFraction extends Fraction {
    Component component;
    String classify;
    MovieEntity movieEntity;
    @Override
    protected Component onComponentAttached(LayoutScatter scatter, ComponentContainer container, Intent intent) {
        component = scatter.parse(ResourceTable.Layout_fraction_movie_likes, container, false);
        //指定布局文件
        return component;
    }

    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        getYourLikes();
    }

    public MovieLikesFraction(MovieEntity movieEntity){
        this.movieEntity = movieEntity;
    }

    /**
     * @desc 猜你喜欢
     * @since 2022-07-25
     */
    private void getYourLikes(){
        Call<ResultEntity> yourLikesCall = RequestUtils.getInstance().getYourLikes(movieEntity.getLabel() != null ? movieEntity.getLabel() : movieEntity.getCategory(),movieEntity.getClassify());
        yourLikesCall.enqueue(new Callback<ResultEntity>() {
            @Override
            public void onResponse(Call<ResultEntity> call, Response<ResultEntity> response) {
                List<MovieEntity> movieEntityList = JSON.parseArray(JSON.toJSONString(response.body().getData()), MovieEntity.class);
                getContext().getUITaskDispatcher().asyncDispatch(()->{
                    ListContainer listContainer = (ListContainer) component.findComponentById(ResourceTable.Id_likes_list);
                    if(movieEntityList.size()!=0){
                        listContainer.setItemProvider(new MovieItemProvider(movieEntityList,getContext(), getFractionAbility()));
                    }else{
                        component.findComponentById(ResourceTable.Id_likes_layout).setVisibility(Component.HIDE);
                    }
                });
            }

            @Override
            public void onFailure(Call<ResultEntity> call, Throwable throwable) {

            }
        });
    }

}
