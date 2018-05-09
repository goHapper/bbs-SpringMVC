package com.happer.control;

import com.happer.service.ArticleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "/file",urlPatterns = {"/kindupload"})
public class KindControl extends HttpServlet{
    @Autowired
    private ArticleServiceImpl service;
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String s =service.uploadPic(req);
        PrintWriter out=resp.getWriter();

        out.print(s);
        out.flush();
        out.close();
    }
}
