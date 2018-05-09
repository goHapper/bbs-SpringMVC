package com.happer.control;

import com.alibaba.fastjson.JSON;
import com.happer.po.Article;
import com.happer.po.Bbsuser;
import com.happer.po.PageBean;
import com.happer.service.ArticleServiceImpl;
import com.happer.util.FreemarkerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "ArticleControl",urlPatterns = {"/article"},
        initParams = {@WebInitParam(name="success",value="/show.ftl")})
public class ArticleControl extends HttpServlet{
    Map<String,String> map = new HashMap();
    @Autowired
    private ArticleServiceImpl service;
    @Override
    public void init(ServletConfig config) throws ServletException {
        map.put("success",config.getInitParameter("success"));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String action =req.getParameter("action");
        Map map1 = new HashMap();
        switch (action){
            case "queryall":
                PageBean pb=queryAll(req,resp);
                map1.put("pb",pb);
                break;
            case "delz":
                String id =req.getParameter("is");
                delz(req,resp);
                break;
            case "addz":
                add(req,resp);
                break;
            case "queryid":
                queryreply(req,resp);
                break;
            case "delc":
                delc(req,resp);
                break;
            case "reply":
                add(req,resp);
                break;
        }

        map1.put("user",req.getSession().getAttribute("user"));
        FreemarkerUtil.forward(map.get("success").toString(),map1,resp);
    }

    private void delc(HttpServletRequest req, HttpServletResponse resp) {
        String rid = req.getParameter("rid");
        service.delectCT(Integer.parseInt(rid));
        queryreply(req,resp);
    }

    private void queryreply(HttpServletRequest req, HttpServletResponse resp) {
        String id =  req.getParameter("id");
        Map<String,Object> map=service.queryById(Integer.parseInt(id));
        String json=JSON.toJSONString(map,true);
        resp.setContentType("text/html");
        resp.setCharacterEncoding("utf-8");
        try {
            PrintWriter out=resp.getWriter();
            out.print(json  );
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void add(HttpServletRequest req, HttpServletResponse resp) {
    String title=req.getParameter("title");
    String content=req.getParameter("content");
    String rootid=req.getParameter("rootid");
    String userid=req.getParameter("userid");

    Article a=new Article();
    a.setRootid(Integer.parseInt(rootid));
    a.setContent(content);
    a.setTitle(title);
    a.setDatetime(new Date(System.currentTimeMillis()));
    a.setUser(new Bbsuser(Integer.parseInt(userid)));
    if(service.save(a)!=null){
        if(rootid.equals("0")){
            RequestDispatcher dispatcher = null;
            dispatcher=req.getRequestDispatcher("index");
            try {
                dispatcher.forward(req,resp);
            } catch (ServletException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            queryreply(req,resp);
        }

    }
    }

    private void delz(HttpServletRequest req, HttpServletResponse resp) {
        String id =req.getParameter("id");
        service.deleteZT(Integer.parseInt(id));
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

    private PageBean queryAll(HttpServletRequest req, HttpServletResponse resp) {
        String page =req.getParameter("page");
        Bbsuser bbsuser =(Bbsuser)  req.getSession().getAttribute("user");
        int pageNum=5;
        if(bbsuser!=null){
            pageNum=bbsuser.getPagenum();
        }
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        Pageable pageable = new PageRequest(Integer.parseInt(page)-1,pageNum,sort);
        Page<Article> pa=service.findAll(pageable,0);


        PageBean pb =new PageBean();
        pb.setRowsPerPage(pageNum);//
        pb.setCurPage(Integer.parseInt(page));//当前是第几页
        pb.setMaxRowCount(pa.getTotalElements());
        pb.setData(pa.getContent());
        pb.setMaxPage(pa.getTotalPages());


        return pb;
    }
}