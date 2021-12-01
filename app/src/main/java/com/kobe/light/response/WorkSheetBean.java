package com.kobe.light.response;

import java.io.Serializable;

public class WorkSheetBean implements Serializable {
    public String viewCode;//维修单号
    public String checkStatus;//维修状态


    public String field1Name;//所在道路
    public String field2Name;
    public String field3Name;
    public String field4Name;//灯杆名
    public String field5Name;
    public String billCode;

    public String billDate;//报修时间

    public String billCause;

    public String billRemarks;

    public String remarks;//维修方法

    public String baiduLongitude;//经度
    public String baiduLatitude;//纬度


}
