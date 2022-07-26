package com.huawei.movie.slice;

import com.alibaba.fastjson.JSON;
import com.huawei.movie.ResourceTable;
import com.huawei.movie.entity.MovieEntity;
import com.huawei.movie.entity.MovieUrlEntity;
import com.huawei.movie.http.RequestUtils;
import com.huawei.movie.http.ResultEntity;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.*;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

public class PlayAbilitySlice extends AbilitySlice {
    MovieEntity movieEntity;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_play);
        String movieItem = getAbility().getIntent().getStringParam("movieItem");
        movieEntity = JSON.parseObject(movieItem, MovieEntity.class);
        setMovieName();
        getMovieUrl();
        savePlayRecord();
    }

    /**
     * @desc 获取播放地址
     * @since 2022-07-25
     */
    private void getMovieUrl(){
//        Call<ResultEntity> getMovieUrlCall = RequestUtils.getInstance().getMovieUrl(movieEntity.getMovieId());
        Call<ResultEntity> getMovieUrlCall = RequestUtils.getInstance().getMovieUrl(20144L);
        getMovieUrlCall.enqueue(new Callback<ResultEntity>() {
            @Override
            public void onResponse(Call<ResultEntity> call, Response<ResultEntity> response) {
                List<MovieUrlEntity> movieUrlEntityList = JSON.parseArray(JSON.toJSONString(response.body().getData()),MovieUrlEntity.class);
                getContext().getUITaskDispatcher().asyncDispatch(()->{
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

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }
}
