package com.huawei.movie.fraction;

import ohos.aafwk.ability.fraction.Fraction;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.LayoutScatter;
import com.huawei.movie.ResourceTable;

public class HomeFraction extends Fraction {
    Component rootComponent;
    @Override
    protected Component onComponentAttached(LayoutScatter scatter, ComponentContainer container, Intent intent) {
        if(rootComponent == null){
            rootComponent = scatter.parse(ResourceTable.Layout_fraction_home, container, false);
        }
        //指定布局文件
        return rootComponent;
    }

    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        getFractionAbility().getUITaskDispatcher().asyncDispatch(()-> {
            getFractionAbility().getFractionManager()
                .startFractionScheduler()
                .add(ResourceTable.Id_avater_wrapper, new AvaterFraction())
                .add(ResourceTable.Id_avater_wrapper,new SearchFraction())
                .submit();
        });
    }
}
