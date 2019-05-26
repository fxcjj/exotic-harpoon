package com.vic.productserver.service;


import com.vic.productserver.dataobject.ProductCategory;

import java.util.List;

public interface ProductCategoryService {

    List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList);
}
