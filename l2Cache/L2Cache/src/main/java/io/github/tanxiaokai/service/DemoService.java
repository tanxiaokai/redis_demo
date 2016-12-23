package io.github.tanxiaokai.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.github.tanxiaokai.mapper.DemoMapper;
import io.github.tanxiaokai.model.Demo;

@Service
public class DemoService {
	
	@Autowired
	private DemoMapper demoMapper;
	

	public List<Demo> listAll(){
		return demoMapper.listAll();
	}
	
}
