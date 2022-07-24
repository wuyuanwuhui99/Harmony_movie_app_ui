package com.huawei.movie.slice;

import com.alibaba.fastjson.JSON;
import com.huawei.movie.ResourceTable;
import com.huawei.movie.config.Api;
import com.huawei.movie.config.Config;
import com.huawei.movie.entity.MovieEntity;
import com.huawei.movie.entity.MovieStarEntity;
import com.huawei.movie.http.RequestUtils;
import com.huawei.movie.http.ResultEntity;
import com.huawei.movie.provider.MovieStarItemProvider;
import com.huawei.movie.utils.HttpRequest;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.*;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class DetailAbilitySlice extends AbilitySlice {
    MovieEntity movieEntity;
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_detail);
        String movieItem = getAbility().getIntent().getStringParam("movieItem");
        movieEntity = JSON.parseObject(movieItem, MovieEntity.class);
        initUI();
        setScore();
        getStarList();
    }

    /**
     * @desc 初始化UI
     * @since 2022-07-23
     * */
    private void initUI(){
        Text movieName = (Text)findComponentById(ResourceTable.Id_detail_movie_name);
        movieName.setText(movieEntity.getMovieName());

        Text description = (Text)findComponentById(ResourceTable.Id_detail_description);
        if(movieEntity.getDescription() != null){
            description.setText(movieEntity.getDescription().trim());
        }else{
            description.setVisibility(Component.HIDE);
        }

        Text star = (Text)findComponentById(ResourceTable.Id_detail_star);
        star.setText(movieEntity.getStar());

        Image image = (Image)findComponentById(ResourceTable.Id_detail_movie_img);
        image.setCornerRadius(Config.imgRadius);
        HttpRequest.setImages(this,image, Api.PROXY + movieEntity.getLocalImg());

        Text plot = (Text)findComponentById(ResourceTable.Id_detail_plot);
        plot.setText(movieEntity.getPlot().trim());
    }

    /**
     * @desc 设置得分星星的数量
     * @since 2022-07-24
     * */
    private void setScore(){
        Double score = movieEntity.getScore();
        if(score == null){
            DirectionalLayout layout = (DirectionalLayout) findComponentById(ResourceTable.Id_detail_star_layout);
            layout.setVisibility(Component.HIDE);
            return;
        }
        Image starImag [] = {
            (Image) findComponentById(ResourceTable.Id_detail_start_1),
            (Image) findComponentById(ResourceTable.Id_detail_start_2),
            (Image) findComponentById(ResourceTable.Id_detail_start_3),
            (Image) findComponentById(ResourceTable.Id_detail_start_4),
            (Image) findComponentById(ResourceTable.Id_detail_start_5)
        };

        for (int i = 0;i < starImag.length; i++){
            if((i+1) * 2 < score){
                starImag[i].setImageAndDecodeBounds(ResourceTable.Media_icon_full_star);
            }else if((i+1) * 2 - 1 < score){
                starImag[i].setImageAndDecodeBounds(ResourceTable.Media_icon_half_star);
            }else {
                starImag[i].setImageAndDecodeBounds(ResourceTable.Media_icon_empty_star);
            }
        }

        Text scoreText = (Text)findComponentById(ResourceTable.Id_detail_score);
        scoreText.setText(score.toString());
    }

    private void getStarList(){
        Call<ResultEntity> starListCall = RequestUtils.getInstance().getStarList(movieEntity.getMovieId().toString());
        starListCall.enqueue(new Callback<ResultEntity>() {
            @Override
            public void onResponse(Call<ResultEntity> call, Response<ResultEntity> response) {
                List<MovieStarEntity> movieStarEntities = JSON.parseArray(JSON.toJSONString(response.body().getData()), MovieStarEntity.class);
                getContext().getUITaskDispatcher().asyncDispatch(()->{
                    ListContainer listContainer = (ListContainer) findComponentById(ResourceTable.Id_detail_star_list);
                   if(movieStarEntities.size()==0){
                       listContainer.setItemProvider(new MovieStarItemProvider(movieStarEntities,getContext()));
                   }else{
                       listContainer.setVisibility(Component.HIDE);
                   }
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
