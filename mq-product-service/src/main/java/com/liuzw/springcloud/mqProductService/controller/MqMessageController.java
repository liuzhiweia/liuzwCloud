package com.liuzw.springcloud.mqProductService.controller;

import com.liuzw.springcloud.common.R;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: liuzw
 * @date: 2021/9/3 6:02 下午
 * @e-mail: liuzhiwei1@ylzinfo.com
 * -------------------------------
 * @description： mq消息发送控制层
 */
@RestController
@RequestMapping("/mqMessage")
public class MqMessageController {
    private static final Logger log = LoggerFactory.getLogger(MqMessageController.class);

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @RequestMapping("/send")
    public R send(@RequestParam("message") String message){
        rocketMQTemplate.convertAndSend("first-topic",message);
        return R.ok();
    }

}
