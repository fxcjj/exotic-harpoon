package com.vic.productserver.service;

import com.vic.DecreaseStockInput;
import com.vic.productserver.dataobject.ProductInfo;

import java.util.List;

public interface ProductService {

    /**
     * 查询所有上架的商品列表
     *
     * @return
     */
    List<ProductInfo> findUpAll();


    /**
     * 查询商品列表
     *
     * @param productIdList
     * @return
     */
    List<ProductInfo> findList(List<String> productIdList);

    /**
     * 扣库存
     *
     * @param cartDTOList
     */
    void decreaseStock(List<DecreaseStockInput> cartDTOList);

}
