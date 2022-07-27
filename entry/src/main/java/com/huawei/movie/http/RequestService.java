package com.huawei.movie.http;

import com.huawei.movie.config.Api;
import com.huawei.movie.entity.MovieEntity;
import retrofit2.Call;
import retrofit2.http.*;

public interface RequestService {

    // 获取用户信息
    @GET(Api.GETUSERDATA)
    Call<ResultEntity> getUserData();

    // 获取分类电影
    @GET(Api.GETCATEGORYLIST)
    Call<ResultEntity> getCategoryList(@Query("category")String category, @Query("classify")String classify);

    // 获取所有分类
    @GET(Api.GETALLCATEGORYLISTBYPAGENAME)
    Call<ResultEntity> getAllCategoryListByPageName(@Query("pageName")String pageName);

    // 获取推荐的电影
    @GET(Api.GETKEYWORD)
    Call<ResultEntity> getKeyWord(@Query("classify")String classify);

    // 获取用户4个指标
    @GET(Api.GETUSERMSG)
    Call<ResultEntity> getUserMsg();

    // 获取用户播放记录
    @GET(Api.GETPLAYRECORD)
    Call<ResultEntity> getPlayRecord();

    // 获取主演
    @GET(Api.GETSTAR)
    Call<ResultEntity> getStarList(@Path("movieId") String movieId);

    // 获取猜你喜欢
    @GET(Api.GETYOURLIKES)
    Call<ResultEntity> getYourLikes(@Query("labels") String labels,@Query("classify") String classify);

    // 获取推荐电影
    @GET(Api.GETRECOMMEND)
    Call<ResultEntity> getRecommend(@Query("classify") String classify);

    // 获取前20电影
    @GET(Api.GETTOPMOVIELIST)
    Call<ResultEntity> getTopMovieList(@Query("classify") String classify,@Query("category") String category);

    // 获取电影播放地址
    @GET(Api.GETMOVIEURL)
    Call<ResultEntity> getMovieUrl(@Query("movieId") Long movieId);

    // 保存浏览记录
    @POST(Api.SAVEVIEWRECORD)
    Call<ResultEntity> saveViewRecord(@Body MovieEntity movieEntity);

    // 保存浏览记录
    @POST(Api.SAVEPLAYRECORD)
    Call<ResultEntity> savePlayRecord(@Body MovieEntity movieEntity);

    // 查询评论数量
    @GET(Api.GETCOMMENTCOUNT)
    Call<ResultEntity> getCommentCount(@Query("movieId") long movieId);

    // 查询是否收藏
    @GET(Api.ISFAVORITE)
    Call<ResultEntity> isFavorite(@Query("movieId") long movieId);

    // 添加收藏
    @POST(Api.SAVEFAVORITE)
    Call<ResultEntity> saveFavorite(@Body MovieEntity movieEntity);

    // 添加收藏
    @DELETE(Api.DELETEFAVORITE)
    Call<ResultEntity> deleteFavorite(@Query("movieId") long movieId);

}
