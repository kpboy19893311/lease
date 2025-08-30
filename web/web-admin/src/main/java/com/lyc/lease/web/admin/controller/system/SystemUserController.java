package com.lyc.lease.web.admin.controller.system;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyc.lease.common.result.Result;
import com.lyc.lease.model.entity.SystemUser;
import com.lyc.lease.model.enums.BaseStatus;
import com.lyc.lease.web.admin.service.SystemUserService;
import com.lyc.lease.web.admin.vo.system.user.SystemUserItemVo;
import com.lyc.lease.web.admin.vo.system.user.SystemUserQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.web.bind.annotation.*;


@Tag(name = "后台用户信息管理")
@RestController
@RequestMapping("/admin/system/user")
public class SystemUserController {

    @Autowired
    SystemUserService systemUserService;

    /**
     * 根据条件分页查询后台用户列表
     *
     * @param current 当前页码
     * @param size    每页显示数量
     * @param queryVo 查询条件对象
     * @return 包含分页结果和查询条件的Result对象
     */
    @Operation(summary = "根据条件分页查询后台用户列表")
    @GetMapping("page")
    public Result<IPage<SystemUserItemVo>> page(@RequestParam long current, @RequestParam long size, SystemUserQueryVo queryVo) {
        //dto 操作数据库，传一个对象，
        //实体类 entity 他的每个属性与数据库一一对应。
        //dto ：数据传输对象  我们增删改查的时候，不需要全部的属性，只需要对应的属性进行查询或者修改，所以我们定义一个dto
        //vo: 视图对象， 返回给前端的对象，用户能看到的信息，用户不需要知道数据库的属性，只需要知道用户看到的信息
        Page<SystemUserItemVo> objectPage = new Page<>(current, size);
        IPage<SystemUserItemVo> systemUserItemVoIPage = systemUserService.pageSystemUser(objectPage, queryVo);
        return Result.ok(systemUserItemVoIPage);
    }

    /**
     * 根据ID查询后台用户信息
     *
     * @param id 后台用户的唯一标识符
     * @return 包含后台用户信息的Result对象
     */
    @Operation(summary = "根据ID查询后台用户信息")
    @GetMapping("getById")
    public Result<SystemUserItemVo> getById(@RequestParam Long id) {
        SystemUserItemVo systemUserById = systemUserService.getSystemUserById(id);
        return Result.ok(systemUserById);
    }

    /**
     * @RequestBody 当发送一个Post请求时，前端发送的Json对象形式的参数，
     * 后端接受对象就用@RequestBody
     *
     * @param systemUser
     * @return
     */
    @Operation(summary = "保存或更新后台用户信息")
    @PostMapping("saveOrUpdate")
    public Result saveOrUpdate(@RequestBody SystemUser systemUser) {
        //参数校验
        if (systemUser.getUsername() == null || systemUser.getUsername().trim().length() == 0) {
            return Result.fail(400, "用户名不能为空");
        }
        //密码不能为空
        if (systemUser.getPassword() == null || systemUser.getPassword().trim().length() == 0) {
            return Result.fail(400, "密码不能为空");
        }
        //...
        boolean b = systemUserService.saveOrUpdate(systemUser);
        return Result.ok(b);
    }

    /**
     * 判断后台用户名是否可用
     *
     * @param username 需要判断的用户名
     * @return 如果用户名可用，则返回包含true的Result对象；否则返回包含false的Result对象
     */
    @Operation(summary = "判断后台用户名是否可用")
    @GetMapping("isUserNameAvailable")
    public Result<Boolean> isUsernameExists(@RequestParam String username) {
        //一般状态类型 都是数字形式表示的  0 1 2
        // char 节省内存空间
        LambdaQueryWrapper<SystemUser> queryWrapper = new LambdaQueryWrapper<>();
        //Lambada表达式
        queryWrapper.eq(SystemUser::getUsername, username);
        long count = systemUserService.count(queryWrapper);

        //count == 0成功
        //count != 0 失败
        return Result.ok(count == 0);
    }

    /**
     * 根据ID删除后台用户信息
     *
     * @param id 后台用户的唯一标识符
     * @return 删除成功的结果对象
     */
    @DeleteMapping("deleteById")
    @Operation(summary = "根据ID删除后台用户信息")
    public Result removeById(@RequestParam Long id) {
        systemUserService.removeById(id);
        return Result.ok();
    }

    /**
     * 根据ID修改后台用户状态
     *
     * @param id     后台用户的唯一标识符
     * @param status 需要设置的新状态
     * @return 修改成功的结果对象
     */
    @Operation(summary = "根据ID修改后台用户状态")
    @PostMapping("updateStatusByUserId")
    public Result updateStatusByUserId(@RequestParam Long id, @RequestParam BaseStatus status) {
        LambdaUpdateWrapper<SystemUser> updateWrapper = new LambdaUpdateWrapper<>();
        //equals
        updateWrapper.eq(SystemUser::getId, id);
        updateWrapper.set(SystemUser::getStatus, status);
        boolean update = systemUserService.update(updateWrapper);
        return Result.ok(update);
    }
}
