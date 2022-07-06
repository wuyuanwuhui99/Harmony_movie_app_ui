package com.huawei.movie.fraction;

import com.huawei.movie.ResourceTable;
import ohos.aafwk.ability.fraction.Fraction;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.LayoutScatter;

public class SearchFraction extends Fraction {
    Component component;
    @Override
    protected Component onComponentAttached(LayoutScatter scatter, ComponentContainer container, Intent intent) {
        component = scatter.parse(ResourceTable.Layout_fraction_search, container, false);
        //指定布局文件
        return component;
    }

    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
    }
}
