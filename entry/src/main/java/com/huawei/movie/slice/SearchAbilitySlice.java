package com.huawei.movie.slice;

import com.alibaba.fastjson.JSON;
import com.huawei.movie.ResourceTable;
import com.huawei.movie.config.Config;
import com.huawei.movie.entity.MovieEntity;
import com.huawei.movie.http.RequestUtils;
import com.huawei.movie.http.ResultEntity;
import com.huawei.movie.provider.SearchListProvider;
import com.huawei.movie.provider.SearchMovieRowProvider;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.ability.DataAbilityHelper;
import ohos.aafwk.ability.DataAbilityRemoteException;
import ohos.aafwk.content.Intent;
import ohos.agp.components.*;
import ohos.data.dataability.DataAbilityPredicates;
import ohos.data.rdb.ValuesBucket;
import ohos.data.resultset.ResultSet;
import ohos.utils.net.Uri;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SearchAbilitySlice extends AbilitySlice {
    MovieEntity movieEntity;
    ListContainer searchLstContainer;
    ListContainer recommendListContainer;
    DirectionalLayout searchRecordlayout;
    int pageSize = 20;
    int pageNum = 1;
    Image clearIcon;
    TextField textField;
    DataAbilityHelper creator;
    int total = 0;
    List<MovieEntity> searchResultList;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_search);
        String movieItem = getAbility().getIntent().getStringParam("movieItem");
        movieEntity = JSON.parseObject(movieItem, MovieEntity.class);
        getSearchRecord();
        setSearch();
        getRecommend();
        setClearClickListener();
        setScrolledListener();
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
                // 显示搜索记录
                searchRecordlayout.setVisibility(Component.VISIBLE);
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
            // 点击按钮，隐藏搜索记录
            searchRecordlayout.setVisibility(Component.HIDE);
            if("".equals(textField.getText()) || movieEntity.getMovieName().equals(textField.getText()) || textField.getText() == null){// 根据搜索框的标签进行搜索
                List<MovieEntity> movieEntityList = new ArrayList<>();
                movieEntityList.add(movieEntity);
                textField.setText(movieEntity.getMovieName());
                searchLstContainer.setVisibility(Component.VISIBLE);
                searchLstContainer.setItemProvider(new SearchListProvider(movieEntityList,SearchAbilitySlice.this,SearchAbilitySlice.this));
                insertRecord(movieEntity.getMovieName());
                recommendListContainer.setVisibility(Component.HIDE);
                total = 1;
            }else{
                // 根据关键字索索
                Call<ResultEntity> searchCall = RequestUtils.getInstance().search(null,null,null,null,null,textField.getText(),pageSize,pageNum);
                searchCall.enqueue(new Callback<ResultEntity>() {
                    @Override
                    public void onResponse(Call<ResultEntity> call, Response<ResultEntity> response) {
                        getContext().getUITaskDispatcher().asyncDispatch(()->{
                            ResultEntity body = response.body();
                            total = body.getTotal();
                            searchResultList = JSON.parseArray(JSON.toJSONString(body.getData()), MovieEntity.class);
                            searchLstContainer.setVisibility(Component.VISIBLE);
                            searchLstContainer.setItemProvider(new SearchListProvider(searchResultList,SearchAbilitySlice.this,SearchAbilitySlice.this));
                            insertRecord(textField.getText());
                        });

                    }

                    @Override
                    public void onFailure(Call<ResultEntity> call, Throwable throwable) {

                    }
                });
            }
        });

    }

    /**
     * @desc 请求按钮的点击事件
     * @since 2022-08-04
     */
    private void setClearClickListener(){
        clearIcon = (Image)findComponentById(ResourceTable.Id_search_icon_clear);
        clearIcon.setClickedListener(listener->{
            textField.setText(null);
            searchLstContainer.setVisibility(Component.HIDE);
            // 显示搜索记录
            searchRecordlayout.setVisibility(Component.VISIBLE);
            getSearchRecord();
            recommendListContainer.setVisibility(Component.VISIBLE);
        });
    }

    /**
     * @desc 监听滚动加载
     * @since 2022-08-04
     */
    private void setScrolledListener(){
        ScrollView scrollView = (ScrollView) findComponentById(ResourceTable.Id_search_scroll);
        scrollView.setScrolledListener((Component component, int i, int i1, int i2, int i3)->{
            if(i1 >= i3){ // 如果快滑到底部，加载更多
                if(total > pageSize * pageNum){
                    pageNum++;
                    Call<ResultEntity> searchCall = RequestUtils.getInstance().search(null,null,null,null,null,textField.getText(),pageSize,pageNum);
                    searchCall.enqueue(new Callback<ResultEntity>() {
                        @Override
                        public void onResponse(Call<ResultEntity> call, Response<ResultEntity> response) {
                            getContext().getUITaskDispatcher().asyncDispatch(()->{
                                ResultEntity body = response.body();
                                total = body.getTotal();
                                List<MovieEntity> movieEntityList = JSON.parseArray(JSON.toJSONString(body.getData()), MovieEntity.class);
                                searchResultList.addAll(movieEntityList);
                                searchLstContainer.setItemProvider(new SearchListProvider(searchResultList,SearchAbilitySlice.this,SearchAbilitySlice.this));
                            });
                        }

                        @Override
                        public void onFailure(Call<ResultEntity> call, Throwable throwable) {

                        }
                    });
                }
            }
        });
    }

    // 保存搜索记录
    private void insertRecord(String name){
        ValuesBucket value = new ValuesBucket();
        value.putString("name",name);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        value.putString("create_time",simpleDateFormat.format(new Date()));
        DataAbilityPredicates predicates = new DataAbilityPredicates();
        predicates.equalTo("name",name);
        Uri uri = Uri.parse(Config.searchUri);
        try {
            creator.delete(uri,predicates);
            creator.insert(uri,value);
        } catch (DataAbilityRemoteException e) {
            e.printStackTrace();
        }
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

    /**
     * @desc 获取搜索记录
     * @since 2022-07-25
     */
    private void getSearchRecord(){
        creator = DataAbilityHelper.creator(getContext());
        String[] columns = {"name"};
        DataAbilityPredicates predicates = new DataAbilityPredicates();
        predicates.orderByDesc("create_time");
        ResultSet resultSet = null;
        try {
            resultSet = creator.query(Uri.parse(Config.searchUri), columns, predicates);
        } catch (DataAbilityRemoteException e) {
            e.printStackTrace();
        }
        int length = resultSet.getRowCount();
        searchRecordlayout = (DirectionalLayout) findComponentById(ResourceTable.Id_search_record_layout);
        DirectionalLayout searchRecordLabellayout = (DirectionalLayout) searchRecordlayout.findComponentById(ResourceTable.Id_search_record_label_layout);
        searchRecordLabellayout.removeAllComponents();
        LayoutScatter layoutScatter = LayoutScatter.getInstance(SearchAbilitySlice.this);
        if(length > 0){
            resultSet.goToFirstRow();
            do {
                String keyword = resultSet.getString(0);
                Text searchLabel = (Text)layoutScatter.parse(ResourceTable.Layout_search_record_text, null, false);
                searchLabel.setText(keyword);
                searchRecordLabellayout.addComponent(searchLabel);
            }while (resultSet.goToNextRow());
        }else {
            Text noDataLabel = (Text)layoutScatter.parse(ResourceTable.Layout_search_record_no_data_text, null, false);
            searchRecordLabellayout.addComponent(noDataLabel);
        }
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
