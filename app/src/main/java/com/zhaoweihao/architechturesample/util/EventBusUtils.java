package com.zhaoweihao.architechturesample.util;

import org.greenrobot.eventbus.EventBus;

/**
 * EVentbus管理
 * @author Zhaoweihao
 */

public class EventBusUtils {
    /**
     * 发送简单的指定/通知
     */
    public static final int EVENT_SEND_INFORM = 1000;


    public static void register(Object object) {
        if (!EventBus.getDefault().isRegistered(object)) {
            EventBus.getDefault().register(object);
        }
    }

    public static void unregister(Object object) {
        if (EventBus.getDefault().isRegistered(object)) {
            EventBus.getDefault().unregister(object);
        }
    }


    public static <T> void sendEventMsg(int tag, T modle) {
        EventMessage eventMsg = new EventMessage();
        eventMsg.setTag(tag);
        eventMsg.setModle(modle);
        EventBus.getDefault().post(eventMsg);
    }

    public static <T> void sendEventMsg(int tag) {
        EventMessage eventMsg = new EventMessage();
        eventMsg.setTag(tag);
        EventBus.getDefault().post(eventMsg);
    }

    public static class EventMessage<T> {
        private Integer tag;
        private String type;
        private T modle;

        public int getTag() {
            if (null == tag) {
                return -1;
            } else {
                return tag.intValue();
            }
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setTag(Integer tag) {
            this.tag = tag;
        }

        public T getModle() {
            return modle;
        }

        public void setModle(T modle) {
            this.modle = modle;
        }
    }
}
