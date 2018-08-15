package top.daoyang.demo.service;

import top.daoyang.demo.entity.Category;
import top.daoyang.demo.payload.request.CategoryCreateRequest;
import top.daoyang.demo.util.TreeCategory;

import java.util.List;

public interface ManageCategoryService {

    List<Category> findByParentId(Integer parentId);

    Category findByCategoryId(Integer orElse);

    Category createCategory(CategoryCreateRequest categoryCreateRequest);

    Category updateCategory(CategoryCreateRequest categoryCreateRequest, Integer categoryId);

    List<Category> findChildParallelCategory(Integer categoryId);

    TreeCategory findChildDeptCategory(Integer categoryId);
}
