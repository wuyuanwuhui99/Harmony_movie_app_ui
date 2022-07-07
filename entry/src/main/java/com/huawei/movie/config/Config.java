package com.huawei.movie.config;

import com.huawei.movie.entity.UserEntity;

public class Config {
    public static String token = "";
    public static final String tokenUri = "dataability:///com.huawei.movie.ability.DataAbility/token";
    public static UserEntity userEntity;
    public static final Integer INTERVAL = 3000;// 轮播时长
    public static final String swiperrunner = "SwiperRunner";
}
