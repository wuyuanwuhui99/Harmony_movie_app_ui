package com.huawei.movie.slice;

import com.alibaba.fastjson.JSON;
import com.huawei.movie.ResourceTable;
import com.huawei.movie.config.Config;
import com.huawei.movie.entity.MovieEntity;
import com.huawei.movie.entity.UserEntity;
import com.huawei.movie.http.RequestUtils;
import com.huawei.movie.http.ResultEntity;
import com.huawei.movie.provider.SearchListProvider;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.Text;
import ohos.global.resource.Element;
import ohos.global.resource.NotExistException;
import ohos.global.resource.ResourceManager;
import ohos.global.resource.WrongTypeException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.util.Map;

public class EditAbilitySlice extends AbilitySlice {
    Map map;
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_edit);
        map = JSON.parseObject( getAbility().getIntent().getStringParam("data"), Map.class);
        initUI();
        setSaveClickListener();
    }

    /**
     * @desc 初始化UI
     * @since 2022-08-07
     * */
    private void initUI(){
        Text title = (Text) findComponentById(ResourceTable.Id_edit_title);
        title.setText((String) map.get("title"));
        Text value = (Text) findComponentById(ResourceTable.Id_edit_value);
        value.setText((String) map.get("value"));
    }

    /**
     * @desc 保存用户信息
     * @since 2022-08-07
     * */
    private void setSaveClickListener(){
        Button button = (Button) findComponentById(ResourceTable.Id_btn_save);
        button.setClickedListener(listner->{
            UserEntity userEntity = new UserEntity(Config.userEntity);
            String title = (String) map.get("title");
            Text valueText = (Text) findComponentById(ResourceTable.Id_edit_value);
            String value = valueText.getText();
            try {
                ResourceManager resourceManager = getResourceManager();
                String userName = resourceManager.getElement(ResourceTable.String_user_edit_user_name).getString();
                String tel = resourceManager.getElement(ResourceTable.String_user_edit_tel).getString();
                String email = resourceManager.getElement(ResourceTable.String_user_edit_email).getString();
                String birthday = resourceManager.getElement(ResourceTable.String_user_edit_birthday).getString();
                String sex = resourceManager.getElement(ResourceTable.String_user_edit_sex).getString();
                String sign = resourceManager.getElement(ResourceTable.String_user_edit_sign).getString();
                String region = resourceManager.getElement(ResourceTable.String_user_edit_region).getString();

                if(userName.equals(title)){
                    userEntity.setUsername(value);
                }else if(tel.equals(title)){
                    userEntity.setTelephone(value);
                }else if(email.equals(title)){
                    userEntity.setEmail(value);
                }else if(birthday.equals(title)){
                    userEntity.setBirthday(birthday);
                }else if(sex.equals(title)){
                    userEntity.setSex(birthday);
                }else if(sign.equals(title)){
                    userEntity.setSign(birthday);
                }else if(region.equals(title)){
                    userEntity.setRegion(birthday);
                }
                Call<ResultEntity> searchCall = RequestUtils.getInstance().updateUser(userEntity);
                searchCall.enqueue(new Callback<ResultEntity>() {
                    @Override
                    public void onResponse(Call<ResultEntity> call, Response<ResultEntity> response) {
                        getContext().getUITaskDispatcher().asyncDispatch(()->{
                            if(userName.equals(title)){
                                Config.userEntity.setUsername(value);
                            }else if(tel.equals(title)){
                                Config.userEntity.setTelephone(value);
                            }else if(email.equals(title)){
                                Config.userEntity.setEmail(value);
                            }else if(birthday.equals(title)){
                                Config.userEntity.setBirthday(birthday);
                            }else if(sex.equals(title)){
                                Config.userEntity.setSex(birthday);
                            }else if(sign.equals(title)){
                                Config.userEntity.setSign(birthday);
                            }else if(region.equals(title)){
                                Config.userEntity.setRegion(birthday);
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<ResultEntity> call, Throwable throwable) {

                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NotExistException e) {
                e.printStackTrace();
            } catch (WrongTypeException e) {
                e.printStackTrace();
            }
        });

    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }
}
