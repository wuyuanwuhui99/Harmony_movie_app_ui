package com.huawei.movie.provider;

import com.alibaba.fastjson.JSON;
import com.huawei.movie.ResourceTable;
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
            //????????????????????????????????????
            Intent intent = new Intent();
            //??????????????????????????????
            Operation operation = new Intent.OperationBuilder()
                    //????????????????????????????????????????????????????????????????????????????????????
                    .withDeviceId("")
                    //????????????????????????????????????????????????????????????
                    .withBundleName("com.huawei.movie")
                    //??????????????????
                    .withAbilityName("com.huawei.movie.ability.DetailAbility")
                    //??????????????????????????????????????????
                    .build();
            //??????????????????operation?????????????????????
            intent.setOperation(operation);
            intent.setParam("movieItem", JSON.toJSONString(movieEntityList.get(i)));
            //????????????
            if(abilitySlice != null){
                abilitySlice.startAbility(intent);
            }else{
                fractionAbility.startAbility(intent);
            }
        });
        return cpt;
    }
}
