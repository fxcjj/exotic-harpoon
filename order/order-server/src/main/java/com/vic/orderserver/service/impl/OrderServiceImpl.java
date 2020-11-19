package com.vic.orderserver.service.impl;

import com.vic.bo.product.DecreaseStockInput;
import com.vic.bo.product.ProductInfoOutput;
import com.vic.dto.OrderDTO;
import com.vic.entity.order.OrderDetail;
import com.vic.entity.order.OrderMaster;
import com.vic.enums.order.OrderStatusEnum;
import com.vic.enums.order.PayStatusEnum;
import com.vic.feign.product.ProductClient;
import com.vic.orderserver.repository.OrderDetailRepository;
import com.vic.orderserver.repository.OrderMasterRepository;
import com.vic.orderserver.service.OrderService;
import com.vic.utils.KeyUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private OrderMasterRepository orderMasterRepository;

    @Autowired
    private ProductClient productClient;

    @Override
    public OrderDTO create(OrderDTO orderDTO) {

        //查询商品信息（调用商品服务）
        List<String> productIdList = orderDTO.getOrderDetailList().stream()
                .map(OrderDetail::getProductId)
                .collect(Collectors.toList());

        List<ProductInfoOutput> productInfoList = productClient.listForOrder(productIdList);


        String orderId = KeyUtil.genUniqueKey();

        //计算总价
        BigDecimal orderAmount = new BigDecimal(BigInteger.ZERO);
        List<OrderDetail> orderDetailList = orderDTO.getOrderDetailList();

        for (OrderDetail orderDetail : orderDetailList) {
            for (ProductInfoOutput productInfo : productInfoList) {
                if (StringUtils.equals(orderDetail.getProductId(), productInfo.getProductId())) {

                    //单价*数量
                    orderAmount = productInfo.getProductPrice()
                            .multiply(new BigDecimal(orderDetail.getProductQuantity()))
                            .add(orderAmount);
                    BeanUtils.copyProperties(productInfo, orderDetail);
                    orderDetail.setOrderId(orderId);
                    orderDetail.setDetailId(KeyUtil.genUniqueKey());

                    //订单详情入库
                    orderDetailRepository.save(orderDetail);
                }
            }
        }

        //扣库存（调用商品服务）
        List<DecreaseStockInput> cartDTOList = orderDTO.getOrderDetailList().stream()
                .map(e -> new DecreaseStockInput(e.getProductId(), e.getProductQuantity()))
                .collect(Collectors.toList());
        productClient.decreaseStock(cartDTOList);

        OrderMaster orderMaster = new OrderMaster();
        orderDTO.setOrderId(orderId);
        BeanUtils.copyProperties(orderDTO, orderMaster);
        orderMaster.setOrderAmount(orderAmount);
        orderMaster.setOrderStatus(OrderStatusEnum.NEW.getCode());
        orderMaster.setPayStatus(PayStatusEnum.WAIT.getCode());
        orderMasterRepository.save(orderMaster);
        return orderDTO;
    }
}
