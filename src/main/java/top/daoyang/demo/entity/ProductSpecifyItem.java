package top.daoyang.demo.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ProductSpecifyItem {

    private Integer id;

    private Integer specifyId;

    private String description;

    private Date createTime;

    private Date updateTime;
}
