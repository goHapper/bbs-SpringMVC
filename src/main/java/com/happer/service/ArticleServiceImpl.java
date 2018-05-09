package com.happer.service;

import com.alibaba.fastjson.JSONObject;
import com.happer.po.Article;
import com.happer.dao.ArticleDAOImpl;
import com.happer.dao.IArticleDAO;
import com.happer.service.kafka.producer.MsgProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional
public class ArticleServiceImpl {
    private Map<String,String> types=new HashMap<String,String>();
    public ArticleServiceImpl(){
        types.put("image/jpeg", ".jpg");
        types.put("image/gif", ".gif");
        types.put("image/x-ms-bmp", ".bmp");
        types.put("image/png", ".png");
    }

    @Autowired
    public IArticleDAO dao;


    @Autowired
    public ArticleDAOImpl dao1;

    @Autowired
    public MsgProducer msgProducer;

    public Page<Article> findAll(Pageable pb, Integer rid){
        return dao.findAll(pb,rid);
    }

    public int deleteZT(Integer id){
        return dao.deleteZT(id);
    }

    public String uploadPic(HttpServletRequest req){//上传t图片
        CommonsMultipartResolver commonsMultipartResolver = new
                CommonsMultipartResolver(req.getSession().getServletContext());

        commonsMultipartResolver.setDefaultEncoding("utf-8");//文件可以有中文名
        commonsMultipartResolver.setResolveLazily(true);//必须上传完毕才解析
        commonsMultipartResolver.setMaxInMemorySize(4096*1024);//设置交换空间的缓存
        commonsMultipartResolver.setMaxUploadSizePerFile(1024*1024);
        commonsMultipartResolver.setMaxUploadSize(2*1024*1024);//上传总共文件的大小
        if(commonsMultipartResolver.isMultipart(req)){
            //转换req，把键值对的req转换成流的req
            MultipartHttpServletRequest request=commonsMultipartResolver.resolveMultipart(req);
            MultipartFile file=request.getFile("imgFile");//上传组建的名字。
            String type=file.getContentType();//你将上传的文件格式
            if(types.containsKey(type)){//符合
                //生成全局唯一码，做文件名字，防止多线程冲突
               // File targetFile=new File("upload"+File.separator+req.getSession().getId()+types.get(type));
                String s3=ArticleServiceImpl.class.getClassLoader().getResource("").toString();
                //取得文件上传的目的目录
                String dir=req.getParameter("dir");//

                String id= UUID.randomUUID().toString(); //得到上传后的文件名称，唯一名称

                String newFileName= s3+ "static/editor/upload/" +dir+"/"+ id+types.get(type);

                newFileName=newFileName.substring(6);//file:\D:\DOC\java 类似格式
                //上传后记录的文件...
                File imageFile = new File(newFileName);
                //取得正常字段内容
                //上传...
                try {

                    file.transferTo(imageFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }


                //形成在编辑器页面显示的url地址 localhost://XXX
                String tpath=req.getRequestURL().toString() ;
                tpath=tpath.substring(0,tpath.lastIndexOf("/"));
               // tpath=tpath.substring(0,tpath.lastIndexOf("/"));
                String path=tpath+"/editor/upload/"+dir+"/";//最终显示在编辑器中图片路径


                JSONObject obj = new JSONObject();
                obj.put("error", 0);//无错误
                obj.put("url", path+ id+types.get(type));//使用json格式把上传文件信息传递到前端
                System.out.println("ok");
                return obj.toJSONString();
            }

        }
        return "";


    }



    public Map<String,Object> queryById(int id){
        return dao1.queryById(id);
    }
    public  int delectCT(int id){
        return dao.deleteCT(id);
    }

    public Article save(Article article){
        Article a=dao.save(article);
        if(article.getRootid()!=0){//从贴
            msgProducer.sendMsg(article);
        }
        return a;
    }

    public Article findOne(Integer rid){//根据主贴id，去找发主贴用户id
        return dao.findOne(rid);
    }
}
