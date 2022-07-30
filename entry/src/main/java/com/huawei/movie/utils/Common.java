package com.huawei.movie.utils;

import com.huawei.movie.ResourceTable;
import ohos.agp.components.*;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.window.dialog.ToastDialog;
import ohos.app.Context;
import ohos.global.resource.NotExistException;
import ohos.global.resource.WrongTypeException;
import ohos.media.image.ImageSource;
import ohos.media.image.PixelMap;
import ohos.media.image.common.PixelFormat;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;

public class Common {
    public final static void showToast(int resId, Context context){
        DirectionalLayout toastLayout = (DirectionalLayout) LayoutScatter.getInstance(context)
                .parse(ResourceTable.Layout_layout_toast, null, false);
        Text text = (Text) toastLayout.findComponentById(ResourceTable.Id_msg_toast);
        String toastText = null;
        try {
            toastText = context.getResourceManager()
                    .getElement(resId)
                    .getString();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NotExistException e) {
            e.printStackTrace();
        } catch (WrongTypeException e) {
            e.printStackTrace();
        }
        text.setText(toastText);
        new ToastDialog(context)
                .setContentCustomComponent(toastLayout)
                .setSize(DirectionalLayout.LayoutConfig.MATCH_CONTENT, DirectionalLayout.LayoutConfig.MATCH_CONTENT)
                .setAlignment(LayoutAlignment.CENTER)
                .show();
    }

    public final static int vp2px(Context context,int resId){
        String size;
        Float vp = null;
        try {
            size = context.getResourceManager().getElement(resId).getString();
            vp = Float.parseFloat(Pattern.compile("[^0-9]").matcher(size).replaceAll("").trim());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NotExistException e) {
            e.printStackTrace();
        } catch (WrongTypeException e) {
            e.printStackTrace();
        }
        return AttrHelper.vp2px(vp,AttrHelper.getDensity(context));
    }

    public final static int[] vp2px(Context context,int resId[]){
        String size;
        int result[] = new int[resId.length];
        try {
            for (int i = 0; i < resId.length; i++){
                size = context.getResourceManager().getElement(resId[i]).getString();
                float vp = Float.parseFloat(Pattern.compile("[^0-9]").matcher(size).replaceAll("").trim());
                result[i] = AttrHelper.vp2px(vp,AttrHelper.getDensity(context));
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (NotExistException e) {
            e.printStackTrace();
        } catch (WrongTypeException e) {
            e.printStackTrace();
        }
        return result;
    }

    public final static void setImages(Context context, Image imageView, String url, int resId) {
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
                context.getUITaskDispatcher().delayDispatch(()->{
                    imageView.setPixelMap(pixelMap);
                    if(resId != 0){
                        float size = Common.vp2px(context,resId);
                        imageView.setCornerRadius(size);
                    }
                    //释放位图
                    pixelMap.release();
                }, 0);
            }catch (IOException e) {
                e.printStackTrace();
            }

        }).start();
    }
}
