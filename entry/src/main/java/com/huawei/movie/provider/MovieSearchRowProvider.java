package com.huawei.movie.provider;

import com.alibaba.fastjson.JSON;
import com.huawei.movie.ResourceTable;
import com.huawei.movie.config.Api;
import com.huawei.movie.entity.MovieEntity;
import com.huawei.movie.utils.Common;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.*;
import ohos.app.Context;

import java.util.List;

public class MovieSearchRowProvider extends BaseItemProvider  {

    List<MovieEntity>movieEntityList;
    Context context;
    AbilitySlice abilitySlice;
    int width;
    int height;

    public MovieSearchRowProvider(List<MovieEntity>movieEntityList, Context context, AbilitySlice abilitySlice, ListContainer listContainer) {
        this.movieEntityList = movieEntityList;
        this.context = context;
        this.abilitySlice = abilitySlice;
        int padding = listContainer.getPaddingLeft() + listContainer.getPaddingRight();
        int innerWidth = listContainer.getWidth() - padding;
        width = (innerWidth - padding)/3;
        height = (int) (width/0.72);// 图片高度和宽度按比例计算
    }

    @Override
    public int getCount() {
        return (int) Math.ceil(movieEntityList.size()/3);
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
            row = (DirectionalLayout)layoutScatter.parse(ResourceTable.Layout_movie_search_movie_row, null, false);
        }else{
            row = (DirectionalLayout)component;
        }
        if(i == 0){
            row.setMarginTop(0);
        }
        for(int j = 0; j < 3; j++){
            DirectionalLayout itemLayout = (DirectionalLayout)row.getComponentAt(j);
            if(i*3 +j < movieEntityList.size()){
                MovieEntity movieEntity = movieEntityList.get(i*3 +j);
                Image image = (Image)itemLayout.getComponentAt(0);
                image.setHeight(height);
                image.setWidth(width);
                Common.setImages(context,image,Api.HOST + movieEntity.getLocalImg(),ResourceTable.String_middle_border_radius_size);
                Text text = (Text)itemLayout.getComponentAt(1);
                text.setText(movieEntity.getMovieName());
                itemLayout.setClickedListener(listener->{
                    //跳转到哪个页面中（意图）
                    Intent intent = new Intent();
                    //包含了页面跳转的信息
                    Operation operation = new Intent.OperationBuilder()
                            //要跳转到哪个设备上，如果传递一个空的内容，表示跳转到本机
                            .withDeviceId("")
                            //要跳转到哪个应用上，小括号里面可以写包名
                            .withBundleName("com.huawei.movie")
                            //要跳转的页面
                            .withAbilityName("com.huawei.movie.ability.DetailAbility")
                            //表示将上面的三个信息进行打包
                            .build();
                    //把打包之后的operation设置到意图当中
                    intent.setOperation(operation);
                    intent.setParam("movieItem", JSON.toJSONString(movieEntity));
                    //跳转页面
                    abilitySlice.startAbility(intent);
                });
            }else{
                itemLayout.setVisibility(Component.INVISIBLE);
            }
        }
        return row;
    }
}
