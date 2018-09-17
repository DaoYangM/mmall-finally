package top.daoyang.demo.controller.portal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.daoyang.demo.payload.ServerResponse;
import top.daoyang.demo.service.ManageCategoryService;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private ManageCategoryService manageCategoryService;

    @GetMapping
    public ServerResponse searchCategory(@RequestParam(value = "q") String q ) {
        return ServerResponse.createBySuccess(manageCategoryService.searchCategory(q));
    }
}
