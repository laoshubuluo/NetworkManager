package com.rat.nm.network.request;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.rat.nm.common.MessageSignConstant;
import com.rat.nm.common.ResponseConstant;
import com.rat.nm.common.WebConstant;
import com.rat.nm.entity.enums.DataGetType;
import com.rat.nm.entity.net.request.AlarmListGetActionInfo;
import com.rat.nm.entity.net.request.base.RequestInfo;
import com.rat.nm.entity.net.response.AlarmListGetRspInfo;
import com.rat.nm.network.request.base.PostJsonRequest;
import com.rat.nm.util.GsonUtil;
import com.rat.nm.util.LogUtil;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * author : L.jinzhu
 * date : 2015/9/11
 * introduce : 告警列表请求request
 */
public class AlarmListGetRequest extends PostJsonRequest {
    private DataGetType dataGetType;// 数据获取类型
    private int totalPage;
    private int currentPage;
    private String alarmType;
    private String timeStart;
    private String timeEnd;
    private String deviceId;

    public AlarmListGetRequest(Handler handler, Context context, int totalPage, int currentPage, String alarmType, String timeStart, String timeEnd, String deviceId, DataGetType dataGetType) {
        this.handler = handler;
        this.context = context;
        this.totalPage = totalPage;
        this.currentPage = currentPage;
        this.dataGetType = dataGetType;
        this.alarmType = alarmType;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.deviceId = deviceId;
        if (dataGetType.equals(DataGetType.UPDATE)) {
            this.totalPage = 0;
            this.currentPage = 0;
            this.dataGetType = DataGetType.UPDATE;
        } else
            this.dataGetType = dataGetType;
    }

    @Override
    protected String getParamsJson() {
        AlarmListGetActionInfo actionInfo = new AlarmListGetActionInfo(0, alarmType, timeStart, timeEnd, deviceId, totalPage, currentPage, dataGetType.getType());
        RequestInfo r = new RequestInfo(context, actionInfo);
        return GsonUtil.toJson(r);
    }

    @Override
    protected String getUrl() {
        return WebConstant.getServerUrl() + "alarm/list";
    }

    @Override
    protected String requestTag() {
        return "alarmListGet";
    }

    @Override
    protected void responseSuccess(JSONObject response) {
        Bundle b = new Bundle();
        Message msg = new Message();
        try {
            LogUtil.i("response success json: [" + requestTag() + "]: " + response.toString());
            AlarmListGetRspInfo info = GsonUtil.fromJson(response.toString(), AlarmListGetRspInfo.class);
            //响应正常
            if (ResponseConstant.SUCCESS == info.getCode()) {
                b.putInt("totalPage", info.getTotalPage());
                b.putInt("currentPage", currentPage);
                b.putString("dataGetType", dataGetType == null ? "" : dataGetType.getType());
                b.putSerializable("alarmList", (Serializable) info.getAlarmList());
                msg.what = MessageSignConstant.ALARM_LIST_GET_SUCCESS;
                msg.setData(b);
                handler.sendMessage(msg);
                LogUtil.i(requestTag() + " success");
            }
            //响应失败
            else {
                b.putInt("code", info.getCode());
                b.putString("message", info.getMessage());
                msg.what = MessageSignConstant.ALARM_LIST_GET_FAILURE;
                msg.setData(b);
                handler.sendMessage(msg);
                LogUtil.e(requestTag() + " failure: code: " + info.getCode() + ",message: " + info.getMessage(), null);
            }
        } catch (Throwable e) {
            handler.sendEmptyMessage(MessageSignConstant.UNKNOWN_ERROR);
            LogUtil.e(requestTag() + " error", e);
        }
    }
}