package com.huawei.movie.slice;

import com.alibaba.fastjson.JSON;
import com.huawei.movie.ResourceTable;
import com.huawei.movie.components.CustomDialog;
import com.huawei.movie.components.DialogClickListener;
import com.huawei.movie.config.Api;
import com.huawei.movie.config.Config;
import com.huawei.movie.utils.Common;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.*;

import java.util.HashMap;
import java.util.Map;

public class UserAbilitySlice extends AbilitySlice {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_user);
        initUI();
        setClickListener();
        logout();
    }

    /**
     * @desc 初始化UI
     * @since 2022-08-06
     * */
    private void initUI(){
        Image avaterImg = (Image) findComponentById(ResourceTable.Id_user_edit_avater);
        Common.setImages(this,avaterImg, Api.PROXY + Config.userEntity.getAvater(),0);
        int i = Common.vp2px(this, ResourceTable.String_middel_avater_size);
        avaterImg.setCornerRadius(i*2);

        Text userName = (Text) findComponentById(ResourceTable.Id_user_edit_name_value);
        userName.setText(Config.userEntity.getUsername());

        Text tel = (Text)findComponentById(ResourceTable.Id_user_edit_tel_value);
        tel.setText(Config.userEntity.getTelephone());

        Text emial = (Text)findComponentById(ResourceTable.Id_user_edit_emial_value);
        emial.setText(Config.userEntity.getEmail());

        Text birthday = (Text)findComponentById(ResourceTable.Id_user_edit_birthday_value);
        birthday.setText(Config.userEntity.getBirthday());

        Text sex = (Text)findComponentById(ResourceTable.Id_user_edit_sex_value);
        sex.setText(Config.userEntity.getSex());

        Text sign = (Text)findComponentById(ResourceTable.Id_user_edit_sign_value);
        sign.setText(Config.userEntity.getSign());

        Text region = (Text)findComponentById(ResourceTable.Id_user_edit_region_value);
        region.setText(Config.userEntity.getRegion());
    }

    /**
     * @desc 设置点击事件
     * @since 2022-08-07
     * */
    private void setClickListener(){
        int layouts[] = new int[]{
                ResourceTable.Id_user_name_layout,
                ResourceTable.Id_user_tel_layout,
                ResourceTable.Id_user_email_layout,
                ResourceTable.Id_user_birthday_layout,
                ResourceTable.Id_user_sex_layout,
                ResourceTable.Id_user_sign_layout,
                ResourceTable.Id_user_region_layout,
        };
        int titles[] = new int[]{
                ResourceTable.Id_user_edit_name_title,
                ResourceTable.Id_user_edit_tel_title,
                ResourceTable.Id_user_edit_email_title,
                ResourceTable.Id_user_edit_birthday_title,
                ResourceTable.Id_user_edit_sex_title,
                ResourceTable.Id_user_edit_sign_title,
                ResourceTable.Id_user_edit_region_title,
        };
        int values[] = new int[]{
                ResourceTable.Id_user_edit_name_value,
                ResourceTable.Id_user_edit_tel_value,
                ResourceTable.Id_user_edit_emial_value,
                ResourceTable.Id_user_edit_birthday_value,
                ResourceTable.Id_user_edit_sex_value,
                ResourceTable.Id_user_edit_sign_value,
                ResourceTable.Id_user_edit_region_value,
        };
        for (int i = 0; i < layouts.length; i++){
            DirectionalLayout layoutItem = (DirectionalLayout)findComponentById(layouts[i]);
            Text title = (Text)findComponentById(titles[i]);
            Text vavlue = (Text)findComponentById(values[i]);
            layoutItem.setClickedListener(listener->{
                //跳转到哪个页面中（意图）
                Intent intent = new Intent();
                Map<String,String> map = new HashMap<>();
                map.put("title",title.getText());
                map.put("value",vavlue.getText());
                intent.setParam("data", JSON.toJSONString(map));
                present(new EditAbilitySlice(),intent);
            });
        }
    }

    /**
     * @desc 退出登录
     * @since 2022-08-08
     * */
    private void logout(){
        Button button = (Button) findComponentById(ResourceTable.Id_user_logout);
        button.setClickedListener(listener->{
            CustomDialog customDialog = new CustomDialog(this,ResourceTable.String_user_logout_tip);
            customDialog.setOnClickListener(new DialogClickListener() {
                @Override
                public void onSureListener() {
                    present(new LoginAbilitySlice(),new Intent());
                }

                @Override
                public void onCancelListener() {

                }
            });

        });
    }

    @Override
    public void onActive() {
        super.onActive();
        initUI();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }
}
