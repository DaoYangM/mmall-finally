package top.daoyang.demo.mapper;

import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import top.daoyang.demo.entity.Index;

import java.util.List;

public interface IndexMapper {
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "image", column = "image")
    })

    @Select("SELECT id, image FROM mmall_index")
    List<Index> getIndex();
}
