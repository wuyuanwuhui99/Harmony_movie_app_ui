package com.huawei.movie.ability;

import com.huawei.movie.slice.MovieSearchAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

public class MovieSearchAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(MovieSearchAbilitySlice.class.getName());
    }
}
