package com.happer.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.resource.VersionResourceResolver;

@Configuration
public class MyWebConfig extends WebMvcConfigurerAdapter {

//    @Override//设置主页访问地址
//    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/").setViewName("forward:/article/queryall/1");
//        super.addViewControllers( registry );
//    }

    @Override//防止静态资源被拦截
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/").resourceChain(true);


        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/").resourceChain(true).addResolver(
                new VersionResourceResolver().addFixedVersionStrategy("1.10", "/**/*.js").addContentVersionStrategy("/**"));


    }
}