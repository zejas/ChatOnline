package com.zejas.authsvr.service;

import com.zejas.authsvr.model.vo.LoginUserVo;
import com.zejas.authsvr.event.LoginSuccessEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * <p>  </p>
 *
 * @author ZShuo
 * @description
 * @date 2025/6/28 10:14
 */

@Service
public class CouponService {

    private Logger logger = LogManager.getLogger(CouponService.class);

    @EventListener({LoginSuccessEvent.class})
    public void onEvent(ApplicationEvent event) {
        logger.info("优惠券感知事件{}",event.getClass().getSimpleName());
        LoginUserVo user = (LoginUserVo) event.getSource();
        sendCoupon(user.getUsername());
    }

    public void sendCoupon(String username) {
        //给用户随机添加优惠券
        logger.info("给用户{}随机添加一个优惠券",username);
    }
}
