package com.huawei.movie.slice;

import com.alibaba.fastjson.JSON;
import com.huawei.movie.MyApplication;
import com.huawei.movie.ResourceTable;
import com.huawei.movie.ability.MovieMainAbility;
import com.huawei.movie.config.Config;
import com.huawei.movie.entity.UserEntity;
import com.huawei.movie.fraction.MovieHomeFraction;
import com.huawei.movie.fraction.MovieFraction;
import com.huawei.movie.fraction.MovieMyFraction;
import com.huawei.movie.fraction.MovieTvFraction;
import com.huawei.movie.http.RequestUtils;
import com.huawei.movie.http.ResultEntity;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.ability.DataAbilityHelper;
import ohos.aafwk.ability.DataAbilityRemoteException;
import ohos.aafwk.ability.fraction.FractionScheduler;
import ohos.aafwk.content.Intent;
import ohos.agp.components.*;
import ohos.agp.utils.Color;
import ohos.app.Context;
import ohos.bundle.ApplicationInfo;
import ohos.data.dataability.DataAbilityPredicates;
import ohos.data.rdb.ValuesBucket;
import ohos.data.resultset.ResultSet;
import ohos.utils.net.Uri;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import java.util.ArrayList;
import java.util.List;

public class MovieMainAbilitySlice extends AbilitySlice {
    List<Component>navDirectionalLayout = new ArrayList<>();// 澳航布局
    List<List<Component>> navComponents = new ArrayList<>();//底部导航栏
    List<List<Integer>> navResourceTables = new ArrayList<>();// 导航图标集合
    int currentTabIndex = 0;

    MovieHomeFraction movieHomeFraction;
    MovieFraction movieFraction;
    MovieMyFraction movieMyFraction;
    MovieTvFraction movieTvFraction;

    int oldCurrentIndex = 0;

    List<DirectionalLayout>tabDirectionalLayout = new ArrayList<>();// 四个滚动内容

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_movie_main);
        getUserData();
    }


    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }

    /**
     * @desc 初始化首页布局
     * @since 2022-06-25
     * */
    private void initUI(){
        navDirectionalLayout.add(findComponentById(ResourceTable.Id_home_dir));
        navDirectionalLayout.add(findComponentById(ResourceTable.Id_movie_dir));
        navDirectionalLayout.add(findComponentById(ResourceTable.Id_tv_dir));
        navDirectionalLayout.add(findComponentById(ResourceTable.Id_my_dir));

        // 首页图片和文字
        List<Component> homeComponents = new ArrayList<>();
        homeComponents.add(findComponentById(ResourceTable.Id_home_img));
        homeComponents.add(findComponentById(ResourceTable.Id_home_text));

        // 电影图片和文字
        List<Component>movieComponents = new ArrayList<>();
        movieComponents.add(findComponentById(ResourceTable.Id_movie_img));
        movieComponents.add(findComponentById(ResourceTable.Id_movie_text));

        // 电视剧图片和文字
        List<Component>tvComponents = new ArrayList<>();
        tvComponents.add(findComponentById(ResourceTable.Id_tv_img));
        tvComponents.add(findComponentById(ResourceTable.Id_tv_text));

        // 我的图片和文字
        List<Component>myComponents = new ArrayList<>();
        myComponents.add(findComponentById(ResourceTable.Id_my_img));
        myComponents.add(findComponentById(ResourceTable.Id_my_text));

        navComponents.add(homeComponents);
        navComponents.add(movieComponents);
        navComponents.add(tvComponents);
        navComponents.add(myComponents);

        // 所有切换图标

        List<Integer> homeResourceTables = new ArrayList<>();
        homeResourceTables.add(ResourceTable.Media_icon_home);
        homeResourceTables.add(ResourceTable.Media_icon_home_active);

        List<Integer> movieResourceTables = new ArrayList<>();
        movieResourceTables.add(ResourceTable.Media_icon_movie);
        movieResourceTables.add(ResourceTable.Media_icon_movie_active);

        List<Integer> tvResourceTables = new ArrayList<>();
        tvResourceTables.add(ResourceTable.Media_icon_tv);
        tvResourceTables.add(ResourceTable.Media_icon_tv_active);

        List<Integer> myResourceTables = new ArrayList<>();
        myResourceTables.add(ResourceTable.Media_icon_user);
        myResourceTables.add(ResourceTable.Media_icon_user_active);

        navResourceTables.add(homeResourceTables);
        navResourceTables.add(movieResourceTables);
        navResourceTables.add(tvResourceTables);
        navResourceTables.add(myResourceTables);

        tabDirectionalLayout.add((DirectionalLayout) findComponentById(ResourceTable.Id_home_layout));
        tabDirectionalLayout.add((DirectionalLayout) findComponentById(ResourceTable.Id_movie_layout));
        tabDirectionalLayout.add((DirectionalLayout) findComponentById(ResourceTable.Id_tv_layout));
        tabDirectionalLayout.add((DirectionalLayout) findComponentById(ResourceTable.Id_my_layout));
    }

    /**
     * @desc 循环绑定点击tab导航时间
     * @since 2022-06-25
     * */
    private void setListeners() {
        for (int i = 0; i < navDirectionalLayout.size();i++){
            int finalI = i;
            navDirectionalLayout.get(i).setClickedListener(component -> {
                // 清除上一个选中tab样式
                Image prevImage =  (Image)navComponents.get(currentTabIndex).get(0);
                Text prevText = (Text) navComponents.get(currentTabIndex).get(1);
                prevImage.setImageAndDecodeBounds(navResourceTables.get(currentTabIndex).get(0));
                prevText.setTextColor(new Color(getColor(ResourceTable.Color_buttom_nav_normal_color)));

                // 设置当前选中一个tab样式
                Image currentImage =  (Image)navComponents.get(finalI).get(0);
                Text currentText = (Text) navComponents.get(finalI).get(1);
                currentImage.setImageAndDecodeBounds(navResourceTables.get(finalI).get(1));
                    currentText.setTextColor(new Color(getColor(ResourceTable.Color_buttom_nav_active_color)));

                currentTabIndex = finalI;

                loadFraction(currentTabIndex);
            });
        }
    }

    /**
     * @desc 采用懒加载方式，按照下标加载Fraction，如果已经加载过了点击时不用在加载
     * @param type tab页的下标
     * @since 2022-06-25
     * */
    private void loadFraction(int type){
        //获取小部分的管理器
        MovieMainAbility mainAbility = (MovieMainAbility) getAbility();
        //获取FractionScheduler对象
        FractionScheduler scheduler=mainAbility.getFractionManager().startFractionScheduler();
        //建立FractionScheduler和Fraction之间的关系
        switch (type) {
            case 0 :{
                if(movieHomeFraction == null){
                    movieHomeFraction = new MovieHomeFraction();
                    scheduler.replace(ResourceTable.Id_home_layout,movieHomeFraction);
                }
                break;
            }
            case 1 :{
                if(movieFraction == null){
                    movieFraction = new MovieFraction();
                    scheduler.replace(ResourceTable.Id_movie_layout,movieFraction);
                }
                break;
            }
            case 2 :{
                if(movieTvFraction == null){
                    movieTvFraction = new MovieTvFraction();
                    scheduler.replace(ResourceTable.Id_tv_layout,movieTvFraction);
                }
                break;
            }
            case 3 :{
                if(movieMyFraction == null){
                    movieMyFraction = new MovieMyFraction();
                    scheduler.replace(ResourceTable.Id_my_layout,movieMyFraction);
                }
                break;
            }
        }
        scheduler.submit();
        tabDirectionalLayout.get(oldCurrentIndex).setVisibility(Component.HIDE);
        tabDirectionalLayout.get(type).setVisibility(Component.VISIBLE);
        oldCurrentIndex = type;
    }

    /**
     * @desc 获取用户数据
     * @since 2022-07-05
     * */
    private void getUserData() {
        DataAbilityHelper creator = DataAbilityHelper.creator(getContext());
        String[] columns = {"token"};
        DataAbilityPredicates predicates = new DataAbilityPredicates();
        ResultSet resultSet = null;
        try {
            resultSet = creator.query(Uri.parse(Config.tokenUri), columns, predicates);
        } catch (DataAbilityRemoteException e) {
            e.printStackTrace();
        }
        if(resultSet.getRowCount() > 0){
            resultSet.goToFirstRow();
            MyApplication.getInstance().setToken(resultSet.getString(0));
        };
        // 获取用户信息
        Call<ResultEntity> userDataCall = RequestUtils.getInstance().getUserData();
        userDataCall.enqueue(new Callback<ResultEntity>() {
            @Override
            public void onResponse(Call<ResultEntity> call, Response<ResultEntity> response) {
                MyApplication.getInstance().setUserEntity(JSON.parseObject(JSON.toJSONString(response.body().getData()),UserEntity.class));
                MyApplication.getInstance().setToken(response.body().getToken());
                ValuesBucket valuesBucket = new ValuesBucket();
                valuesBucket.putString("token",MyApplication.getInstance().getToken());
                // 保存token
                try {
                    creator.insert(Uri.parse(Config.tokenUri),valuesBucket);
                } catch (DataAbilityRemoteException e) {
                    e.printStackTrace();
                }
                // 初始化UI
                getContext().getUITaskDispatcher().asyncDispatch(()->{
                    initUI();
                    setListeners();
                    loadFraction(currentTabIndex);
                });
            }

            @Override
            public void onFailure(Call<ResultEntity> call, Throwable throwable) {
                System.out.println(throwable);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

}
