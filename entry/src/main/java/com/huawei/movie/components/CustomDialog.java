package com.huawei.movie.components;

import com.huawei.movie.ResourceTable;
import ohos.agp.components.Component;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.Text;
import ohos.agp.utils.TextAlignment;
import ohos.agp.window.dialog.CommonDialog;
import ohos.app.Context;
import ohos.global.resource.NotExistException;
import ohos.global.resource.WrongTypeException;
import ohos.multimodalinput.event.KeyEvent;

import java.io.IOException;

public class CustomDialog extends CommonDialog {
    private Component customComponent;
    private Context context;
    private int resId;
    private Component sureBtn;
    private Component cancelBtn;

    private DialogClickListener dialogClickListener;

    public CustomDialog(Context context, int resId) {
        super(context);
        this.context = context;
        this.resId = resId;
        initComponents();
        setAlignment(TextAlignment.CENTER);
        setTransparent(true);
        siteRemovable(true);
        setSwipeToDismiss(true);
        show();
    }

    private void initComponents() {
        customComponent = LayoutScatter.getInstance(context)
                .parse(ResourceTable.Layout_dialog_custom, null, false);
        Text tips = (Text) customComponent.findComponentById(ResourceTable.Id_dialog_tips);
        String txt = null;
        try {
            txt = context.getResourceManager().getElement(resId).getString();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NotExistException e) {
            e.printStackTrace();
        } catch (WrongTypeException e) {
            e.printStackTrace();
        }
        tips.setText(txt);
        sureBtn = customComponent.findComponentById(ResourceTable.Id_dialog_sure);
        cancelBtn = customComponent.findComponentById(ResourceTable.Id_dialog_cancle);
        super.setContentCustomComponent(customComponent);
        confirm();
    }
    private void confirm() {
        sureBtn.setClickedListener(component -> {
            dialogClickListener.onSureListener();
            destroy();
        });
        cancelBtn.setClickedListener(component -> {
            if (dialogClickListener != null) {
                dialogClickListener.onCancelListener();
                destroy();
            }
        });
    }

    public void setOnClickListener(DialogClickListener dialogClickListener) {
        this.dialogClickListener = dialogClickListener;
    }


    //拦截返回键
    @Override
    public boolean deliverKeyboardCase(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEY_BACK) {//拦截系统返回事件
            return true;
        }
        return super.deliverKeyboardCase(event);
    }
}
