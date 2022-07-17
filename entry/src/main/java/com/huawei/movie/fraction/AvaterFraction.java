package com.huawei.movie.fraction;

import com.huawei.movie.ResourceTable;
import com.huawei.movie.config.Api;
import com.huawei.movie.config.Config;
import com.huawei.movie.utils.HttpRequest;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.ability.fraction.Fraction;
import ohos.aafwk.content.Intent;
import ohos.agp.components.*;

public class AvaterFraction extends Fraction implements Component.ClickedListener {
    Component component;
    String size;
    @Override
    protected Component onComponentAttached(LayoutScatter scatter, ComponentContainer container, Intent intent) {
        component = scatter.parse(ResourceTable.Layout_fraction_avater, container, false);
        //指定布局文件
        return component;
    }

    public AvaterFraction(String size){
        this.size = size;
    }

    public AvaterFraction(){

    }

    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        Image avaterImg = (Image) component.findComponentById(ResourceTable.Id_avater);
        HttpRequest.setImages( getContext(),avaterImg,Api.PROXY + Config.userEntity.getAvater());
        avaterImg.setCornerRadius(100f);
        if("big".equals(size)){
            avaterImg.setCornerRadius(Config.bigAvaterSize);
            avaterImg.setHeight(Config.bigAvaterSize);
            avaterImg.setWidth(Config.bigAvaterSize);
        }
    }

    @Override
    public void onClick(Component component) {

    }
}
