package top.daoyang.demo.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.daoyang.demo.entity.Category;
import top.daoyang.demo.enums.ExceptionEnum;
import top.daoyang.demo.exception.CategoryException;
import top.daoyang.demo.exception.CategoryExistedException;
import top.daoyang.demo.mapper.CategoryMapper;
import top.daoyang.demo.payload.request.CategoryCreateRequest;
import top.daoyang.demo.service.ManageCategoryService;
import top.daoyang.demo.util.TreeCategory;

import java.util.List;
import java.util.Optional;

@Service
public class ManageCategoryServiceImpl implements ManageCategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public List<Category> findByParentId(Integer parentId) {
        return categoryMapper.findByParentId(parentId);
    }

    @Override
    public Category findByCategoryId(Integer categoryId) {

        return categoryMapper.selectByPrimaryKey(categoryId);
    }

    @Override
    @Transactional
    public Category createCategory(CategoryCreateRequest categoryCreateRequest) {

        if (categoryMapper.findCategoryNameCount(categoryCreateRequest.getName()) > 0) {
            throw new CategoryExistedException(ExceptionEnum.CATEGORY_EXISTED);
        }

        Category category = new Category();
        BeanUtils.copyProperties(categoryCreateRequest, category);
        category.setParentId(Integer.parseInt(categoryCreateRequest.getParentId()));

        if (categoryMapper.insertSelective(category) == 1)
            return category;
        else {
            throw new CategoryException(ExceptionEnum.CATEGORY_CREATE_ERROR);
        }
    }

    @Override
    @Transactional
    public Category updateCategory(CategoryCreateRequest categoryCreateRequest, Integer categoryId) {

        Category category = Optional.of(categoryMapper.selectByPrimaryKey(categoryId)).
                orElseThrow(() -> new CategoryException(ExceptionEnum.CATEGORY_DOES_NOT_EXIST));
        category.setName(categoryCreateRequest.getName());
        category.setParentId(Integer.parseInt(categoryCreateRequest.getParentId()));

        if (categoryMapper.updateByPrimaryKeySelective(category) == 1)
            return category;
        else
            throw new CategoryException(ExceptionEnum.CATEGORY_UPDATE_ERROR);
    }

    @Override
    public List<Category> findChildParallelCategory(Integer categoryId) {
        return categoryMapper.findChildParallelCategoryByCategoryId(categoryId);
    }

    @Override
    public TreeCategory findChildDeptCategory(Integer categoryId) {
        TreeCategory treeCategory = new TreeCategory();
        deptChild(categoryId, treeCategory);

        return treeCategory;
    }

    private TreeCategory deptChild(Integer categoryId, TreeCategory treeCategoryList) {
        List<Category> cIds = categoryMapper.findChildParallelCategoryByCategoryId(categoryId);

        for (int i = 0; i < cIds.size(); i++) {
            Category category = cIds.get(i);
            treeCategoryList.getChildCategoryList().add(new TreeCategory());
            deptChild(category.getId(), treeCategoryList.getChildCategoryList().get(i));
        }
        treeCategoryList.setCategory(categoryMapper.selectByPrimaryKey(categoryId));
        return treeCategoryList;
    }
}
