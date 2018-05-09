package com.happer.service;

import com.alibaba.fastjson.JSON;
import com.happer.dao.IUserDAO;
import com.happer.po.Bbsuser;
import com.happer.po.Message;
import com.happer.service.kafka.consumer.MsgConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class UserServiceImpl {
    @Autowired
    private IUserDAO dao;
    @Autowired
    private MsgConsumer msgConsumer;
    public Bbsuser login(Bbsuser bbsuser){
        return dao.login(bbsuser.getUsername(),bbsuser.getPassword());
    }

    private Map<String,String> types=new HashMap<String,String>();

    public UserServiceImpl(){
        //允许上传的文件类型
        types.put("image/jpeg", ".jpg");
        types.put("image/gif", ".gif");
        types.put("image/x-ms-bmp", ".bmp");
        types.put("image/png", ".png");
    }
    public Bbsuser uploadPic(HttpServletRequest req, CommonsMultipartResolver commonsMultipartResolver){
        Bbsuser user = null;
        commonsMultipartResolver.setDefaultEncoding("utf-8");//文件可以有中文名
        commonsMultipartResolver.setResolveLazily(true);//必须上传完毕才解析
        commonsMultipartResolver.setMaxInMemorySize(4096*1024);//设置交换空间的缓存
        commonsMultipartResolver.setMaxUploadSizePerFile(1024*1024);
        commonsMultipartResolver.setMaxUploadSize(2*1024*1024);//上传总共文件的大小
        if(commonsMultipartResolver.isMultipart(req)){
            //转换req，把键值对的req转换成流的req
            MultipartHttpServletRequest request=commonsMultipartResolver. resolveMultipart(req);

            MultipartFile file= request.getFile("file0");

            String type=file.getContentType();//你将上传的文件格式
            if(types.containsKey(type)){//符合
                //生成全局唯一码，做文件名字，防止多线程冲突
                File targetFile=new File("upload"+File.separator+req.getSession().getId()+types.get(type));

                user=new Bbsuser();
                String reusername=request.getParameter("reusername");
                String repassword=request.getParameter("repassword");

                user.setUsername(reusername);
                user.setPassword(repassword);
                user.setPicPath(targetFile.getPath());
                //user.setPagenum(5);
                //拷贝文件
                try {
                    file.transferTo(targetFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //把头像数据放到po中
                try(FileInputStream fis=new FileInputStream(targetFile);) {

                    byte[] buffer=new byte[fis.available()];
                    fis.read(buffer);
                    user.setPic(buffer);
                    user.setPagenum(5);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return user;
        }
        public Bbsuser save(Bbsuser user){
            return dao.save(user);
        }

        public Bbsuser findOne(Integer id){
            return dao.findOne(id);
        }

        public int updatePageNumById(Bbsuser user){
            //return dao.updatePageNumById(user);
            return dao.updatePageNumById(user.getUserid(),user.getPagenum());

        }

    public String getMessage(Integer uid){
        List<Message> list=msgConsumer.consumerMsg(uid);
        return JSON.toJSONString(list);
    }
}

