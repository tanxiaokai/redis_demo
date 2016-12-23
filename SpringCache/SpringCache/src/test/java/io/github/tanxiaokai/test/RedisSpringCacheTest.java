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
		
		Thread.sleep(1000);
		System.out.println(">>LIST1>>>>> " + demoService.listAll());
		
		Thread.sleep(1000);
		System.out.println(">>LIST2>>>>> " + demoService.listAll());
		

		Thread.sleep(1000);
		Demo demo = new Demo();
		demo.setCreateTime(new Date());
		demo.setValue(this.getClass().getClassLoader().getResource("").getPath() + "----" + System.currentTimeMillis() );
		
		System.out.println(">>ADD>>>>> " + demoService.addDemo(demo));

		Thread.sleep(1000);
		Demo demo2 = demoService.findById(demo.getId());
		System.out.println(">>FIND - " + demo.getId() + " >>>>> " + demo2);

		Thread.sleep(3000);
		System.out.println(">>LIST3>>>>> " + demoService.listAll());

		Thread.sleep(1000);
		System.out.println(">>UPDATE>>>>> " + demoService.modifiedDemo(demo.getId(), "Modified --- " + demo.getValue()));
		
		Thread.sleep(1000);
		demo2 = demoService.findById(demo.getId());
		System.out.println(">>FIND - " + demo.getId() + " >>>>> " + demo2);
		
		Thread.sleep(3000);
		System.out.println(">>LIST4>>>>> " + demoService.listAll());

		//Thread.sleep(1000);
		//System.out.println(">>DELETE>>>>> " + demoService.removeDemo(demo.getId()));

		Thread.sleep(3000);
		System.out.println(">>LIST4>>>>> " + demoService.listAll());
		
	}

}