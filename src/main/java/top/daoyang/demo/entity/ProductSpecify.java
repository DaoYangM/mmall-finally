package top.daoyang.demo.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ProductSpecify {

    private Integer id;

    private String title;

    private Integer productId;

    private Date createTime;

    private Date updateTime;
}
