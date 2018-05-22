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
 * @Date 2018/5/22 21:41
 * @Version 1.0
 */
public class CompanyActor extends UntypedActor {
    private final LoggingAdapter LOG = Logging.getLogger(getContext().system(), this);
    //定义账户余额
    private Ref.View<Integer> count = STM.newRef(100);

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof Coordinated) {
            Coordinated coordinated = (Coordinated) message;
            int downCount = (int) coordinated.getMessage();
            // 通知Employee的账户增加资金
            SMPMain.employeeActor.tell(coordinated.coordinate(downCount), getSelf());

            try {
                coordinated.atomic(() -> {
                    if (count.get() < downCount) {
                        throw new RuntimeException("余额不足");
                    }
                    STM.increment(count, -downCount);
                });
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
