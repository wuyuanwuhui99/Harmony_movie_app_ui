package com.huawei.movie.event;

import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.eventhandler.InnerEvent;

public class MyEventHandle extends EventHandler {

    public MyEventHandle(EventRunner runner) throws IllegalArgumentException {
        super(runner);
    }

    @Override

    protected void processEvent(InnerEvent event) {
        super.processEvent(event);
        //处理事件，由开发者撰写
        int evnetID=event.eventId;
    }

}
