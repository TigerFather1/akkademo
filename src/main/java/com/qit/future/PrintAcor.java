/*
 * Copyright (c) 2018 duanxitao All rights reserved.
 */

package com.qit.future;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 * @ProjectName akkademo
 * @Description TODO
 * @Autohr DuanXiTao
 * @Date 2018/5/22 21:09
 * @Version 1.0
 */
public class PrintAcor extends UntypedActor {
    private final LoggingAdapter LOG = Logging.getLogger(getContext().system(), this);

    @Override
    public void onReceive(Object message) throws Throwable {
        LOG.info("akka.future.PrintActor.onReceive: " + message);
        if (message instanceof Integer) {
            LOG.info("print: " + message);
        } else {
            unhandled(message);
        }
    }
}
