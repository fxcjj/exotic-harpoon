package com.vic.orderserver.service;

import com.vic.dto.OrderDTO;

public interface OrderService {

    /**
     * 创建订单
     *
     * @param orderDTO
     * @return
     */
    OrderDTO create(OrderDTO orderDTO);
}
