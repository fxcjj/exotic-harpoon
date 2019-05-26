package com.vic.orderserver.service;


import com.vic.orderserver.dto.OrderDTO;

public interface OrderService {

    /**
     * 创建订单
     *
     * @param orderDTO
     * @return
     */
    OrderDTO create(OrderDTO orderDTO);
}
