package com.huawei.movie.fraction;

import com.huawei.movie.ResourceTable;
import com.huawei.movie.config.Api;
import com.huawei.movie.config.Config;
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
    @Override
    protected Component onComponentAttached(LayoutScatter scatter, ComponentContainer container, Intent intent) {
        component = scatter.parse(ResourceTable.Layout_fraction_avater, container, false);
        //指定布局文件
        return component;
    }

    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        Image avaterImg = (Image) component.findComponentById(ResourceTable.Id_avater);
        HttpRequest.loadImageData(avaterImg, Api.HOST + Config.userEntity.getAvater(),this);
        avaterImg.setCornerRadius(100f);
    }
}
