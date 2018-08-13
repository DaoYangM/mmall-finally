package top.daoyang.demo.utils;

import lombok.Getter;
import lombok.Setter;
import top.daoyang.demo.entity.Category;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TreeCategory {

    private Category category;

    private List<TreeCategory> childCategoryList = new ArrayList<>(5);

    public TreeCategory(Category category) {
        this.category = category;
    }

    public TreeCategory() {

    }
}
