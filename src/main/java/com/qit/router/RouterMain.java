/*
 * Copyright (c) 2018 duanxitao All rights reserved.
 */

package com.qit.router;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.qit.inbox.InboxTest;
import com.typesafe.config.ConfigFactory;

/**
 * @ProjectName akkademo
 * @Description TODO
 * @Autohr DuanXiTao
 * @Date 2018/5/20 23:32
 * @Version 1.0
 */
public class RouterMain {
    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("strategy", ConfigFactory.load("akka.config"));
        ActorRef routerTest = system.actorOf(Props.create(RouterTest.class), "RouterTest");

        int i = 1;
        while(RouterTest.flag.get()){
            routerTest.tell(InboxTest.Msg.WORKING, ActorRef.noSender());

            if(i % 10 == 0) {
                routerTest.tell(InboxTest.Msg.CLOSE, ActorRef.noSender());
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            i ++;
        }
    }
}
