/*
 * Copyright (c) 2018 duanxitao All rights reserved.
 */

package com.qit.smt;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.Patterns;
import akka.transactor.Coordinated;
import akka.util.Timeout;
import com.typesafe.config.ConfigFactory;
import scala.concurrent.Await;

import java.util.concurrent.TimeUnit;

/**
 * @ProjectName akkademo
 * @Description TODO
 * @Autohr DuanXiTao
 * @Date 2018/5/22 22:02
 * @Version 1.0
 */
public class SMPMain {
    public static ActorRef companyActor = null;
    public static ActorRef employeeActor = null;

    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("stm", ConfigFactory.load("akka.conf"));
        companyActor = system.actorOf(Props.create(CompanyActor.class), "CompanyActor");
        employeeActor = system.actorOf(Props.create(EmployeeActor.class), "EmployeeActor");

        Timeout timeout = new Timeout(1, TimeUnit.SECONDS);
        try {
            for (int i = 0; i < 23; i++) {
                companyActor.tell(new Coordinated(i, timeout), ActorRef.noSender());
                Thread.sleep(200);

                int companyCount = (int) Await.result(Patterns.ask(companyActor, "getCount", timeout), timeout.duration());
                int employeeCount = (int) Await.result(Patterns.ask(employeeActor, "getCount", timeout), timeout.duration());

                System.out.println("companyCount = " + companyCount + "; employeeCount = " + employeeCount);
                System.out.println("-------------------------------");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
