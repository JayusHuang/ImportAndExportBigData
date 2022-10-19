package com.jayus.lx.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.io.Serializable;
import java.sql.Date;

/**
 * @ Description 订单信息
 * @ Author jayus
 * @ Date 2022/10/19 10:13
 */
@Data
public class OrderInformation implements Serializable {
    @ExcelProperty(value = "id")
    private Long id;
    //月份
    @ExcelProperty(value = "月份")
    private Integer period;
    //金额
    @ExcelProperty(value = "金额")
    private Double amount;
    //用户名
    @ExcelProperty(value = "用户名")
    private String userName;
    //手机号码
    @ExcelProperty(value = "手机号码")
    private String phone;
    //创建时间
    @ExcelProperty(value = "创建时间")
    private Date created;
    //创建人
    @ExcelProperty(value = "创建人")
    private String creator;
    //修改时间
    @ExcelProperty(value = "修改时间")
    private Date modified;
    //修改人
    @ExcelProperty(value = "修改人")
    private String modifier;
}

