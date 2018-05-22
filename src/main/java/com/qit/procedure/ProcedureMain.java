/*
 * Copyright (c) 2018 duanxitao All rights reserved.
 */

package com.qit.procedure;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.PoisonPill;
import akka.actor.Props;
import com.typesafe.config.ConfigFactory;

/**
 * @ProjectName akkademo
 * @Description TODO
 * @Autohr DuanXiTao
 * @Date 2018/5/22 20:37
 * @Version 1.0
 */
public class ProcedureMain {
    public static void main(String[] args) {
        ActorSystem system =  ActorSystem.create("strategy", ConfigFactory.load("akka.config"));
        ActorRef procedure = system.actorOf(Props.create(ProcedureTest.class), "ProcedureTest");

        procedure.tell(ProcedureTest.Msg.PLAY, ActorRef.noSender());
        procedure.tell(ProcedureTest.Msg.SLEEP, ActorRef.noSender());
//        procedure.tell(ProcedureTest.Msg.PLAY, ActorRef.noSender());
//        procedure.tell(ProcedureTest.Msg.PLAY, ActorRef.noSender());

        procedure.tell(PoisonPill.getInstance(), ActorRef.noSender());
    }
}
