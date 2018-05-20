/*
 * Copyright (c) 2018 duanxitao All rights reserved.
 */

package com.qit.lifecycle;

import akka.actor.ActorRef;
import akka.actor.Terminated;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 * @ProjectName akkademo
 * @Description TODO
 * @Autohr DuanXiTao
 * @Date 2018/5/20 18:40
 * @Version 1.0
 */
public class WatchActor extends UntypedActor {
    LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

    /**
     * 监听一个Actor
     * @param actorRef
     */
    public WatchActor(ActorRef actorRef) {
        getContext().watch(actorRef);
    }

    public WatchActor() {

    }

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof Terminated) {
            // 这里简单打印一下， 然后停止System
            logger.error(((Terminated)message).getActor().path() + " has Terminated. now shutdown the system");
            getContext().system().stop(getSelf());
        } else {
            unhandled(message);
        }
    }
}
