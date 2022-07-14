package com.huawei.movie.fraction;

import com.huawei.movie.ResourceTable;
import com.huawei.movie.config.Api;
import com.huawei.movie.config.Config;
import com.huawei.movie.utils.HttpRequest;
import ohos.aafwk.ability.fraction.Fraction;
import ohos.aafwk.content.Intent;
import ohos.agp.components.*;

public class CategoryFraction extends Fraction {
    Component component;
    String category;
    @Override
    protected Component onComponentAttached(LayoutScatter scatter, ComponentContainer container, Intent intent) {
        component = scatter.parse(ResourceTable.Layout_fraction_category, container, false);
        Text text = (Text) component.findComponentById(ResourceTable.Id_category_title);
        text.setText(category);
        return component;
    }

    public CategoryFraction(String category){
        this.category = category;
    }

    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);

    }
}
