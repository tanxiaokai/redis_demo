package io.github.tanxiaokai.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import io.github.tanxiaokai.mapper.DemoMapper;
import io.github.tanxiaokai.model.Demo;

@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class DemoService {

	@Autowired
	private DemoMapper demoMapper;

	@Cacheable(value="DemoService", keyGenerator="customKeyGenerator")
	public List<Demo> listAll() {
		return demoMapper.listAll();
	}

	@Cacheable(value="DemoService", keyGenerator="customKeyGenerator")
	public Demo findById(Integer id) {
		return demoMapper.findById(id);
	}
	
	@CacheEvict(value = { "DemoService"}, allEntries = true)
	public Demo addDemo(Demo demo) {
		
		demoMapper.insertDemo(demo);
		return demo;
	}

	@CacheEvict(value = { "DemoService"}, allEntries = true)
	public Boolean modifiedDemo(Integer id, String value) {
		return demoMapper.updateDemo(id, value);
	}

	@CacheEvict(value = { "DemoService"}, allEntries = true)
	public Boolean removeDemo(Integer id) {
		return demoMapper.deleteDemo(id);
	}
}
