package com.spring.authsvr.controller;

import com.spring.authsvr.annotation.CheckPermission;
import com.spring.authsvr.common.R;
import com.spring.authsvr.event.EventPublisher;
import com.spring.authsvr.event.LoginSuccessEvent;
import com.spring.authsvr.po.User;
import com.spring.authsvr.service.UserService;
import com.spring.authsvr.vo.LoginUserVo;
import com.spring.authsvr.vo.RegisterUserVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

;

/**
 * <p>  </p>
 *
 * @author ZShuo
 * @description
 * @date 2025/6/23 10:45
 */
@Tag(name="用户",description = "用户信息CRUD操作")
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private EventPublisher eventPublisher;

    @Operation(summary = "登录")
    @PostMapping("/login")
    public R login(@Valid @RequestBody LoginUserVo loginUserVo){
        R r = null;

        Map<String,String> data =  userService.login(loginUserVo.getUsername(), loginUserVo.getPassword());

        if(data != null){
            r = R.success(data);
        }else{
            r = R.fail(500,"获取token失败","error");
        }

        if(r.getCode() == 200){
            //1.准备事件loginUser
            //2.发送事件
            eventPublisher.publishEvent(new LoginSuccessEvent(loginUserVo));
        }

        return r;
    }

    @Operation(summary = "注册用户")
    @PostMapping("/register")
    public R<String> register(@Valid @RequestBody RegisterUserVo userVo) {
        userService.addUser(userVo);
        return R.success("success");
    }

    @CheckPermission(permitType = {"manager","committer"})
    @Operation(summary = "按照id查询用户")
    @GetMapping("/user/{id}")
    public R<User> getUser(@PathVariable("id")Long id) {
        User user = userService.getUserById(id);
        return R.success(user);
    }


}
