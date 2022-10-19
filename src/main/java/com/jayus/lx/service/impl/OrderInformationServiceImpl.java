package com.jayus.lx.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayus.lx.entity.OrderInformation;
import com.jayus.lx.mapper.OrderInformationMapper;
import com.jayus.lx.service.OrderInformationService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ Description
 * @ Author jayus
 * @ Date 2022/10/19 10:43
 */
@Service
public class OrderInformationServiceImpl extends ServiceImpl<OrderInformationMapper, OrderInformation> implements OrderInformationService {

    public int getRecordCount(){
        return count();
    }

    @Override
    public List<OrderInformation> getByPage(Integer index,Integer size) {
        //1.创建IPage实例
        if (index==null || index <= 0){
            index = 0;
        }
        IPage<OrderInformation> iPage=new Page<>(index,size);
        IPage<OrderInformation> page = page(iPage);
        return page.getRecords();
    }

}

