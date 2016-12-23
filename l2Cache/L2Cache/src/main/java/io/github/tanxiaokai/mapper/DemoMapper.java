package io.github.tanxiaokai.mapper;

import java.util.List;

import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import io.github.tanxiaokai.model.Demo;

@Repository
@CacheNamespace(implementation=(io.github.tanxiaokai.croe.MybatisRedisCache.class))
public interface DemoMapper {
	
	@Select("select * from demo")  
	public List<Demo> listAll();

	@Insert("insert into demo(value, create_time) values(#{value}, #{createTime})")  
    public void insertDemo(Demo demo);  
}
