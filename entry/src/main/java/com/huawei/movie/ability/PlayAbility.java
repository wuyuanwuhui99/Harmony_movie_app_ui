package com.huawei.movie.ability;

import com.huawei.movie.slice.PlayAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

public class PlayAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(PlayAbilitySlice.class.getName());
    }
}
