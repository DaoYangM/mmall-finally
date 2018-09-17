package top.daoyang.demo.mapper;

import org.apache.ibatis.annotations.Param;
import top.daoyang.demo.entity.Category;

import java.util.List;

public interface CategoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Category record);

    int insertSelective(Category record);

    Category selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Category record);

    int updateByPrimaryKey(Category record);

    List<Category> findByParentId(Integer parentId);

    int findCategoryNameCount(String name);

    List<Category> findChildParallelCategoryByCategoryId(Integer categoryId);

    List<Category> searchByName(@Param("name") String name);
}