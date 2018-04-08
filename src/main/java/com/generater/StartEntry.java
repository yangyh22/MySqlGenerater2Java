package com.generater;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Description 启动入口类
 * @author yangyh
 * @date 2018年4月8日
 * @version V1.0.0
 */
@Controller
@SpringBootApplication
public class StartEntry {

	@RequestMapping("/")
	public ModelAndView index() {
		return new ModelAndView("index");
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(StartEntry.class, args);
	}
}
