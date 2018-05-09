package com.happer.util;

import freemarker.template.Configuration;
import freemarker.template.Template;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;


public class FreemarkerUtil {
    private static Configuration configuration;

    private static Configuration bulitConfiguration() {

        if(null==configuration){
            configuration = new Configuration(Configuration.VERSION_2_3_26);


            String path = FreemarkerUtil.class.getResource("/").getPath();//编译之后没有Resource  resource下的文件直接在src下
            //path = path.substring(1, path.indexOf("classes"));
            File ftlPathDir = new File(path+File.separator+"templates");// (File.separator)代表斜杠 \  /

            try {
                configuration.setDefaultEncoding("utf-8");
                configuration.setDirectoryForTemplateLoading(ftlPathDir);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return configuration;
        }
        return configuration;
    }


    /**
     * 获取模板Template
     * @param ftlName
     * @return
     */
    public static Template getTemplate(String ftlName){
        Template template=null;
        try {
            template = bulitConfiguration().getTemplate(ftlName);//得到模板
            return template;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return template;
    }

    public static void forward(String ftlName, Map map, HttpServletResponse resp){//freemarker 转向
        Template temp=getTemplate(ftlName);
        resp.setContentType("text/html");//响应类型
         resp.setCharacterEncoding("UTF-8");
        PrintWriter out=null;
        try {
            out = resp.getWriter();
            temp.process(map, out);//map  把user属性（键值对）传给前端
        } catch (Exception e) {
            e.printStackTrace();
        }
        out.flush();
    }
}