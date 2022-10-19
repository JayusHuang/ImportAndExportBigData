package com.jayus.lx.util;

import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import org.springframework.stereotype.Component;
import com.alibaba.excel.converters.Converter;

import java.sql.Date;

/**
 * @ Description
 * @ Author jayus
 * @ Date 2022/10/19 16:05
 */
@Component
public class DateConverter implements Converter<Date> {
    /**
     * 开启对 Date 类型的支持
     */
    @Override
    public Class<?> supportJavaTypeKey() {
        return Date.class;
    }


    /**
     * 自定义对 Date 类型数据的处理
     * 这里就拿 String 去包装了下
     */
    @Override
    public WriteCellData<?> convertToExcelData(Date value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        return new WriteCellData<Date>(String.valueOf(value));
    }
}

