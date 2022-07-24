package com.huawei.movie.slice;

import com.alibaba.fastjson.JSON;
import com.huawei.movie.ResourceTable;
import com.huawei.movie.config.Api;
import com.huawei.movie.config.Config;
import com.huawei.movie.entity.MovieEntity;
import com.huawei.movie.utils.HttpRequest;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.Image;
import ohos.agp.components.Text;

import java.io.Serializable;

public class DetailAbilitySlice extends AbilitySlice {
    MovieEntity movieEntity;
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_detail);
        String movieItem = getAbility().getIntent().getStringParam("movieItem");
        movieEntity = JSON.parseObject(movieItem, MovieEntity.class);
        initUI();
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

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }
}
