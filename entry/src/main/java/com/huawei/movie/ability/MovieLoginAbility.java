package com.huawei.movie.ability;

import com.huawei.movie.slice.MovieLoginAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

public class MovieLoginAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(MovieLoginAbilitySlice.class.getName());
    }
}
