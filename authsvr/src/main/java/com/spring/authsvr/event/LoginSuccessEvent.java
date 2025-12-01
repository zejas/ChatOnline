package com.spring.authsvr.event;

import com.spring.authsvr.vo.LoginUserVo;
import org.springframework.context.ApplicationEvent;

/**
 * <p>  </p>
 *
 * @author ZShuo
 * @description
 * @date 2025/6/28 9:57
 */
public class LoginSuccessEvent extends ApplicationEvent {
    public LoginSuccessEvent(LoginUserVo loginUserVo) {
        super(loginUserVo);
    }
}
