package com.huawei.movie.ability;

import com.huawei.movie.slice.MovieEditAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

public class MovieEditAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(MovieEditAbilitySlice.class.getName());
    }
}
