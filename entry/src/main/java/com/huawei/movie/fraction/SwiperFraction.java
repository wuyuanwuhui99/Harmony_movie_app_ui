package com.huawei.movie.fraction;

import com.alibaba.fastjson.JSON;
import com.huawei.movie.ResourceTable;

import com.huawei.movie.config.Config;
import com.huawei.movie.entity.MovieEntity;
import com.huawei.movie.http.RequestUtils;
import com.huawei.movie.http.ResultEntity;
import com.huawei.movie.provider.SwiperPageSliderProvider;
import ohos.aafwk.ability.fraction.Fraction;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.PageSlider;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.eventhandler.InnerEvent;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import java.util.List;

public class SwiperFraction extends Fraction {
    Component component;
    String classify;
    PageSlider pageSlider;
    EventRunner eventRunner;
    private SwiperEventHandler handler;
    int currentIndex = 0;
    @Override
    protected Component onComponentAttached(LayoutScatter scatter, ComponentContainer container, Intent intent) {
        component = scatter.parse(ResourceTable.Layout_fraction_swiper, container, false);
        //指定布局文件
        return component;
    }

    public SwiperFraction(String classify){
        this.classify = classify;
    }

    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        getSwiperData();
    }

    /*
    * @desc 获取轮播数据
    * @since 2022-07-07
    * */
    private void getSwiperData(){
        Call<ResultEntity> swiperData = RequestUtils.getInstance().getCategoryList("轮播",classify);
        swiperData.enqueue(new Callback<ResultEntity>() {
            @Override
            public void onResponse(Call<ResultEntity> call, Response<ResultEntity> response) {
                List<MovieEntity> movieEntityList = JSON.parseArray(JSON.toJSONString(response.body().getData()), MovieEntity.class);
                 //初始化UI
                getContext().getUITaskDispatcher().asyncDispatch(()->{
                    pageSlider = (PageSlider) component.findComponentById(ResourceTable.Id_page_slider);//查找组件
                    SwiperPageSliderProvider swiperPageSliderProvider = new SwiperPageSliderProvider(pageSlider,movieEntityList, getContext(), SwiperFraction.this);
                    pageSlider.setProvider(swiperPageSliderProvider);
                    eventRunner = EventRunner.create(Config.swiperrunner);
                    // 初始化SwiperEventHandler
                    handler = new SwiperEventHandler(eventRunner);
                    InnerEvent normalInnerEvent = InnerEvent.get(0, 0, null);
                    handler.sendEvent(normalInnerEvent, Config.INTERVAL);//发送消息
                    pageSlider.addPageChangedListener(new PageSlider.PageChangedListener() {
                        @Override
                        public void onPageSliding(int i, float v, int i1) { }

                        @Override
                        public void onPageSlideStateChanged(int i) { }

                        @Override
                        public void onPageChosen(int i) {
                            currentIndex = i;
                        }
                    });
                });
            }

            @Override
            public void onFailure(Call<ResultEntity> call, Throwable throwable) {
                System.out.println(throwable);
            }
        });
    }

    private class SwiperEventHandler extends EventHandler {

        private SwiperEventHandler(EventRunner runner) {
            super(runner);
        }

        @Override
        public void processEvent(InnerEvent event) {
            getUITaskDispatcher().asyncDispatch(()-> {
                pageSlider.setCurrentPage(currentIndex+1);//让pageSlider滑到下一个
                InnerEvent normalInnerEvent = InnerEvent.get(0, 0, null);
                handler.sendEvent(normalInnerEvent, Config.INTERVAL);//Todo 隔一秒 发送一个消息
            });
        }
    }
}
