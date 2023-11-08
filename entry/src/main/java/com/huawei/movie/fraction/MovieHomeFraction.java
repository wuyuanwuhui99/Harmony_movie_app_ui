package com.huawei.movie.fraction;

import com.alibaba.fastjson.JSON;
import com.huawei.movie.entity.CategoyEntity;
import com.huawei.movie.http.RequestUtils;
import com.huawei.movie.http.ResultEntity;
import ohos.aafwk.ability.fraction.Fraction;
import ohos.aafwk.ability.fraction.FractionScheduler;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.LayoutScatter;
import com.huawei.movie.ResourceTable;
import ohos.agp.components.ScrollView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class MovieHomeFraction extends Fraction {
    Component rootComponent;
    @Override
    protected Component onComponentAttached(LayoutScatter scatter, ComponentContainer container, Intent intent) {
        if(rootComponent == null){
            rootComponent = scatter.parse(ResourceTable.Layout_fraction_home, container, false);
        }
        //指定布局文件
        return rootComponent;
    }

    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        getFractionAbility().getUITaskDispatcher().asyncDispatch(()-> {
            getFractionAbility().getFractionManager()
                .startFractionScheduler()
                .add(ResourceTable.Id_home_avater_wrapper, new MovieAvaterFraction())
                .add(ResourceTable.Id_home_avater_wrapper,new MovieSearchFraction("电影"))
                .add(ResourceTable.Id_home_swriper_wrapper,new MovieSwiperFraction("电影"))
                .submit();
        });
        getAllCategoryListByPageName();
        setScroll();
    }

    /**
     * @des 获取首页所有分类电影
     * @since 2022-07-14
     * */
    private void getAllCategoryListByPageName(){
        Call<ResultEntity> allCategoryListByPageNameCall = RequestUtils.getInstance().getAllCategoryListByPageName("首页");
        allCategoryListByPageNameCall.enqueue(new Callback<ResultEntity>() {
            @Override
            public void onResponse(Call<ResultEntity> call, Response<ResultEntity> response) {
                List<CategoyEntity> categoyEntityList = JSON.parseArray(JSON.toJSONString(response.body().getData()), CategoyEntity.class);
                System.out.println(categoyEntityList);
                getContext().getUITaskDispatcher().asyncDispatch(()->{
                    getFractionAbility().getUITaskDispatcher().asyncDispatch(()-> {
                        FractionScheduler fractionScheduler = getFractionAbility().getFractionManager().startFractionScheduler();
                        for (CategoyEntity categoyEntity:categoyEntityList){
                            fractionScheduler.add(ResourceTable.Id_home_category_list,new MovieCategoryFraction(categoyEntity));
                        }
                        fractionScheduler.submit();
                    });
                });
            }

            @Override
            public void onFailure(Call<ResultEntity> call, Throwable throwable) {

            }
        });
    }

    private void setScroll(){
        ScrollView scrollView = (ScrollView)rootComponent.findComponentById(ResourceTable.Id_home_scroll_view);
        scrollView.addScrolledListener(new Component.ScrolledListener() {
            @Override
            public void onContentScrolled(Component component, int i, int i1, int i2, int i3) {
                System.out.println(scrollView.getHeight());
                System.out.println(i);
                System.out.println(i1);
                System.out.println(i2);
                System.out.println(i3);
            }
        });
    }
}
