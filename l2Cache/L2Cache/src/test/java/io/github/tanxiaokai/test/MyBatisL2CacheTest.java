package io.github.tanxiaokai.test;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import io.github.tanxiaokai.mapper.DemoMapper;
import io.github.tanxiaokai.model.Demo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml"})
public class MyBatisL2CacheTest {

	@Autowired
	private DemoMapper demoMapper;

	/*
	 * 二级缓存测试
	 */
	@Test
	public void testCache2() throws InterruptedException {
		
		Thread.sleep(10000);
		try{
			System.out.println(demoMapper.listAll());
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		Thread.sleep(5000);
		Demo demo = new Demo();
		demo.setCreateTime(new Date());
		demo.setValue(this.getClass().getClassLoader().getResource("").getPath() + "----" + System.currentTimeMillis() );
		
		demoMapper.insertDemo(demo);

		Thread.sleep(5000);
		System.out.println(demoMapper.listAll());
	}

}