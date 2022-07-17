package com.huawei.movie.config;

import com.huawei.movie.entity.UserEntity;

public class Config {
    public static String token = "";
    public static final String tokenUri = "dataability:///com.huawei.movie.ability.DataAbility/token";
    public static UserEntity userEntity;
    public static final Integer INTERVAL = 3000;// 轮播时长
    public static final String swiperrunner = "SwiperRunner";
    public static final float imgRadius = 20; // 图片圆角
    public static final int bigAvaterSize = 200;// 大头像
}
