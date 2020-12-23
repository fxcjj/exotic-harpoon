package com.vic.productserver.controller;

import com.alibaba.fastjson.JSON;
import com.vic.bo.product.DecreaseStockInput;
import com.vic.entity.product.ProductCategory;
import com.vic.entity.product.ProductInfo;
import com.vic.productserver.service.ProductCategoryService;
import com.vic.productserver.service.ProductService;
import com.vic.vo.ResultVo;
import com.vic.vo.product.ProductInfoVo;
import com.vic.vo.product.ProductVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author victor
 */
@Slf4j
@RestController
@RequestMapping("product")
public class ProductController {

    @Autowired
    ProductService productService;

    @Autowired
    ProductCategoryService categoryService;

    /**
     * 1. 查询所有上架的商品
     * 2. 获取类目type列表
     * 3. 从数据库查询类目
     * 4. 构造数据
     *
     * @return
     */
    @GetMapping("list")
    public ResultVo<List<ProductVo>> list(HttpServletRequest request) {

        /**
         * 当使用zuul时，api-gateway不设置sensitiveHeaders为空时，cookies传不过来
         */
        //1. 查询所有上架的商品
        List<ProductInfo> productInfoList = productService.findUpAll();

        //2. 获取类目type列表
        List<Integer> categoryTypeList = productInfoList.stream()
                .map(ProductInfo::getCategoryType)
                .collect(Collectors.toList());

        //3. 从数据库查询类目
        List<ProductCategory> categoryList = categoryService.findByCategoryTypeIn(categoryTypeList);

        //4. 构造数据
        List<ProductVo> productVoList = new ArrayList<>();
        for (ProductCategory c : categoryList) {
            ProductVo productVO = new ProductVo();
            productVO.setCategoryName(c.getCategoryName());
            productVO.setCategoryType(c.getCategoryType());

            List<ProductInfoVo> productInfoVoList = new ArrayList<>();
            for (ProductInfo p : productInfoList) {
                if (c.getCategoryType().intValue() == p.getCategoryType().intValue()) {
                    ProductInfoVo productInfoVO = new ProductInfoVo();
                    BeanUtils.copyProperties(p, productInfoVO);
                    productInfoVoList.add(productInfoVO);
                }
            }
            productVO.setProductInfoVoList(productInfoVoList);
            productVoList.add(productVO);
        }

        return ResultVo.success(productVoList);
    }

    /**
     * 查询商品列表服务
     *
     * @param productIdList
     * @return
     */
    @PostMapping("listForOrder")
    public List<ProductInfo> listForOrder(@RequestBody List<String> productIdList) {
        log.info("invoke listForOrder method start");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<ProductInfo> list = productService.findList(productIdList);
        log.info("invoke listForOrder method result:{}", JSON.toJSONString(list));
        return list;
    }


    @PostMapping("decreaseStock")
    public void decreaseStock(@RequestBody List<DecreaseStockInput> cartDTOList) {
        productService.decreaseStock(cartDTOList);
    }
}
