/*
 * Copyright (c) 2018 duanxitao All rights reserved.
 */

package com.qit.lifecycle;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.PoisonPill;
import akka.actor.Props;
import com.typesafe.config.ConfigFactory;

/**
 * @ProjectName akkademo
 * @Description TODO
 * @Autohr DuanXiTao
 * @Date 2018/5/20 18:48
 * @Version 1.0
 */
public class CycleMain {
    public static void main(String[] args) {
        // 创建ActorSystem, 一般来说，一个系统只需要一个ActorSystem
        // 参数1：系统名称；参数2：配置文件
        ActorSystem system = ActorSystem.create("Hello", ConfigFactory.load("akka.config"));
        ActorRef myWork = system.actorOf(Props.create(MyWork.class), "myWork");
        ActorRef watchActor = system.actorOf(Props.create(WatchActor.class, myWork), "watchActor");

        myWork.tell(MyWork.Msg.WORKING, ActorRef.noSender());
        myWork.tell(MyWork.Msg.DONE, ActorRef.noSender());

        // 中断myWork
        myWork.tell(PoisonPill.getInstance(), ActorRef.noSender());
    }
}
