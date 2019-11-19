package com.wlkg.repository;

import com.wlkg.pojo.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
//用来添加文档（数据）
public interface GoodsRepository extends ElasticsearchRepository<Goods,Long> {
}
