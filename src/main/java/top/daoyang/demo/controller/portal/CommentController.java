package top.daoyang.demo.controller.portal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import top.daoyang.demo.payload.ServerResponse;
import top.daoyang.demo.service.CommentService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/{productId}")
    public ServerResponse getComment(@PathVariable Integer productId) {
        return ServerResponse.createBySuccess(commentService.getCommentTreeByProductId(productId));
    }

    @PostMapping
    public ServerResponse createComment(HttpServletRequest request, HttpServletResponse response) throws IOException {
        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;

        MultipartFile multipartFile = multipartHttpServletRequest.getFile("file");
        String realPath = "D:/";
        File dir = new File(realPath);
        File file = new File(realPath, "aa.jpg");
        multipartFile.transferTo(file);
        return null;
    }
}
