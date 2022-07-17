package com.huawei.movie.entity;

public class UserMsgEntity {
    private String userAge;
    private String favoriteCount;
    private String viewRecordCount;
    private String playRecordCount;

    public String getUserAge() {
        return userAge;
    }

    public void setUserAge(String userAge) {
        this.userAge = userAge;
    }

    public String getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(String favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public String getViewRecordCount() {
        return viewRecordCount;
    }

    public void setViewRecordCount(String viewRecordCount) {
        this.viewRecordCount = viewRecordCount;
    }

    public String getPlayRecordCount() {
        return playRecordCount;
    }

    public void setPlayRecordCount(String playRecordCount) {
        this.playRecordCount = playRecordCount;
    }
}
