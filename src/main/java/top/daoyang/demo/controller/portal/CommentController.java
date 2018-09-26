package top.daoyang.demo.controller.portal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import top.daoyang.demo.payload.ServerResponse;
import top.daoyang.demo.payload.request.CommentCreateRequest;
import top.daoyang.demo.security.WXUserDetails;
import top.daoyang.demo.service.CommentService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
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
    public ServerResponse createComment(@AuthenticationPrincipal WXUserDetails wxUserDetails,
                                        @Valid @RequestBody CommentCreateRequest commentCreateRequest)  {

        return ServerResponse.createBySuccess(commentService.createComment(wxUserDetails.getId(), commentCreateRequest));
    }


    @PostMapping("/img")
    public ServerResponse createComment(HttpServletRequest request,
                                        @AuthenticationPrincipal WXUserDetails wxUserDetails) throws IOException {

        return ServerResponse.createBySuccess(commentService.createCommentImg(request, wxUserDetails.getId()));

    }
}
