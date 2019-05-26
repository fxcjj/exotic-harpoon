package com.vic.productserver.service.impl;

import com.alibaba.fastjson.JSON;
import com.vic.DecreaseStockInput;
import com.vic.ProductInfoOutput;
import com.vic.productserver.dataobject.ProductInfo;
import com.vic.productserver.enums.ProductStatusEnum;
import com.vic.productserver.enums.ResultEnum;
import com.vic.productserver.exception.ProductException;
import com.vic.productserver.repository.ProductInfoRepository;
import com.vic.productserver.service.ProductService;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductInfoRepository productInfoRepository;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Override
    public List<ProductInfo> findUpAll() {
        return productInfoRepository.findByProductStatus(ProductStatusEnum.UP.getCode());
    }

    @Override
    public List<ProductInfo> findList(List<String> productIdList) {
        return productInfoRepository.findByProductIdIn(productIdList);
    }

    @Override
    public void decreaseStock(List<DecreaseStockInput> cartDTOList) {
        List<ProductInfo> productInfoList = decreaseStockProduct(cartDTOList);
        List<ProductInfoOutput> list = productInfoList.stream().map(e -> {
            ProductInfoOutput o = new ProductInfoOutput();
            BeanUtils.copyProperties(e, o);
            return o;
        }).collect(Collectors.toList());
        amqpTemplate.convertAndSend("productInfo", JSON.toJSONString(list));
    }

    @Transactional
    public List<ProductInfo> decreaseStockProduct(List<DecreaseStockInput> cartDTOList) {
        List<ProductInfo> list = new ArrayList<ProductInfo>();

        for (DecreaseStockInput c : cartDTOList) {
            Optional<ProductInfo> optional = productInfoRepository.findById(c.getProductId());

            //判断商品是否存在
            if (!optional.isPresent()) {
                throw new ProductException(ResultEnum.PRODUCT_NOT_EXISTS);
            }

            //判断库存
            ProductInfo productInfo = optional.get();
            Integer redundant = productInfo.getProductStock() - c.getProductQuantity();
            if (redundant < 0) {
                throw new ProductException(ResultEnum.PRODUCT_STOCK_ERROR);
            }

            productInfo.setProductStock(redundant);
            productInfoRepository.save(productInfo);

            list.add(productInfo);
        }
        return list;
    }
}
