package top.daoyang.demo.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ProductParamter {

    private Integer id;

    private Integer productId;

    private String parameterName;

    private String parameterValue;

    private Date createTime;

    private Date updateTime;
}
