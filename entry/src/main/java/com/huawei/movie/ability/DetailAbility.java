package com.huawei.movie.ability;

import com.huawei.movie.slice.DetailAbilitySlice;
import ohos.aafwk.ability.fraction.FractionAbility;
import ohos.aafwk.content.Intent;

public class DetailAbility extends FractionAbility {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(DetailAbilitySlice.class.getName());
    }
}
