package com.happer.control;

import com.happer.po.Bbsuser;
import com.happer.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

//@WebServlet(name="UserControl",urlPatterns = {"/user"})
@Controller
@RequestMapping(value = "/user")

public class UserControl  {
    @Autowired
    private UserServiceImpl service;


    @RequestMapping("/reg")//MultipartHttpServletRequest req Spring MVC支持的文件上传请求对象
    public void reg(MultipartHttpServletRequest req, HttpServletResponse resp) {
        CommonsMultipartResolver commonsMultipartResolver = new
                CommonsMultipartResolver(req.getSession().getServletContext());
        if (commonsMultipartResolver.isMultipart(req)) {//返回为true，它是enctype="multipart/form-data"  编码方式上传，流形式
            //纯注册
            reg(req, commonsMultipartResolver, resp);
        }


    }
    @RequestMapping("/message/{uid}")
    private void poll(@PathVariable String uid, HttpServletRequest req, HttpServletResponse resp) {
        // String uid = req.getParameter("uid");
        String result = service.getMessage(Integer.parseInt(uid));
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("text/html");

        try {
            PrintWriter writer = resp.getWriter();
            writer.print(result);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    @RequestMapping("/ur/{pagenum}")
    public void ur(@PathVariable  String pagenum, HttpServletRequest req, HttpServletResponse resp) {


        Bbsuser user = (Bbsuser) req.getSession().getAttribute("user");
        user.setPagenum(Integer.parseInt(pagenum));
        System.out.println(service.updatePageNumById(user));
        RequestDispatcher dispatcher = null;
        dispatcher = req.getRequestDispatcher("/");
        try {
            dispatcher.forward(req, resp);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void reg(MultipartHttpServletRequest req,
                     CommonsMultipartResolver commonsMultipartResolver,
                     HttpServletResponse resp) {
        Bbsuser user = service.uploadPic(req, commonsMultipartResolver);

        service.save(user);
        RequestDispatcher dispatcher = null;
        dispatcher = req.getRequestDispatcher("/");
        try {
            dispatcher.forward(req, resp);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    @RequestMapping("/logout")
    private void logout(HttpServletRequest req, HttpServletResponse resp) {
        RequestDispatcher dispatcher = null;
        req.getSession().removeAttribute("user");

        dispatcher = req.getRequestDispatcher("/");
        try {
            dispatcher.forward(req, resp);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @RequestMapping("/pic/{id}")
    private void pic(@PathVariable String id, HttpServletResponse resp) {

        Bbsuser user = service.findOne(Integer.parseInt(id));

        byte[] buffer = user.getPic();
        try {
            ServletOutputStream os = resp.getOutputStream();
            os.write(buffer);
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/login")
    public void login(@RequestParam("username") String username,
                      @RequestParam("password") String password,
                      HttpServletRequest req,
                      HttpServletResponse resp) {

        Bbsuser user = new Bbsuser(username, password);
        String flag = ((user = service.login(user)) == null) ? "false" : "true";

        if (flag.equals("true")) {
            //登录成功，需要把用户放到 session 让他每页都能访问
            req.getSession().setAttribute("user", user);

//            Cookie [] cs=req.getCookies();
//            for(Cookie c:cs){
//               if(c.getName().equals("wpau")){
//                   System.out.println(c.getValue());
//               }
//            }
            //是否使用cookie
//            String sun = req.getParameter("sun");
//            if (sun != null) {//已经勾上
//
//                Cookie cookie = new Cookie("papaoku", user.getUsername());
//                wpau.setMaxAge(3600 * 24 * 7);
//                resp.addCookie(cookie);
//                Cookie cookie1 = new Cookie("papaokp", user.getPassword());
//                wpap.setMaxAge(3600 * 24 * 7);
//                resp.addCookie(cookie1);
//
//            }
        }


        resp.setContentType("text/html");
        resp.setCharacterEncoding("utf-8");
        PrintWriter out = null;
        try {
            out = resp.getWriter();
            out.println(flag);
        } catch (IOException e) {
            e.printStackTrace();
        }
        out.flush();
        out.close();


    }


}
