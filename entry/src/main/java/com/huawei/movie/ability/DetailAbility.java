package com.huawei.movie.ability;

import com.huawei.movie.slice.DetailAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

public class DetailAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(DetailAbilitySlice.class.getName());
    }
}
