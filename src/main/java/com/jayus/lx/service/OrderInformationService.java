package com.jayus.lx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jayus.lx.entity.OrderInformation;

import java.util.List;

/**
 * @Description
 * @Author jayus
 * @Date 2022/10/19 10:42
 */
public interface OrderInformationService extends IService<OrderInformation> {
    int getRecordCount();
    List<OrderInformation> getByPage(Integer index,Integer size);
}
