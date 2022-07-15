package com.huawei.movie.provider;

import com.huawei.movie.ResourceTable;
import com.huawei.movie.config.Api;
import com.huawei.movie.config.Config;
import com.huawei.movie.entity.MovieEntity;
import com.huawei.movie.utils.HttpRequest;
import ohos.agp.components.*;
import ohos.app.Context;

import java.util.List;

public class MovieItemProvider  extends BaseItemProvider  {

    List<MovieEntity>movieEntityList;
    Context context;

    public MovieItemProvider(List<MovieEntity>movieEntityList, Context context){
        this.movieEntityList = movieEntityList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return movieEntityList.size();
    }

    @Override
    public Object getItem(int i) {
        if (movieEntityList != null && i >= 0 && i < movieEntityList.size()){
            return movieEntityList.get(i);
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public Component getComponent(int i, Component component, ComponentContainer componentContainer) {
        final Component cpt;
        if (component == null) {
            LayoutScatter layoutScatter = LayoutScatter.getInstance(context);
            cpt = layoutScatter.parse(ResourceTable.Layout_movie_item, null, false);
        }else{
            cpt = component;
        }
        Image image = (Image)cpt.findComponentById(ResourceTable.Id_movie_img);
        image.setCornerRadius(Config.imgRadius);
        Text text = (Text) cpt.findComponentById(ResourceTable.Id_movie_name);
        text.setText(movieEntityList.get(i).getMovieName());
        HttpRequest.setImages(context,image, Api.PROXY + movieEntityList.get(i).getLocalImg());
        return cpt;
    }
}
