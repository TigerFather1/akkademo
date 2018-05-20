/*
 * Copyright (c) 2018 duanxitao All rights reserved.
 */

package com.qit.com.sample;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

/**
 * @ProjectName akkademo
 * @Description TODO
 * @Autohr DuanXiTao
 * @Date 2018/5/20 17:23
 * @Version 1.0
 */
public class AkkaMain {
    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("Hello");
        ActorRef ref = system.actorOf(Props.create(HelloWorld.class), "helloWorld");
        System.out.println(ref.path());
    }
}
