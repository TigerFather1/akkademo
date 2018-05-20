/*
 * Copyright (c) 2018 duanxitao All rights reserved.
 */

package com.qit.lifecycle;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 * @ProjectName akkademo
 * @Description TODO
 * @Autohr DuanXiTao
 * @Date 2018/5/20 18:33
 * @Version 1.0
 */
public class MyWork extends UntypedActor {
    LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

    public static enum Msg {
        WORKING, DONE, CLOSE;
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        try {
            if (message == Msg.WORKING) {
                logger.info("I am working");
            } else if (message == Msg.DONE) {
                logger.info("stop working");
            } else if (message == Msg.CLOSE) {
                logger.info("stop close");
                getSender().tell(Msg.CLOSE, getSelf());
                getContext().stop(getSelf());
            } else {
                unhandled(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void preStart() throws Exception {
        logger.info("myWork starting ...");
    }

    @Override
    public void postStop() throws Exception {
        logger.info("myWork stopping ...");
    }
}
