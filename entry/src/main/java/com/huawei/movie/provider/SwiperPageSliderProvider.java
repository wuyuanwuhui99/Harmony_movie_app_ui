package com.huawei.movie.provider;

import com.huawei.movie.ResourceTable;
import com.huawei.movie.config.Api;
import com.huawei.movie.config.Config;
import com.huawei.movie.entity.MovieEntity;
import com.huawei.movie.utils.HttpRequest;
import ohos.aafwk.ability.fraction.Fraction;
import ohos.agp.components.*;
import ohos.app.Context;

import java.util.List;

public class SwiperPageSliderProvider extends PageSliderProvider {
    private List<MovieEntity> movieEntityList;
    private Context context;
    Fraction fraction;
    PageSlider pageSlider;
    @Override
    public int getCount() {
        return movieEntityList.size();
    }
    public SwiperPageSliderProvider(PageSlider pageSlider,List<MovieEntity> movieEntityList, Context context, Fraction fraction){
        this.fraction = fraction;
        this.context = context;
        this.movieEntityList = movieEntityList;
        this.pageSlider = pageSlider;
    }
    @Override
    public Object createPageInContainer(ComponentContainer componentContainer, int i) {
        LayoutScatter layoutScatter = LayoutScatter.getInstance(context);
        Image image = (Image)layoutScatter.parse(ResourceTable.Layout_banner_item, null, false);
        componentContainer.addComponent(image);
        return image;
//        HttpRequest.setImages(context,image,Api.PROXY + movieEntityList.get(i).getLocalImg());
    }

    @Override
    public void destroyPageFromContainer(ComponentContainer componentContainer, int i, Object o) {
        componentContainer.removeComponent((Component) o);
    }

    @Override
    public boolean isPageMatchToObject(Component component, Object o) {
        return true;
    }
}
