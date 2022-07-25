package com.huawei.movie.slice;

import com.alibaba.fastjson.JSON;
import com.huawei.movie.ResourceTable;
import com.huawei.movie.config.Api;
import com.huawei.movie.config.Config;
import com.huawei.movie.entity.MovieEntity;
import com.huawei.movie.entity.MovieStarEntity;
import com.huawei.movie.http.RequestUtils;
import com.huawei.movie.http.ResultEntity;
import com.huawei.movie.provider.MovieItemProvider;
import com.huawei.movie.provider.MovieStarItemProvider;
import com.huawei.movie.utils.HttpRequest;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
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
        getYourLikes();
        getRecommend();
        saveViewRecord();
        setClickedListener();
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
     */
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

    /**
     * @desc 查询主演
     * @since 2022-07-25
     */
    private void getStarList(){
        Call<ResultEntity> starListCall = RequestUtils.getInstance().getStarList(movieEntity.getMovieId().toString());
        starListCall.enqueue(new Callback<ResultEntity>() {
            @Override
            public void onResponse(Call<ResultEntity> call, Response<ResultEntity> response) {
                List<MovieStarEntity> movieStarEntities = JSON.parseArray(JSON.toJSONString(response.body().getData()), MovieStarEntity.class);
                getContext().getUITaskDispatcher().asyncDispatch(()->{
                    ListContainer listContainer = (ListContainer) findComponentById(ResourceTable.Id_detail_star_list);
                   if(movieStarEntities.size()!=0){
                       listContainer.setItemProvider(new MovieStarItemProvider(movieStarEntities,getContext()));
                   }else{
                       findComponentById(ResourceTable.Id_star_layout).setVisibility(Component.HIDE);
                   }
                });
            }

            @Override
            public void onFailure(Call<ResultEntity> call, Throwable throwable) {

            }
        });
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
                    ListContainer listContainer = (ListContainer) findComponentById(ResourceTable.Id_detail_likes_list);
                    if(movieEntityList.size()!=0){
                        listContainer.setItemProvider(new MovieItemProvider(movieEntityList,getContext(),DetailAbilitySlice.this));
                    }else{
                        findComponentById(ResourceTable.Id_likes_layout).setVisibility(Component.HIDE);
                    }
                });
            }

            @Override
            public void onFailure(Call<ResultEntity> call, Throwable throwable) {

            }
        });
    }

    /**
     * @desc 获取推荐影片
     * @since 2022-07-25
     */
    private void getRecommend(){
        Call<ResultEntity> yourLikesCall = RequestUtils.getInstance().getRecommend(movieEntity.getClassify());
        yourLikesCall.enqueue(new Callback<ResultEntity>() {
            @Override
            public void onResponse(Call<ResultEntity> call, Response<ResultEntity> response) {
                List<MovieEntity> movieEntityList = JSON.parseArray(JSON.toJSONString(response.body().getData()), MovieEntity.class);
                getContext().getUITaskDispatcher().asyncDispatch(()->{
                    ListContainer listContainer = (ListContainer) findComponentById(ResourceTable.Id_detail_recommend_list);
                    if(movieEntityList.size()!=0){
                        listContainer.setItemProvider(new MovieItemProvider(movieEntityList,getContext(),DetailAbilitySlice.this));
                    }else{
                        findComponentById(ResourceTable.Id_recommend_layout).setVisibility(Component.HIDE);
                    }
                });
            }

            @Override
            public void onFailure(Call<ResultEntity> call, Throwable throwable) {

            }
        });
    }

    /**
     * @desc 保存浏览记录
     * @since 2022-07-25
     */
    private void saveViewRecord(){
        movieEntity.setCreateTime(null);
        movieEntity.setUpdateTime(null);
        Call<ResultEntity> saveViewRecordCall = RequestUtils.getInstance().saveViewRecord(movieEntity);
        saveViewRecordCall.enqueue(new Callback<ResultEntity>() {

            @Override
            public void onResponse(Call<ResultEntity> call, Response<ResultEntity> response) {
                System.out.println(response.body().getData());
            }

            @Override
            public void onFailure(Call<ResultEntity> call, Throwable throwable) {
                System.out.println(throwable);
            }
        });
    }

    /**
     * @desc 点击图片跳转播放页
     * @since 2022-07-25
     */
    private void setClickedListener(){
        if(movieEntity.getMovieId() == null)return;
        Component component = findComponentById(ResourceTable.Id_movie_img_layout);
        component.setClickedListener(list->{
            //跳转到哪个页面中（意图）
            Intent intent = new Intent();
            //包含了页面跳转的信息
            Operation operation = new Intent.OperationBuilder()
                    //要跳转到哪个设备上，如果传递一个空的内容，表示跳转到本机
                    .withDeviceId("")
                    //要跳转到哪个应用上，小括号里面可以写包名
                    .withBundleName("com.huawei.movie")
                    //要跳转的页面
                    .withAbilityName("com.huawei.movie.ability.PlayAbility")
                    //表示将上面的三个信息进行打包
                    .build();
            //把打包之后的operation设置到意图当中
            intent.setOperation(operation);
            intent.setParam("movieItem", JSON.toJSONString(movieEntity));
            startAbility(intent);
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
