/*
 * Copyright (c) 2018 duanxitao All rights reserved.
 */

package com.qit.supervision;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.ConfigFactory;

/**
 * @ProjectName akkademo
 * @Description TODO
 * @Autohr DuanXiTao
 * @Date 2018/5/20 22:07
 * @Version 1.0
 */
public class SupervisorMain {
    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("strategy", ConfigFactory.load("akka.config"));
        ActorRef supervisor = system.actorOf(Props.create(Supervisor.class), "supervisor");
        supervisor.tell(Props.create(RestartActor.class), ActorRef.noSender());

        ActorSelection actorSelection = system.actorSelection("akka://strategy/user/supervisor/restartActor");
        for (int i=0; i < 100; i++) {
            actorSelection.tell(RestartActor.Msg.RESTART, ActorRef.noSender());
        }
    }
}
