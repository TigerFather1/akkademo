/*
 * Copyright (c) 2018 duanxitao All rights reserved.
 */

package com.qit.inbox;

import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.typesafe.config.ConfigFactory;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;


/**
 * @ProjectName akkademo
 * @Description TODO
 * @Autohr DuanXiTao
 * @Date 2018/5/20 22:44
 * @Version 1.0
 */
public class InboxTest extends UntypedActor {
    private final LoggingAdapter LOG = Logging.getLogger(getContext().system(), this);

    public enum Msg {
        WORKING, DONE, CLOSE;
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message == Msg.WORKING) {
            LOG.info("I am working");
        } else if (message == Msg.DONE) {
            LOG.info("I am done");
        } else if (message == Msg.CLOSE) {
            LOG.info("I am close");
            getSender().tell(Msg.CLOSE, getSelf());
            getContext().stop(getSelf());
        } else {
            unhandled(message);
        }
    }
}
