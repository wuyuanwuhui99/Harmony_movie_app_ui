package com.huawei.movie.utils;

import ohos.aafwk.ability.fraction.Fraction;
import ohos.agp.components.Image;
import ohos.media.image.ImageSource;
import ohos.media.image.PixelMap;
import ohos.media.image.common.PixelFormat;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class HttpRequest {
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
