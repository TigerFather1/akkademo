/*
 * Copyright (c) 2018 duanxitao All rights reserved.
 */

package com.qit.sample;

import akka.actor.UntypedActor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qit.bean.Message;

/**
 * @ProjectName akkademo
 * @Description TODO
 * @Autohr DuanXiTao
 * @Date 2018/5/20 17:17
 * @Version 1.0
 */
public class Greeter extends UntypedActor {
    ObjectMapper mapper = new ObjectMapper();

    public static enum Msg {
        GREET, DONE
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof Message) {
            String msg = mapper.writeValueAsString(message);
            System.out.println("Receive mesage: " + msg);
            Thread.sleep(1000);
            getSender().tell(Msg.DONE, getSelf());
        } else {
            unhandled(message);
        }
    }
}
