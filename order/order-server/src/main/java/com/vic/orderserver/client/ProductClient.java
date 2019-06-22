package com.vic.orderserver.client;

//import org.springframework.cloud.openfeign.FeignClient;

/**
 * product应用的接口声明
 * 不应该在这里写,应该在product项目里面写client
 */
/*@FeignClient(name = "product")
public interface ProductClient {

    @GetMapping("/msg")
    String productMsg();

    @PostMapping("/product/listForOrder")
    List<ProductInfo> listForOrder(@RequestBody List<String> productIdList);

    @PostMapping("/product/decreaseStock")
    void decreaseStock(@RequestBody List<CartDTO> cartDTOList);


}*/
