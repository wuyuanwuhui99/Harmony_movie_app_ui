package com.huawei.movie.slice;

import com.alibaba.fastjson.JSON;
import com.huawei.movie.ResourceTable;
import com.huawei.movie.entity.MovieEntity;
import com.huawei.movie.entity.MovieUrlEntity;
import com.huawei.movie.http.RequestUtils;
import com.huawei.movie.http.ResultEntity;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Text;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        Call<ResultEntity> getMovieUrlCall = RequestUtils.getInstance().getMovieUrl(movieEntity.getMovieId());
        getMovieUrlCall.enqueue(new Callback<ResultEntity>() {
            @Override
            public void onResponse(Call<ResultEntity> call, Response<ResultEntity> response) {
                List<MovieUrlEntity> movieUrlEntityList = JSON.parseArray(JSON.toJSONString(response.body().getData()),MovieUrlEntity.class);
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
