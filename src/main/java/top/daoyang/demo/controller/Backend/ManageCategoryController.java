package top.daoyang.demo.controller.Backend;

import com.mysql.fabric.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.daoyang.demo.payload.ServerResponse;
import top.daoyang.demo.payload.request.CategoryCreateRequest;
import top.daoyang.demo.service.ManageCategoryService;

import javax.validation.Valid;

@RestController
@RequestMapping("/manage/category")
public class ManageCategoryController {

    @Autowired
    private ManageCategoryService manageCategoryService;

    @GetMapping("/parent")
    public ServerResponse getCategoryByParentIdDefault() {
        return ServerResponse.createBySuccess(manageCategoryService.findByParentId(0));
    }

    @GetMapping("/pchild/{categoryId}")
    public ServerResponse getChildParallelCategory(@PathVariable Integer categoryId) {
        return ServerResponse.createBySuccess(manageCategoryService.findChildParallelCategory(categoryId));
    }

    @GetMapping("/dchild/{categoryId}")
    public ServerResponse getChildDeptCategory(@PathVariable Integer categoryId) {
        return ServerResponse.createBySuccess(manageCategoryService.findChildDeptCategory(categoryId));
    }


    @GetMapping("/parent/{parentId}")
    public ServerResponse getCategoryByParentId(@PathVariable Integer parentId) {
        return ServerResponse.createBySuccess(manageCategoryService.findByParentId(parentId));
    }

    @GetMapping("/{categoryId}")
    public ServerResponse getCategoryByCategoryId(@PathVariable Integer categoryId) {
        return ServerResponse.createBySuccess(manageCategoryService.findByCategoryId(categoryId));
    }

    @PostMapping
    public ServerResponse createCategory(@Valid @RequestBody CategoryCreateRequest categoryCreateRequest) {
        return ServerResponse.createBySuccess(manageCategoryService.createCategory(categoryCreateRequest));
    }

    @PutMapping("/{categoryId}")
    public ServerResponse updateCategoryByCategoryId(@PathVariable Integer categoryId,
                                                     @Valid @RequestBody CategoryCreateRequest categoryCreateRequest) {
        return ServerResponse.createBySuccess(manageCategoryService.updateCategory(categoryCreateRequest, categoryId));
    }
}
