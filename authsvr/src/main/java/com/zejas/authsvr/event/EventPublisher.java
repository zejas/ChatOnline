package com.zejas.authsvr.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;

/**
 *  <p>  </p>
 *
 * @author ZShuo
 * @description 
 * @date 2025/6/28 10:08
 */
@Component
public class EventPublisher implements ApplicationEventPublisherAware {

    ApplicationEventPublisher applicationEventPublisher;

     public void publishEvent(ApplicationEvent event) {
        applicationEventPublisher.publishEvent(event);
     }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
