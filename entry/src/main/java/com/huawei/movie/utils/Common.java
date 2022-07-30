package com.huawei.movie.utils;

import com.huawei.movie.ResourceTable;
import ohos.agp.components.AttrHelper;
import ohos.agp.components.DirectionalLayout;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.Text;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.window.dialog.ToastDialog;
import ohos.app.Context;
import ohos.global.resource.NotExistException;
import ohos.global.resource.WrongTypeException;

import java.io.IOException;
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
}
