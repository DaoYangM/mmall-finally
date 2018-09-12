package top.daoyang.demo.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Index {
    private Integer id;

    private String image;

    private Date createTime;

    private Date updateTime;
}
