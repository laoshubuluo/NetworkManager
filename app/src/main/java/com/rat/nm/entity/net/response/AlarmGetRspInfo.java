package com.rat.nm.entity.net.response;

import com.rat.nm.entity.model.Alarm;
import com.rat.nm.entity.net.response.base.ResponseInfo;

/**
 * author : L.jinzhu
 * date : 2015/8/12
 * introduce : 响应实体
 */
public class AlarmGetRspInfo extends ResponseInfo {
    private Alarm alarm;

    public Alarm getAlarm() {
        return alarm;
    }

    public void setAlarm(Alarm alarm) {
        this.alarm = alarm;
    }
}