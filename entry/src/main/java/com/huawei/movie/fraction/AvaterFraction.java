package com.huawei.movie.fraction;

import com.bumptech.glide.Glide;
import com.huawei.movie.ResourceTable;
import com.huawei.movie.utils.HttpRequest;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.ability.fraction.Fraction;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.Image;
import ohos.agp.components.LayoutScatter;

public class AvaterFraction extends Fraction {
    Component component;
    AbilitySlice abilitySlice;
    @Override
    protected Component onComponentAttached(LayoutScatter scatter, ComponentContainer container, Intent intent) {
        component = scatter.parse(ResourceTable.Layout_fraction_avater, container, false);
        //指定布局文件
        return component;
    }

    public AvaterFraction(AbilitySlice abilitySlice){
        this.abilitySlice = abilitySlice;
    }

    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        Image avaterImg = (Image) component.findComponentById(ResourceTable.Id_avater);
        HttpRequest.loadImageData(avaterImg,"http://192.168.0.103:5001/static/user/avater/%E5%90%B4%E6%80%A8%E5%90%B4%E6%82%94.jpg",this);
    }
}
