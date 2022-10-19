package com.jayus.lx.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.jayus.lx.entity.OrderInformation;
import com.jayus.lx.service.OrderInformationService;
import com.jayus.lx.util.DateConverter;
import com.jayus.lx.util.DateUtil;
import com.jayus.lx.util.excel.ExcelWidthStyleStrategy;
import org.junit.jupiter.api.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @ Description
 * @ Author jayus
 * @ Date 2022/10/19 10:25
 */
@RestController
public class ExcelController {
    //每个sheet中写入条数
    public final static int SHEET_ROW_COUNT = 1000000;
    //每次写入条数
    public final static int WRITE_ROW_COUNT = 200000;

    @Autowired
    private OrderInformationService orderInformationService;

    @GetMapping("/download")
    public void dataExport300w(HttpServletResponse response) {
        {
            OutputStream outputStream = null;
            ExcelWriter writer;
            try {
                Date startTime = new Date();
                System.out.println("导出开始时间:" + DateUtil.formatDate(startTime));
                String fileName = new String(("export400wExcel.xlsx").getBytes(), StandardCharsets.UTF_8);
                outputStream = response.getOutputStream();
                writer = EasyExcelFactory.write(fileName)
                        .head(OrderInformation.class)
                        .registerWriteHandler(new ExcelWidthStyleStrategy())
                        .excelType(ExcelTypeEnum.XLSX)
                        .build();

                //记录总数,实际可以根据条件筛选
                int totalCount = orderInformationService.getRecordCount();
                System.out.println("总记录数：" + totalCount);
                //计算需要的Sheet数量
                int sheetNum = totalCount % SHEET_ROW_COUNT == 0 ? (totalCount / SHEET_ROW_COUNT) : (totalCount / SHEET_ROW_COUNT + 1);
                //计算一般情况下每一个Sheet需要写入的次数(一般情况不包含最后一个sheet,因为最后一个sheet不确定会写入多少条数据)
                int oneSheetWriteCount = SHEET_ROW_COUNT / WRITE_ROW_COUNT;
                //计算最后一个sheet需要写入的次数
                int lastSheetWriteCount = 0;
                if (sheetNum * SHEET_ROW_COUNT != totalCount) {
                    int tmp = totalCount % SHEET_ROW_COUNT;
                    lastSheetWriteCount = tmp % WRITE_ROW_COUNT == 0 ? tmp / WRITE_ROW_COUNT : (tmp / WRITE_ROW_COUNT) + 1;
                }
                //开始分批查询分次写入
                //注意这次的循环就需要进行嵌套循环了,外层循环是Sheet数目,内层循环是写入次数
                List<List<String>> dataList = new ArrayList<>();
                for (int i = 0; i < sheetNum; i++) {
                    Date sheetStartDate = new Date();
                    System.out.println("测试Sheet" + i + "开始任务:" + DateUtil.formatDate(sheetStartDate));
                    //创建Sheet
                    WriteSheet sheet = EasyExcelFactory.writerSheet("测试Sheet" + i).sheetNo(i).build();
                    //循环写入次数: j的自增条件是当不是最后一个Sheet的时候写入次数为正常的每个Sheet写入的次数,如果是最后一个就需要使用计算的次数lastSheetWriteCount
                    for (int j = 0; j < (i != (sheetNum - 1) ? oneSheetWriteCount : lastSheetWriteCount); j++) {
                        //集合复用,便于GC清理
                        dataList.clear();
                        //分页查询一次20w
                        int rang = i * SHEET_ROW_COUNT + j * WRITE_ROW_COUNT;
                        Date sheetPageStartDate = new Date();
                        System.out.println("测试Sheet" + i + "第" + j + "页开始查询:" + DateUtil.formatDate(sheetPageStartDate));
                        List<OrderInformation> resultList = orderInformationService.getByPage(j + 1 + oneSheetWriteCount * i, WRITE_ROW_COUNT);
                        if (!CollectionUtils.isEmpty(resultList)) {
                            resultList.forEach(item -> dataList.add(Arrays.asList(item.getId().toString(), item.getPeriod().toString(), item.getAmount().toString(), item.getUserName(), item.getPhone(), item.getCreated().toString(), item.getCreator(), item.getModified().toString(), item.getModifier())));
                        }
                        System.out.println("本次查询范围:" + rang + "~" + (rang + WRITE_ROW_COUNT));
                        Date sheetPageEndDate = new Date();
                        System.out.println("测试Sheet" + i + "第" + j + "页结束查询:" + DateUtil.formatDate(sheetPageEndDate));
                        System.out.println("测试Sheet" + i + "第" + j + "页所用时间:" + (sheetPageEndDate.getTime() - sheetPageStartDate.getTime()) / 1000 + "秒");
                        //写数据
                        writer.write(dataList, sheet);
                    }

                    Date sheetEndDate = new Date();
                    System.out.println("测试Sheet" + i + "结束任务:" + DateUtil.formatDate(sheetEndDate));
                    System.out.println("测试Sheet" + i + "所用时间:" + (sheetEndDate.getTime() - sheetStartDate.getTime()) / 1000 + "秒");
                }

                // 下载EXCEL
//                response.setHeader("Content-Disposition", "attachment;filename=" + new String((fileName).getBytes("gb2312"), StandardCharsets.ISO_8859_1));
//                response.setContentType("multipart/form-data");
//                response.setCharacterEncoding("utf-8");

                writer.finish();
                outputStream.flush();
                //导出时间结束
                Date endTime = new Date();
                System.out.println("导出结束时间:" + DateUtil.formatDate(endTime));
                System.out.println("导出所用时间:" + (endTime.getTime() - startTime.getTime()) / 1000 + "秒");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}


