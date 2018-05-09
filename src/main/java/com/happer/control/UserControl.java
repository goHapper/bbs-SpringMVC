package com.happer.control;

import com.happer.po.Bbsuser;
import com.happer.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
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

@WebServlet(name = "UserControl",urlPatterns = {"/user"})
public class UserControl extends HttpServlet{
    @Autowired
    private UserServiceImpl service;
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //在当前的application下创建commonsMultipartResolver
        CommonsMultipartResolver commonsMultipartResolver = new
                CommonsMultipartResolver(req.getSession().getServletContext());
        if(commonsMultipartResolver.isMultipart(req)){//返回为true，它是enctype="multipart/form-data"  编码方式上传，流形式
            //纯注册
            reg(req,commonsMultipartResolver,resp);
        }else{
            String action=req.getParameter("action");
            switch (action){
                case "login":
                    login(req,resp);
                    break;
                case "logout":
                    logout(req,resp);
                    break;
                case "pic":
                    pic(req,resp);
                    break;
                case "ur"://修改行数
                    ur(req,resp);
                    break;
                case "message"://拉消息
                    poll(req,resp);
                    break;
            }
        }
    }

    private void poll(HttpServletRequest req, HttpServletResponse resp) {
        String uid=req.getParameter("uid");
        String result=service.getMessage(Integer.parseInt(uid));
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("text/html");

        try {
            PrintWriter writer =resp.getWriter();
            writer.print(result);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void ur(HttpServletRequest req, HttpServletResponse resp) {
    String pagenum=req.getParameter("pagenum");
    Bbsuser bbsuser=(Bbsuser)req.getSession().getAttribute("user");
    bbsuser.setPagenum(Integer.parseInt(pagenum));
    System.out.println(service.updatePageNumById(bbsuser));
    RequestDispatcher dispatcher = null;
    dispatcher=req.getRequestDispatcher("index");
        try {
            dispatcher.forward(req,resp);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void pic(HttpServletRequest req, HttpServletResponse resp) {
        String id = req.getParameter("id");
        Bbsuser bbsuser =service.findOne(Integer.parseInt(id));
        byte []buffer= bbsuser.getPic();
        try {
            ServletOutputStream os = resp.getOutputStream();
            os.write(buffer);
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void reg(HttpServletRequest req,
                     CommonsMultipartResolver commonsMultipartResolver,
                     HttpServletResponse resp) {
        Bbsuser user=service.uploadPic(req,commonsMultipartResolver);
        user=service.save(user);
        RequestDispatcher dispatcher = null;
        dispatcher=req.getRequestDispatcher("index");
        try {
            dispatcher.forward(req,resp);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void logout(HttpServletRequest req, HttpServletResponse resp) {
        RequestDispatcher dispatcher=null;//利用RequestDispatcher对象，可以将请求转发给另外一个Servlet或JSP页面，甚至是HTML页面，来处理对请求的响应。
        req.getSession().removeAttribute("user");
        dispatcher=req.getRequestDispatcher("index");
        try {
            dispatcher.forward(req,resp);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void login(HttpServletRequest req, HttpServletResponse resp) {
        String username= req.getParameter("username");//从前端获取的数据username和password
        String password= req.getParameter("password");
        Bbsuser bbsuser = new Bbsuser(username,password);
        String flag=((bbsuser=service.login(bbsuser))==null)?"false":"true";   //将true或者false传到前端
        if(flag.equals("true")){
            //登录成功，需要把用户放到 session 让他每页都能访问
            req.getSession().setAttribute("user",bbsuser);
            String sun = req.getParameter("sun");
            if(sun!=null){
                Cookie cookie = new Cookie("papaoku",bbsuser.getUsername());
                cookie.setMaxAge(3600*24*7);
                resp.addCookie(cookie);
                Cookie cookie1 = new Cookie("papaokp",bbsuser.getPassword());
                cookie1.setMaxAge(3600*24*7);
                resp.addCookie(cookie1);
            }
        }
        resp.setContentType("text/html");
        resp.setCharacterEncoding("utf-8");
        PrintWriter out = null;
        try {
            out=resp.getWriter();
            out.println(flag);
        } catch (IOException e) {
            e.printStackTrace();
        }
        out.flush();//清空数据
        out.close();//关闭流
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req,resp);
    }
    @Override
    public void init() throws ServletException {

    }
}
