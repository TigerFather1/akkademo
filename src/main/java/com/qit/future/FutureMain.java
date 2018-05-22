/*
 * Copyright (c) 2018 duanxitao All rights reserved.
 */

package com.qit.future;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.PoisonPill;
import akka.actor.Props;
import akka.pattern.Patterns;
import com.typesafe.config.ConfigFactory;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

/**
 * @ProjectName akkademo
 * @Description TODO
 * @Autohr DuanXiTao
 * @Date 2018/5/22 21:15
 * @Version 1.0
 */
public class FutureMain {
    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("strategy", ConfigFactory.load("akka.config"));
        ActorRef printActor = system.actorOf(Props.create(PrintAcor.class), "PrintActor");
        ActorRef workerActor = system.actorOf(Props.create(WorkerActor.class), "WorkerActor");

        try {
            // 等待future返回
            Future<Object> future = Patterns.ask(workerActor, 5, 1000);
            int result = (int) Await.result(future, Duration.create(3,TimeUnit.SECONDS));
            System.out.println("result: " + result);

            // 不等待返回值，直接重定向到其他Actor，由返回值来的时候将会重定向到PrintActor
            Future<Object> future1 = Patterns.ask(workerActor, 8, 1000);
            Patterns.pipe(future1, system.dispatcher()).to(printActor);

            workerActor.tell(PoisonPill.getInstance(), ActorRef.noSender());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
