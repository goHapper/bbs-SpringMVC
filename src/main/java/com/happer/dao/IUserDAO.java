package com.happer.dao;

import com.happer.po.Bbsuser;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;


public interface IUserDAO extends CrudRepository<Bbsuser,Integer> {//主键是int
    @Query("select c from Bbsuser  c where username =:u and password=:p")
    Bbsuser login(@Param("u")String username, @Param("p")String password);

    @Override
    Bbsuser save(Bbsuser user);

    @Override
    Bbsuser findOne(Integer id);


    @Modifying
    @Query("update Bbsuser set pagenum=:p where userid=:u")
    int updatePageNumById(@Param("u")Integer userid,@Param("p")Integer pagenum);
    //int updatePageNumById(@Param("user")Bbsuser user);
}

