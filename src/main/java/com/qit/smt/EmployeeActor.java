/*
 * Copyright (c) 2018 duanxitao All rights reserved.
 */

package com.qit.smt;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.transactor.Coordinated;
import scala.concurrent.stm.Ref;
import scala.concurrent.stm.japi.STM;

/**
 * @ProjectName akkademo
 * @Description TODO
 * @Autohr DuanXiTao
 * @Date 2018/5/22 22:00
 * @Version 1.0
 */
public class EmployeeActor extends UntypedActor {
    private final LoggingAdapter LOG = Logging.getLogger(getContext().system(), this);
    private Ref.View<Integer> count = STM.newRef(20);

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof Coordinated) {
            Coordinated coordinated = (Coordinated) message;
            int downCount = (int) coordinated.getMessage();

            try {
                coordinated.atomic(() -> STM.increment(count, downCount));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if ("getCount".equals(message)) {
            getSender().tell(count.get(), getSelf());
        } else {
            unhandled(message);
        }
    }
}
