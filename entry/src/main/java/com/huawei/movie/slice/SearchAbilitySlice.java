package com.huawei.movie.slice;

import com.alibaba.fastjson.JSON;
import com.huawei.movie.ResourceTable;
import com.huawei.movie.entity.MovieEntity;
import com.huawei.movie.http.RequestUtils;
import com.huawei.movie.http.ResultEntity;
import com.huawei.movie.provider.SearchListProvider;
import com.huawei.movie.provider.SearchMovieRowProvider;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.*;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

public class SearchAbilitySlice extends AbilitySlice {
    MovieEntity movieEntity;
    ListContainer searchLstContainer;
    ListContainer recommendListContainer;
    int pageSize = 20;
    int pageNum = 1;
    Image clearIcon;
    TextField textField;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_search);
        String movieItem = getAbility().getIntent().getStringParam("movieItem");
        movieEntity = JSON.parseObject(movieItem, MovieEntity.class);
        setSearch();
        getRecommend();
    }

    /**
     * @desc 点击搜索影片
     * @since 2022-07-30
     */
    private void setSearch(){
        searchLstContainer = (ListContainer)findComponentById(ResourceTable.Id_search_result_list);
        textField = (TextField)findComponentById(ResourceTable.Id_search_input);
        textField.setHint(movieEntity.getMovieName());
        textField.setFocusChangedListener((Component component, boolean b)->{
            if(b){
                searchLstContainer.setVisibility(Component.HIDE);
            }else{
                searchLstContainer.setVisibility(Component.VISIBLE);
            }
            if(textField.getText() != null){
                clearIcon.setVisibility(Component.VISIBLE);
            }else{
                clearIcon.setVisibility(Component.HIDE);
            }
        });
        Button button = (Button)findComponentById(ResourceTable.Id_search_btn);
        button.setClickedListener(listener->{
            if("".equals(textField.getText()) || movieEntity.getMovieName().equals(textField.getText()) || textField.getText() == null){// 根据搜索框的标签进行搜索
                List<MovieEntity> movieEntityList = new ArrayList<>();
                movieEntityList.add(movieEntity);
                textField.setText(movieEntity.getMovieName());
                searchLstContainer.setVisibility(Component.VISIBLE);
                searchLstContainer.setItemProvider(new SearchListProvider(movieEntityList,SearchAbilitySlice.this,SearchAbilitySlice.this));
            }else{
                // 根据关键字索索
                Call<ResultEntity> searchCall = RequestUtils.getInstance().search(null,null,null,null,null,textField.getText(),pageSize,pageNum);
                searchCall.enqueue(new Callback<ResultEntity>() {
                    @Override
                    public void onResponse(Call<ResultEntity> call, Response<ResultEntity> response) {
                        getContext().getUITaskDispatcher().asyncDispatch(()->{
                            List<MovieEntity> movieEntityList = JSON.parseArray(JSON.toJSONString(response.body().getData()), MovieEntity.class);
                            searchLstContainer.setVisibility(Component.VISIBLE);
                            searchLstContainer.setItemProvider(new SearchListProvider(movieEntityList,SearchAbilitySlice.this,SearchAbilitySlice.this));
                        });

                    }

                    @Override
                    public void onFailure(Call<ResultEntity> call, Throwable throwable) {

                    }
                });
            }


        });
        clearIcon = (Image)findComponentById(ResourceTable.Id_search_icon_clear);
        clearIcon.setClickedListener(listener->{
            textField.setText(null);
            searchLstContainer.setVisibility(Component.HIDE);
        });
    }

    /**
     * @desc 获取推荐影片
     * @since 2022-07-25
     */
    private void getRecommend(){
        recommendListContainer = (ListContainer) findComponentById(ResourceTable.Id_search_movie_list);
        Call<ResultEntity> getRecommendCall = RequestUtils.getInstance().getRecommend(movieEntity.getClassify());
        getRecommendCall.enqueue(new Callback<ResultEntity>() {
            @Override
            public void onResponse(Call<ResultEntity> call, Response<ResultEntity> response) {
                List<MovieEntity> movieEntityList = JSON.parseArray(JSON.toJSONString(response.body().getData()), MovieEntity.class);
                getContext().getUITaskDispatcher().asyncDispatch(()->{
                    recommendListContainer.setItemProvider(new SearchMovieRowProvider(movieEntityList,SearchAbilitySlice.this,SearchAbilitySlice.this,recommendListContainer));
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
