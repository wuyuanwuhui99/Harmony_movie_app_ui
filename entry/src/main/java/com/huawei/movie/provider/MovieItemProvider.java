package com.huawei.movie.provider;

import com.alibaba.fastjson.JSON;
import com.huawei.movie.ResourceTable;
import com.huawei.movie.ability.DetailAbility;
import com.huawei.movie.config.Api;
import com.huawei.movie.entity.MovieEntity;
import com.huawei.movie.utils.Common;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.ability.fraction.FractionAbility;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.*;
import ohos.app.Context;

import java.util.List;
import java.util.Set;

public class MovieItemProvider  extends BaseItemProvider  {

    List<MovieEntity>movieEntityList;
    Context context;
    FractionAbility fractionAbility;
    AbilitySlice abilitySlice;

    public MovieItemProvider(List<MovieEntity>movieEntityList, Context context, FractionAbility fractionAbility){
        this.movieEntityList = movieEntityList;
        this.context = context;
        this.fractionAbility = fractionAbility;
    }

    public MovieItemProvider(List<MovieEntity>movieEntityList, Context context, AbilitySlice abilitySlice){
        this.movieEntityList = movieEntityList;
        this.context = context;
        this.abilitySlice = abilitySlice;
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
        Text text = (Text) cpt.findComponentById(ResourceTable.Id_movie_name);
        text.setText(movieEntityList.get(i).getMovieName());
        Common.setImages(context,image, Api.PROXY + movieEntityList.get(i).getLocalImg(),ResourceTable.String_middle_border_radius_size);
        cpt.setClickedListener(ncomponent->{
            //跳转到哪个页面中（意图）
            Intent intent = new Intent();
            //包含了页面跳转的信息
            Operation operation = new Intent.OperationBuilder()
                    //要跳转到哪个设备上，如果传递一个空的内容，表示跳转到本机
                    .withDeviceId("")
                    //要跳转到哪个应用上，小括号里面可以写包名
                    .withBundleName(abilitySlice.getBundleName())
                    //要跳转的页面
                    .withAbilityName(DetailAbility.class.getName())
                    //表示将上面的三个信息进行打包
                    .build();
            //把打包之后的operation设置到意图当中
            intent.setOperation(operation);
            intent.setParam("movieItem", JSON.toJSONString(movieEntityList.get(i)));
            //跳转页面
            if(abilitySlice != null){
                abilitySlice.startAbility(intent);
            }else{
                fractionAbility.startAbility(intent);
            }
        });
        return cpt;
    }
}
