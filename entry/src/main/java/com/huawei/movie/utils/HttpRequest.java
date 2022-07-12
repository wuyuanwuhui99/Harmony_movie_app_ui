package com.huawei.movie.utils;

import com.bumptech.glide.util.LogUtil;
import ohos.aafwk.ability.fraction.Fraction;
import ohos.agp.components.Image;
import ohos.app.Context;
import ohos.media.image.ImageSource;
import ohos.media.image.PixelMap;
import ohos.media.image.common.PixelFormat;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class HttpRequest {
    public static void setImages(Context context, Image imageView, String url) {
        System.out.println(url);
        Request request =new Request.Builder().url(url).get().build();
        new Thread(() -> {
            OkHttpClient okHttpClient =new OkHttpClient();
            try {
                //异步网络请求
                Response execute = okHttpClient.newCall(request).execute();
                //获取流
                InputStream inputStream = execute.body().byteStream();
                //利用鸿蒙api将流解码为图片源
                ImageSource imageSource = ImageSource.create(inputStream, new ImageSource.SourceOptions());
                ImageSource.DecodingOptions decodingOptions =new ImageSource.DecodingOptions();
                decodingOptions.desiredPixelFormat = PixelFormat.ARGB_8888;
                //根据图片源创建位图
                PixelMap pixelMap = imageSource.createPixelmap(decodingOptions);
                //展示到组件上
                context.getUITaskDispatcher().delayDispatch(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setPixelMap(pixelMap);
                        //释放位图
                        pixelMap.release();
                    }
                }, 0);
            }catch (IOException e) {
                e.printStackTrace();
            }

        }).start();
    }

    public static final void loadImageData(Image img, String src, Fraction fraction){
        HttpURLConnection connection = null;
        try {
            URL url = new URL(src);
            URLConnection urlConnection =   url.openConnection();
            if (urlConnection instanceof   HttpURLConnection) {
                connection =   (HttpURLConnection) urlConnection;
            }
            if (connection != null) {
                connection.connect();
                // 之后可进行url的其他操作
                // 得到服务器返回过来的流对象
                InputStream inputStream =   urlConnection.getInputStream();
                ImageSource imageSource = ImageSource.create(inputStream,   new ImageSource.SourceOptions());
                ImageSource.DecodingOptions   decodingOptions = new ImageSource.DecodingOptions();
                decodingOptions.desiredPixelFormat   = PixelFormat.ARGB_8888;
                // 普通解码叠加旋转、缩放、裁剪
                PixelMap pixelMap = imageSource.createPixelmap(decodingOptions);
                // 普通解码
                fraction.getUITaskDispatcher().syncDispatch(()   -> {
                    img.setPixelMap(pixelMap);
                    pixelMap.release();
                });
            }
        }   catch (Exception e) {
            e.printStackTrace();
        }
    }
}
