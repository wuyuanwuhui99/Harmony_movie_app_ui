package com.huawei.movie.provider;

import com.huawei.movie.ResourceTable;
import com.huawei.movie.config.Api;
import com.huawei.movie.entity.MovieEntity;
import com.huawei.movie.utils.Common;
import ohos.aafwk.ability.AbilitySlice;
import ohos.agp.components.*;
import ohos.app.Context;

import java.util.List;

public class SearchListProvider extends BaseItemProvider  {

    List<MovieEntity>movieEntityList;
    Context context;
    AbilitySlice abilitySlice;

    public SearchListProvider(List<MovieEntity>movieEntityList, Context context, AbilitySlice abilitySlice) {
        this.movieEntityList = movieEntityList;
        this.context = context;
        this.abilitySlice = abilitySlice;
    }

    @Override
    public int getCount() {
        return movieEntityList.size() == 0 ? 1 : movieEntityList.size();
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
        final DirectionalLayout row;
        if (component == null) {
            LayoutScatter layoutScatter = LayoutScatter.getInstance(context);
            if(movieEntityList.size() > 0){
                row = (DirectionalLayout)layoutScatter.parse(ResourceTable.Layout_search_list_row, null, false);
                Text movieName = (Text) row.findComponentById(ResourceTable.Id_search_item_movie_name);
                movieName.setText(movieEntityList.get(i).getMovieName());
                Text star = (Text) row.findComponentById(ResourceTable.Id_search_item_star);
                star.setText("主演：" + movieEntityList.get(i).getStar());
                Text director = (Text) row.findComponentById(ResourceTable.Id_search_item_director);
                director.setText("导演：" + movieEntityList.get(i).getDirector());
                Text category = (Text) row.findComponentById(ResourceTable.Id_search_item_category);
                category.setText("类型：" + movieEntityList.get(i).getCategory());
                Text releaseTime = (Text) row.findComponentById(ResourceTable.Id_search_item_release_time);
                releaseTime.setText("上映时间：" + movieEntityList.get(i).getReleaseTime());
                Image image = (Image)row.findComponentById(ResourceTable.Id_search_item_img);
                Common.setImages(context,image,Api.HOST + movieEntityList.get(i).getLocalImg(),ResourceTable.String_middle_border_radius_size);
            }else{
                row = (DirectionalLayout)layoutScatter.parse(ResourceTable.Layout_search_list_no_data, null, false);
            }
        }else{
            row = (DirectionalLayout)component;
        }
        if(i==0){
            row.setMarginTop(0);
        }
        return row;
    }
}
