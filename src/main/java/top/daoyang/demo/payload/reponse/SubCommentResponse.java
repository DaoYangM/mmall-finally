package top.daoyang.demo.payload.reponse;

import lombok.Getter;
import lombok.Setter;
import top.daoyang.demo.entity.SubComment;

@Setter
@Getter
public class SubCommentResponse extends SubComment {

    private String FromNickName;

    private String FromAvatar;

    private String ToNickName;

    private String ToAvatar;
}
