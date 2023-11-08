package com.huawei.movie.ability;

import com.huawei.movie.slice.MoviePlayAbilitySlice;
import ohos.aafwk.ability.fraction.FractionAbility;
import ohos.aafwk.content.Intent;

public class MoviePlayAbility extends FractionAbility {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(MoviePlayAbilitySlice.class.getName());
    }
}
