package com.huawei.movie.ability;

import com.huawei.movie.slice.PlayAbilitySlice;
import ohos.aafwk.ability.fraction.FractionAbility;
import ohos.aafwk.content.Intent;

public class PlayAbility extends FractionAbility {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(PlayAbilitySlice.class.getName());
    }
}
