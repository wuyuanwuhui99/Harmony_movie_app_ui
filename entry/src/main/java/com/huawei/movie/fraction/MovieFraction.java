package com.huawei.movie.fraction;

import com.huawei.movie.ResourceTable;
import ohos.aafwk.ability.fraction.Fraction;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.LayoutScatter;

public class MovieFraction extends Fraction {
    @Override
    protected Component onComponentAttached(LayoutScatter scatter, ComponentContainer container, Intent intent) {
        //指定布局文件
        return scatter.parse(ResourceTable.Layout_fraction_movie, container, false);
    }
}
