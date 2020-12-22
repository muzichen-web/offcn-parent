package com.offcn.order.service.impl;

import com.offcn.dycommon.enums.OrderStatusEnumes;
import com.offcn.dycommon.response.AppResponse;
import com.offcn.order.mapper.TOrderMapper;
import com.offcn.order.po.TOrder;
import com.offcn.order.service.OrderService;
import com.offcn.order.service.ProjectServiceFeign;
import com.offcn.order.vo.req.OrderInfoSubmitVo;
import com.offcn.order.vo.resp.TReturn;
import com.offcn.utils.AppDateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private TOrderMapper orderMapper;

    @Autowired
    private ProjectServiceFeign projectServiceFeign;


    @Override
    public TOrder saveOrder(OrderInfoSubmitVo vo) {

        TOrder order = new TOrder();

        String accessToken = vo.getAccessToken();
        String memberId = redisTemplate.opsForValue().get(accessToken);
        order.setMemberid(Integer.parseInt(memberId));
        order.setProjectid(vo.getProjectid());
        order.setReturnid(vo.getReturnid());
        String orderNum = UUID.randomUUID().toString().replace("-", "");
        order.setOrdernum(orderNum);
        order.setCreatedate(AppDateUtils.getFormatTime());
        AppResponse<List<TReturn>> AppResponse = projectServiceFeign.returnInfo(vo.getProjectid());
        List<TReturn> tReturns = AppResponse.getData();
        TReturn tReturn = tReturns.get(0);
        Integer totalMoney = vo.getRtncount() * tReturn.getSupportmoney() + tReturn.getFreight();
        order.setMoney(totalMoney);
        order.setRtncount(vo.getRtncount());
        order.setStatus(OrderStatusEnumes.UNPAY.getCode()+"");
        order.setAddress(vo.getAddress());
        order.setInvoice(vo.getInvoice().toString());
        order.setInvoictitle(vo.getInvoictitle());
        order.setRemark(vo.getRemark());
        orderMapper.insertSelective(order);
        return order;
    }
}
