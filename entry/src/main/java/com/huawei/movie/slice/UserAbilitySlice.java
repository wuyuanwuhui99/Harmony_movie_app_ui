package com.huawei.movie.slice;

import com.huawei.movie.ResourceTable;
import com.huawei.movie.config.Api;
import com.huawei.movie.config.Config;
import com.huawei.movie.utils.Common;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Image;
import ohos.agp.components.Text;

public class UserAbilitySlice extends AbilitySlice {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_user);
        initUI();
    }

    private void initUI(){
        Image avaterImg = (Image) findComponentById(ResourceTable.Id_user_edit_avater);
        Common.setImages(this,avaterImg, Api.PROXY + Config.userEntity.getAvater(),0);
        int i = Common.vp2px(this, ResourceTable.String_middel_avater_size);
        avaterImg.setCornerRadius(i*2);

        Text userName = (Text) findComponentById(ResourceTable.Id_user_edit_user_name);
        userName.setText(Config.userEntity.getUsername());

        Text tel = (Text)findComponentById(ResourceTable.Id_user_edit_tel);
        tel.setText(Config.userEntity.getTelephone());

        Text emial = (Text)findComponentById(ResourceTable.Id_user_edit_emial);
        emial.setText(Config.userEntity.getEmail());

        Text birthday = (Text)findComponentById(ResourceTable.Id_user_edit_birthday);
        birthday.setText(Config.userEntity.getBirthday());

        Text sex = (Text)findComponentById(ResourceTable.Id_user_edit_sex);
        sex.setText(Config.userEntity.getSex());

        Text sign = (Text)findComponentById(ResourceTable.Id_user_edit_sign);
        sign.setText(Config.userEntity.getSign());

        Text region = (Text)findComponentById(ResourceTable.Id_user_edit_region);
        region.setText(Config.userEntity.getRegion());
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }
}
