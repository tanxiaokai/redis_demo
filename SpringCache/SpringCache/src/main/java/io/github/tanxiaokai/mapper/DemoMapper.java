package io.github.tanxiaokai.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import io.github.tanxiaokai.model.Demo;

@Repository
public interface DemoMapper {
	
	@Select("select * from demo")  
	public List<Demo> listAll();

	@Select("select * from demo where id=#{id}")  
	public Demo findById(Integer id);
	
	@Insert("insert into demo(value, create_time) values(#{value}, #{createTime})")
	@Options(useGeneratedKeys = true, keyProperty = "id")
    public Boolean insertDemo(Demo demo);  
	
	@Update("update demo set value=#{value} where id=#{id}")  
    public Boolean updateDemo(@Param("id") Integer id, @Param("value") String value);

	@Delete("delete from demo where id=#{id}")  
    public Boolean deleteDemo(Integer id);
}
