package com.huawei.movie.ability;

import com.huawei.movie.slice.MovieDetailAbilitySlice;
import ohos.aafwk.ability.fraction.FractionAbility;
import ohos.aafwk.content.Intent;

public class MovieDetailAbility extends FractionAbility {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(MovieDetailAbilitySlice.class.getName());
    }
}
