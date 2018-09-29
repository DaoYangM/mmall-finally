package top.daoyang.demo.controller.portal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import top.daoyang.demo.payload.ServerResponse;
import top.daoyang.demo.payload.reponse.SubCommentResponse;
import top.daoyang.demo.payload.request.CommentCreateRequest;
import top.daoyang.demo.payload.request.CommentOrderCreateRequest;
import top.daoyang.demo.payload.request.SubCommentCreateRequest;
import top.daoyang.demo.security.WXUserDetails;
import top.daoyang.demo.service.CommentService;
import weixin.popular.bean.wxa.WxaDUserInfo;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/{productId}")
    public ServerResponse getComment(@PathVariable Integer productId,
                                     @RequestParam(value = "page", defaultValue = "1") Integer page,
                                     @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return ServerResponse.createBySuccess(commentService.getCommentTreeByProductId(productId, page, size));
    }

    @GetMapping("/detail/{commentId}")
    public ServerResponse getCommentDetail(@PathVariable Integer commentId) {
        return ServerResponse.createBySuccess(commentService.getCommentDetail(commentId));
    }

    @GetMapping("/sub/{commentId}")
    public ServerResponse getSubComment(@PathVariable Integer commentId) {
        return ServerResponse.createBySuccess(commentService.getSubCommentDetail(commentId));
    }

    @PostMapping("/sub")
    public ServerResponse createSubComment(@AuthenticationPrincipal WXUserDetails wxUserDetails,
                                           @Valid @RequestBody SubCommentCreateRequest subCommentCreateRequest) {
        return ServerResponse.createBySuccess(commentService.createSubComment(wxUserDetails.getId(), subCommentCreateRequest));
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

    @PostMapping("/order")
    public ServerResponse createCommentOrder(@AuthenticationPrincipal WXUserDetails wxUserDetails,
                                             @Valid @RequestBody CommentOrderCreateRequest commentOrderCreateRequest) {
        return ServerResponse.createBySuccess(commentService.createCommentOrder(wxUserDetails, commentOrderCreateRequest));
    }

    @PutMapping("/up/{commentId}")
    public ServerResponse upComment(@AuthenticationPrincipal WXUserDetails wxUserDetails,
                                    @PathVariable(value = "commentId") Integer commentId) {
        return ServerResponse.createBySuccess(commentService.upComment(wxUserDetails.getId(), commentId));
    }
}
