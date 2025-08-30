package com.lyc.lease.web.admin.service;

import com.lyc.lease.model.entity.SystemUser;
import com.lyc.lease.web.admin.vo.system.user.SystemUserItemVo;
import com.lyc.lease.web.admin.vo.system.user.SystemUserQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author liubo
* @description 针对表【system_user(员工信息表)】的数据库操作Service
* @createDate 2025-07-24 15:48:00
*/
public interface SystemUserService extends IService<SystemUser> {

    IPage<SystemUserItemVo> pageSystemUser(IPage<SystemUserItemVo> page, SystemUserQueryVo queryVo);

    SystemUserItemVo getSystemUserById(Long id);
    SystemUserItemVo getSystemUserById3(Long id);
    SystemUserItemVo getSystemUserById4(Long id);


    //写代码之前会先定义规范，
    //接口就是我们要实现哪些方法，
    //它的实现类就是我们具体实现这些方法
}
