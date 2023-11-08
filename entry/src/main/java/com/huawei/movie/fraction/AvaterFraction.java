package com.huawei.movie.fraction;

import com.huawei.movie.ResourceTable;
import com.huawei.movie.config.Api;
import com.huawei.movie.config.Config;
import com.huawei.movie.utils.Common;
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
        Common.setImages( getContext(),avaterImg,Api.HOST + Config.userEntity.getAvater(),ResourceTable.String_middel_avater_size);
        if("big".equals(size)){
            int resIds[] = new int[]{ResourceTable.String_middel_avater_size,ResourceTable.String_big_avater_size};
            int size[] = Common.vp2px(getContext(),resIds);
            avaterImg.setCornerRadius(size[0]);
            avaterImg.setHeight(size[1]);
            avaterImg.setWidth(size[1]);
        }
    }

    @Override
    public void onClick(Component component) {

    }
}
