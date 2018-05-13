package com.happer;

import com.happer.filter.EncodingFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

//@ServletComponentScan
@SpringBootApplication
@Controller
@EnableWebMvc
public class BbsApplication {
//	@Bean
//	public FilterRegistrationBean registrationBean(){
//		FilterRegistrationBean filterRegistrationBean=new FilterRegistrationBean();
//		filterRegistrationBean.setFilter(this.encodingFilter());
//		filterRegistrationBean.addUrlPatterns("/*");
//		filterRegistrationBean.addInitParameter("encoder", "utf-8");
//		filterRegistrationBean.setName("EncodingFilter");
//		filterRegistrationBean.setOrder(1);
//		return filterRegistrationBean;
//	}
@RequestMapping("/")
public String home(){
	return "redirect:/article/queryall/1";//转向IndexController
}

	public static void main(String[] args) {
		SpringApplication.run(BbsApplication.class, args);
	}

}
