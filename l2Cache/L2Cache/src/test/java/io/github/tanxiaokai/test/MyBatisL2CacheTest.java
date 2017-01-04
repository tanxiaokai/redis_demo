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
		
		System.out.println("=== 第一次输出列表，不使用Cache ===");
		Thread.sleep(2000);
		demoMapper.listAll();

		System.out.println("=== 第二次输出列表，使用Cache ===");
		Thread.sleep(2000);
		demoMapper.listAll();

		System.out.println("=== 插入新数据,清除Cache ===");
		Thread.sleep(2000);
		Demo demo = new Demo();
		demo.setCreateTime(new Date());
		demo.setValue(this.getClass().getClassLoader().getResource("").getPath() + "----" + System.currentTimeMillis() );
		demoMapper.insertDemo(demo);
		
		System.out.println("=== 第三次输出列表，不使用Cache ===");
		Thread.sleep(2000);
		System.out.println(demoMapper.listAll());
		
		demoMapper.insertDemo(demo);
	}

}