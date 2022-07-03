package com.huawei.movie.http;

import com.huawei.movie.config.Api;
import poetry.jianjia.Call;
import poetry.jianjia.http.GET;
import poetry.jianjia.http.Path;
import poetry.jianjia.http.Query;

public interface RequestService {

    @GET(Api.GETUSERDATA)
    Call<ResultEntity> getUserData();

    @GET(Api.GETCATEGORYLIST)
    Call<ResultEntity> getCategoryList(@Query("category")String category, @Query("classify")String classify);

    @GET(Api.GETALLCATEGORYLISTBYPAGENAME)
    Call<ResultEntity> getAllCategoryListByPageName(@Query("pageName")String pageName);

    @GET(Api.GETKEYWORD)
    Call<ResultEntity> getKeyWord(@Query("classify")String classify);

    @GET(Api.GETUSERMSG)
    Call<ResultEntity> getUserMsg();

    @GET(Api.GETPLAYRECORD)
    Call<ResultEntity> getPlayRecord();

    @GET(Api.GETSTAR)
    Call<ResultEntity> getStarList(@Path("movieId") String movieId);

    @GET(Api.GETYOURLIKES)
    Call<ResultEntity> getYourLikes(@Query("labels") String labels,@Query("classify") String classify);

    @GET(Api.GETRECOMMEND)
    Call<ResultEntity> getRecommend(@Query("classify") String classify);

    @GET(Api.GETTOPMOVIELIST)
    Call<ResultEntity> getTopMovieList(@Query("classify") String classify,@Query("category") String category);

    @GET(Api.GETMOVIEURL)
    Call<ResultEntity> getMovieUrl(@Query("movieId") Long movieId);
}
