package com.happer.dao;

import com.happer.po.Article;
import com.happer.po.Bbsuser;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ArticleDAOImpl {
    @PersistenceContext//原生jpa
    private EntityManager entityManager;
    public Map<String,Object> queryById(int id){
        Map<String,Object> map = new HashMap<>();
        StoredProcedureQuery procedureQuery=entityManager.createStoredProcedureQuery("P_1");
        procedureQuery.registerStoredProcedureParameter(1,Integer.class,ParameterMode.IN);
        procedureQuery.registerStoredProcedureParameter(2,String.class,ParameterMode.OUT);
        procedureQuery.setParameter(1,id);//输入参数
        procedureQuery.execute();
        List<Object[]> list =procedureQuery.getResultList();
        List<Article> alist=new ArrayList<>();
        list.forEach((s)->{
            Article a=new Article();
            a.setId(Integer.parseInt(s[0].toString()));
            a.setRootid(Integer.parseInt(s[1].toString()));
            a.setTitle(s[2].toString());
            a.setContent(s[3].toString());
            a.setUser(new Bbsuser(Integer.parseInt(s[4].toString())));
            try {
                java.util.Date d=new SimpleDateFormat("yyyy-mm-dd").parse(s[5].toString());
                a.setDatetime(new java.sql.Date(d.getTime()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            alist.add(a);

        });
        map.put("title",procedureQuery.getOutputParameterValue(2));
        map.put("list",alist);
        return map;
    }





}
