package com.huawei.movie.provider;

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
    @Override
    public int getCount() {
        return movieEntityList.size();
    }
    public SwiperPageSliderProvider(List<MovieEntity> movieEntityList, Context context, Fraction fraction){
        this.fraction = fraction;
        this.context = context;
        this.movieEntityList = movieEntityList;
    }
    @Override
    public Object createPageInContainer(ComponentContainer componentContainer, int i) {
        Image image = new Image(context);
        image.setLayoutConfig(
            new StackLayout.LayoutConfig(
                ComponentContainer.LayoutConfig.MATCH_PARENT,
                ComponentContainer.LayoutConfig.MATCH_PARENT
            )
        );
        image.setScaleMode(Image.ScaleMode.STRETCH);
        componentContainer.addComponent(image);
        HttpRequest.loadImageData(image,Api.PROXY + movieEntityList.get(i).getLocalImg(),fraction);
        return image;
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
