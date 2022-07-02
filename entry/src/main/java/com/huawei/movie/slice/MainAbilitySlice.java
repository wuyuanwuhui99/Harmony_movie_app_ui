package com.huawei.movie.slice;

import com.huawei.movie.ResourceTable;
import com.huawei.movie.ability.MainAbility;
import com.huawei.movie.fraction.HomeFraction;
import com.huawei.movie.fraction.MovieFraction;
import com.huawei.movie.fraction.MyFraction;
import com.huawei.movie.fraction.TvFraction;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.ability.fraction.FractionScheduler;
import ohos.aafwk.content.Intent;
import ohos.agp.components.*;
import ohos.agp.utils.Color;
import java.util.ArrayList;
import java.util.List;

public class MainAbilitySlice extends AbilitySlice {
    List<Component>navDirectionalLayout = new ArrayList<>();// 澳航布局
    List<List<Component>> navComponents = new ArrayList<>();//底部导航栏
    List<List<Integer>> navResourceTables = new ArrayList<>();// 导航图标集合
    int currentTabIndex = 0;

    HomeFraction homeFraction;
    MovieFraction movieFraction;
    MyFraction myFraction;
    TvFraction tvFraction;

    int oldCurrentIndex = 0;

    List<DirectionalLayout>tabDirectionalLayout = new ArrayList<>();// 四个滚动内容

    StackLayout stackLayout;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);
        initUI();
        setListeners();
        loadFraction(currentTabIndex);
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }

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

    // 循环绑定点击时间
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

    private void loadFraction(int type){
        //获取小部分的管理器
        MainAbility mainAbility = (MainAbility) getAbility();
        //获取FractionScheduler对象
        FractionScheduler scheduler=mainAbility.getFractionManager().startFractionScheduler();
        //建立FractionScheduler和Fraction之间的关系
        switch (type) {
            case 0 :{
                if(homeFraction == null){
                    homeFraction = new HomeFraction(this);
                    scheduler.replace(ResourceTable.Id_home_layout,homeFraction);
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
                if(tvFraction == null){
                    tvFraction = new TvFraction();
                    scheduler.replace(ResourceTable.Id_tv_layout,tvFraction);
                }
                break;
            }
            case 3 :{
                if(myFraction == null){
                    myFraction = new MyFraction();
                    scheduler.replace(ResourceTable.Id_my_layout,myFraction);
                }
                break;
            }
        }
        scheduler.submit();
        tabDirectionalLayout.get(oldCurrentIndex).setVisibility(Component.HIDE);
        tabDirectionalLayout.get(type).setVisibility(Component.VISIBLE);
        oldCurrentIndex = type;
    }
}
