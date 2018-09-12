package top.daoyang.demo.controller.portal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.daoyang.demo.payload.ServerResponse;
import top.daoyang.demo.service.IndexService;

@RestController
@RequestMapping("/index")
public class IndexController {

    @Autowired
    private IndexService indexService;

    @GetMapping
    public ServerResponse getIndex() {
        return ServerResponse.createBySuccess(indexService.getIndex());
    }
}
