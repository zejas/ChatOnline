package com.zejas.authsvr.controller;

import com.zejas.authsvr.common.AuthResponse;
import com.zejas.authsvr.common.R;
import com.zejas.authsvr.event.EventPublisher;
import com.zejas.authsvr.event.LoginSuccessEvent;
import com.zejas.authsvr.exception.AuthException;
import com.zejas.authsvr.exception.AuthExceptionEnum;
import com.zejas.authsvr.model.po.UserRole;
import com.zejas.authsvr.model.vo.LoginUserVo;
import com.zejas.authsvr.service.UserRoleService;
import com.zejas.authsvr.service.UserService;
import com.zejas.authsvr.util.TokenUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

;

/**
 * <p>  </p>
 *
 * @author ZShuo
 * @description
 * @date 2025/6/23 10:45
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    TokenUtil tokenUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private EventPublisher eventPublisher;


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
    @GetMapping("/validate")
    public AuthResponse validate(@RequestHeader("Authorization") String token){
        if(token == null || !token.startsWith("Bearer ")){
            throw new AuthException(AuthExceptionEnum.AUTH_EMPTY);
        }
        Long userId = tokenUtil.verifyAccessToken(token.substring(7));
        List<UserRole> userRoles = List.of(userRoleService.getUserRoleByUserId(userId));
        List<Integer> roleCode = userRoles.stream().map(UserRole::getRoleCode).collect(Collectors.toList());
        
        return AuthResponse.success(true,userId,roleCode);
    }

}
