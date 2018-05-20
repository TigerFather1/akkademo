/*
 * Copyright (c) 2018 duanxitao All rights reserved.
 */

package com.qit.supervision;

import akka.actor.OneForOneStrategy;
import akka.actor.Props;
import akka.actor.SupervisorStrategy;
import akka.actor.UntypedActor;
import akka.japi.Function;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

/**
 * @ProjectName akkademo
 * @Description TODO
 * @Autohr DuanXiTao
 * @Date 2018/5/20 21:50
 * @Version 1.0
 */
public class Supervisor extends UntypedActor {

    /**
     * 配置自己的strategy
     * @return
     */
    @Override
    public SupervisorStrategy supervisorStrategy() {
        return new OneForOneStrategy(3, Duration.create(1, TimeUnit.MINUTES),
                new Function<Throwable, SupervisorStrategy.Directive>() {
                    @Override
                    public SupervisorStrategy.Directive apply(Throwable param) throws Exception {
                        if (param instanceof ArithmeticException) {
                            System.out.println("Meet ArithmeticException, just resume.");
                            // 继续，重新开始，恢复职位
                            return SupervisorStrategy.resume();
                        } else if (param instanceof NullPointerException) {
                            System.out.println("Meet NullPointerException, restart.");
                            return SupervisorStrategy.restart();
                        } else if (param instanceof IllegalArgumentException) {
                            System.out.println("Meet IllegalArgumentException, stop.");
                            return SupervisorStrategy.stop();
                        } else {
                            System.out.println("escalate");
                            // 使逐步上升
                            return SupervisorStrategy.escalate();
                        }
                    }
                });
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof Props) {
            getContext().actorOf((Props) message, "restartActor");
        } else {
            unhandled(message);
        }
    }
}
