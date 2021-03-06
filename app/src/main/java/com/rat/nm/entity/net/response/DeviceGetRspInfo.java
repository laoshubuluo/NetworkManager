package com.rat.nm.entity.net.response;

import com.rat.nm.entity.model.Device;
import com.rat.nm.entity.net.response.base.ResponseInfo;

/**
 * author : L.jinzhu
 * date : 2015/8/12
 * introduce : 响应实体
 */
public class DeviceGetRspInfo extends ResponseInfo {
    private Device device;

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }
}