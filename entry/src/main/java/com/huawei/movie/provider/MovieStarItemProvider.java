package com.huawei.movie.provider;

import com.huawei.movie.ResourceTable;
import com.huawei.movie.config.Api;
import com.huawei.movie.entity.MovieStarEntity;
import com.huawei.movie.utils.Common;
import ohos.agp.components.*;
import ohos.app.Context;

import java.util.List;

public class MovieStarItemProvider extends BaseItemProvider  {

    List<MovieStarEntity>movieStarEntityList;
    Context context;

    public MovieStarItemProvider(List<MovieStarEntity>movieStarEntityList, Context context){
        this.movieStarEntityList = movieStarEntityList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return movieStarEntityList.size();
    }

    @Override
    public Object getItem(int i) {
        if (movieStarEntityList != null && i >= 0 && i < movieStarEntityList.size()){
            return movieStarEntityList.get(i);
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
        Text text = (Text) cpt.findComponentById(ResourceTable.Id_movie_name);
        text.setText(movieStarEntityList.get(i).getStarName());
        Common.setImages(context,image, Api.HOST + movieStarEntityList.get(i).getLocalImg(),ResourceTable.String_middle_border_radius_size);
        return cpt;
    }
}
