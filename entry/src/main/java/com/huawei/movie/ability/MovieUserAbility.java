package com.huawei.movie.ability;

import com.huawei.movie.slice.MovieUserAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

public class MovieUserAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(MovieUserAbilitySlice.class.getName());
    }
}
