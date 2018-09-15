package top.daoyang.demo.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Favorite {

    private Integer id;

    private Integer productId;

    private String userId;

    private Date createTime;

    private Date updateTime;


    public Favorite(String userId, Integer productId) {
        this.productId = productId;
        this.userId = userId;
    }

    public Favorite() {
    }
}
