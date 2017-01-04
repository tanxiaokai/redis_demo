package io.github.tanxiaokai.test;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import io.github.tanxiaokai.model.Demo;
import io.github.tanxiaokai.service.DemoService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml"})
public class RedisSpringCacheTest {

	@Autowired
	private DemoService demoService;

	/*
	 * 缓存测试
	 */
	@Test
	public void test() throws InterruptedException {

		System.out.println("=== 第一次输出列表，不使用Cache ===");
		Thread.sleep(2000);
		demoService.listAll();

		System.out.println("=== 第二次输出列表，使用Cache ===");
		Thread.sleep(2000);
		demoService.listAll();
		

		System.out.println("=== 插入新数据,清除Cache ===");
		Thread.sleep(2000);
		Demo demo = new Demo();
		demo.setCreateTime(new Date());
		demo.setValue(this.getClass().getClassLoader().getResource("").getPath() + "----" + System.currentTimeMillis() );
		demoService.addDemo(demo);

		
		Thread.sleep(2000);
		Demo demo2 = demoService.findById(demo.getId());
		System.out.println(">>FIND - " + demo.getId() + " >>>>> " + demo2);

		System.out.println("=== 第三次输出列表，不使用Cache ===");
		Thread.sleep(2000);
		demoService.listAll();
		
		System.out.println("=== 第四次输出列表，使用Cache ===");
		Thread.sleep(2000);
		demoService.listAll();

		System.out.println("=== 更新数据,清除Cache ===");
		Thread.sleep(2000);
		demoService.modifiedDemo(demo.getId(), "Modified --- " + demo.getValue());
		
		Thread.sleep(2000);
		demo2 = demoService.findById(demo.getId());
		System.out.println(">>FIND - " + demo.getId() + " >>>>> " + demo2);
		
		Thread.sleep(1000);
		System.out.println(">>DELETE>>>>> " + demoService.removeDemo(demo.getId()));

		
	}

}