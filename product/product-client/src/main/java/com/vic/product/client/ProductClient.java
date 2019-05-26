package com.vic.product.client;

import com.vic.DecreaseStockInput;
import com.vic.ProductInfoOutput;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * product应用的接口声明
 */
@FeignClient(name = "product")
public interface ProductClient {

    /**
     * 通过商品id列表获取商品列表
     *
     * @param productIdList
     * @return
     */
    @PostMapping("/product/listForOrder")
    List<ProductInfoOutput> listForOrder(@RequestBody List<String> productIdList);

    /**
     * 扣掉购物车里的库存
     *
     * @param cartDTOList
     */
    @PostMapping("/product/decreaseStock")
    void decreaseStock(@RequestBody List<DecreaseStockInput> cartDTOList);


}
