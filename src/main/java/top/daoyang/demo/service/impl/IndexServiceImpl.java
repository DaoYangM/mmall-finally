package top.daoyang.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.daoyang.demo.entity.Index;
import top.daoyang.demo.mapper.IndexMapper;
import top.daoyang.demo.service.IndexService;

import java.util.List;

@Service
public class IndexServiceImpl implements IndexService {

    @Autowired
    private IndexMapper indexMapper;

    @Override
    public List<Index> getIndex() {
        return indexMapper.getIndex();
    }
}
