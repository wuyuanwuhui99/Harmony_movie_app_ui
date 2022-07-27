package com.huawei.movie.slice;

import com.alibaba.fastjson.JSON;
import com.huawei.movie.ResourceTable;
import com.huawei.movie.ability.PlayAbility;
import com.huawei.movie.entity.MovieEntity;
import com.huawei.movie.entity.MovieUrlEntity;
import com.huawei.movie.fraction.LikesFraction;
import com.huawei.movie.fraction.RecommenedFraction;
import com.huawei.movie.http.RequestUtils;
import com.huawei.movie.http.ResultEntity;
import com.huawei.movie.utils.Common;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.*;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.window.dialog.ToastDialog;
import ohos.global.resource.NotExistException;
import ohos.global.resource.WrongTypeException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlayAbilitySlice extends AbilitySlice {
    MovieEntity movieEntity;
    Boolean isFavorite = false;
    Image favoriteIcon;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_play);
        String movieItem = getAbility().getIntent().getStringParam("movieItem");
        favoriteIcon= (Image) findComponentById(ResourceTable.Id_play_icon_favorite);
        movieEntity = JSON.parseObject(movieItem, MovieEntity.class);
        setMovieName();
        getMovieUrl();
        savePlayRecord();
        initFraction();
        getCommentCount();
        getIsFavorite();
        addFavoriteClickListener();
    }

    /**
     * @desc 添加猜你喜欢和推荐模块
     * @since 2022-07-27
     */
    private void initFraction(){
        PlayAbility playAbility = (PlayAbility) getAbility();
        playAbility.getFractionManager().startFractionScheduler()
                .replace(ResourceTable.Id_play_likes_layout,new LikesFraction(movieEntity))
                .replace(ResourceTable.Id_play_recommend_layout,new RecommenedFraction(movieEntity))
                .submit();

    }

    /**
     * @desc 获取播放地址
     * @since 2022-07-25
     */
    private void getMovieUrl(){
        Call<ResultEntity> getMovieUrlCall = RequestUtils.getInstance().getMovieUrl(movieEntity.getMovieId());
        getMovieUrlCall.enqueue(new Callback<ResultEntity>() {
            @Override
            public void onResponse(Call<ResultEntity> call, Response<ResultEntity> response) {
                List<MovieUrlEntity> movieUrlEntityList = JSON.parseArray(JSON.toJSONString(response.body().getData()),MovieUrlEntity.class);
                getUITaskDispatcher().asyncDispatch(()->{
                    List<List<MovieUrlEntity>> movieUrlGroup = new ArrayList<>();
                    movieUrlEntityList.forEach(movieUrlEntity -> {
                        int group = movieUrlEntity.getPlayGroup()-1;
                        List<MovieUrlEntity> movieUrlEntities;
                        if(movieUrlGroup.size()<=group){
                            movieUrlEntities = new ArrayList<>();
                            movieUrlGroup.add(movieUrlEntities);
                        }else{
                            movieUrlEntities = movieUrlGroup.get(group);
                        }
                        movieUrlEntities.add(movieUrlEntity);
                    });
                    DirectionalLayout urlGroupLayout = (DirectionalLayout) findComponentById(ResourceTable.Id_play_url_group_layout);
                    LayoutScatter layoutScatter = LayoutScatter.getInstance(PlayAbilitySlice.this);
                    for (int i = 0; i < movieUrlGroup.size(); i++){
                        DirectionalLayout directionalLayout = (DirectionalLayout) layoutScatter.parse(ResourceTable.Layout_url_group, null, false);
                        int rowNum = (int) Math.ceil(movieUrlGroup.get(i).size()/5.0); // 行数
                        for(int j = 0; j < rowNum; j++){
                            DirectionalLayout row = (DirectionalLayout)layoutScatter.parse(ResourceTable.Layout_row_url, null, false);
                            if(j == 0)row.setMarginTop(0);
                            for(int k = 0; k < 5; k++){
                                Button button  = (Button) row.getComponentAt(k);
                                if(j*5 + k < movieUrlGroup.get(i).size()){
                                    if(k == 0)button.setMarginLeft(0);
                                    button.setText(movieUrlGroup.get(i).get(j*5 + k).getLabel());
                                }else{
                                    button.setVisibility(Component.INVISIBLE);
                                }
                            }
                            directionalLayout.addComponent(row);
                        }
                        urlGroupLayout.addComponent(directionalLayout);
                    }
                });
            }

            @Override
            public void onFailure(Call<ResultEntity> call, Throwable throwable) {

            }
        });
    }

    /**
     * @desc 保存播放记录
     * @since 2022-07-25
     */
    private void savePlayRecord(){
        movieEntity.setCreateTime(null);
        movieEntity.setUpdateTime(null);
        Call<ResultEntity> savePlayRecordCall = RequestUtils.getInstance().savePlayRecord(movieEntity);
        savePlayRecordCall.enqueue(new Callback<ResultEntity>() {
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
     * @desc 设置电影名称和主演
     * @since 2022-07-25
     */
    private void setMovieName(){
        Text movieName = (Text)findComponentById(ResourceTable.Id_play_movie_name);
        movieName.setText(movieEntity.getMovieName());
        Text star = (Text)findComponentById(ResourceTable.Id_play_star);
        star.setText(movieEntity.getStar());
    }

    /**
     * @desc 获取评论数量
     * @since 2022-07-27
     */
    private void getCommentCount(){
        Call<ResultEntity> getCommentCountCall = RequestUtils.getInstance().getCommentCount(movieEntity.getMovieId());
        getCommentCountCall.enqueue(new Callback<ResultEntity>() {

            @Override
            public void onResponse(Call<ResultEntity> call, Response<ResultEntity> response) {
                String commentCount = JSON.parseObject(JSON.toJSONString(response.body().getData()),String.class);
                getUITaskDispatcher().asyncDispatch(()->{
                    Text text = (Text) findComponentById(ResourceTable.Id_play_comment_count);
                    text.setText(commentCount);
                });
            }

            @Override
            public void onFailure(Call<ResultEntity> call, Throwable throwable) {

            }
        });
    }

    /**
     * @desc 获取是否收藏
     * @since 2022-07-27
     */
    private void getIsFavorite(){
        Call<ResultEntity> isFavoriteCall = RequestUtils.getInstance().isFavorite(movieEntity.getMovieId());
        isFavoriteCall.enqueue(new Callback<ResultEntity>() {

            @Override
            public void onResponse(Call<ResultEntity> call, Response<ResultEntity> response) {
                Long favorite = JSON.parseObject(JSON.toJSONString(response.body().getData()),Long.class);
                getUITaskDispatcher().asyncDispatch(()->{
                    if(favorite > 0){
                        isFavorite = true;
                        favoriteIcon.setImageAndDecodeBounds(ResourceTable.Media_icon_collection_active);
                    }
                });
            }

            @Override
            public void onFailure(Call<ResultEntity> call, Throwable throwable) {

            }
        });
    }

    /**
     * @desc 点击收藏和取消收藏时事件
     * @since 2022-07-27
     */
    private void addFavoriteClickListener(){
        favoriteIcon.setClickedListener(listener->{
            Call<ResultEntity> favoriteCall = null;
            if(isFavorite){
                favoriteCall = RequestUtils.getInstance().deleteFavorite(movieEntity.getMovieId());
            }else{
                favoriteCall = RequestUtils.getInstance().saveFavorite(movieEntity);
            }
            favoriteCall.enqueue(new Callback<ResultEntity>() {

                @Override
                public void onResponse(Call<ResultEntity> call, Response<ResultEntity> response) {
                    getUITaskDispatcher().asyncDispatch(()->{
                        // 如果是已经收藏过，点击之后取消收藏，反之则收藏
                        favoriteIcon.setImageAndDecodeBounds(isFavorite ? ResourceTable.Media_icon_collection : ResourceTable.Media_icon_collection_active);
                        isFavorite = !isFavorite;
                        Common.showToast(isFavorite ? ResourceTable.String_toast_add_favorite_text : ResourceTable.String_toast_delete_favorite_text,PlayAbilitySlice.this);
                    });
                }

                @Override
                public void onFailure(Call<ResultEntity> call, Throwable throwable) {

                }
            });
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
