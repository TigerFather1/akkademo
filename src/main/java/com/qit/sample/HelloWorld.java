/*
 * Copyright (c) 2018 duanxitao All rights reserved.
 */

package com.qit.sample;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qit.bean.Message;

/**
 * @ProjectName akkademo
 * @Description TODO
 * @Autohr DuanXiTao
 * @Date 2018/5/20 17:09
 * @Version 1.0
 */
public class HelloWorld extends UntypedActor {
    ObjectMapper mapper = new ObjectMapper();

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message == Greeter.Msg.DONE) {
            // when the greeter is done, stop this actor and with it the application
            System.out.println(mapper.writeValueAsString(message));
            getContext().stop(getSelf());
        } else {
            unhandled(message);
        }
    }

    @Override
    public void preStart() throws Exception {
        // create the greeter actor
        final ActorRef greeter = getContext().actorOf(Props.create(Greeter.class), "greeter");
        // tell it to perform the greeting
        Message message = new Message("Xiaowang", "123456", "Shanghai", 20);
        greeter.tell(message, getSelf());
    }
}
