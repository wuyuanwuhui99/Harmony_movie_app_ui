package com.huawei.movie.ability;
import com.huawei.movie.slice.MovieMainAbilitySlice;
import ohos.aafwk.ability.fraction.FractionAbility;
import ohos.aafwk.content.Intent;

public class MovieMainAbility extends FractionAbility {

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(MovieMainAbilitySlice.class.getName());
    }
}
