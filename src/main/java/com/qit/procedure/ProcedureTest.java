/*
 * Copyright (c) 2018 duanxitao All rights reserved.
 */

package com.qit.procedure;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Procedure;
import com.qit.inbox.InboxTest;

/**
 * @ProjectName akkademo
 * @Description TODO
 * @Autohr DuanXiTao
 * @Date 2018/5/22 20:29
 * @Version 1.0
 */
public class ProcedureTest extends UntypedActor {
    private final LoggingAdapter LOG = Logging.getLogger(getContext().system(), this);

    public enum Msg {
        PLAY, SLEEP
    }

    Procedure<Object> happy = new Procedure<Object>() {
        @Override
        public void apply(Object param) throws Exception {
            LOG.info("I am happy! " + param);
            if (param == Msg.PLAY) {
                getSender().tell("I am already happy!!", getSelf());
                LOG.info("I am already happy!");
            } else if (param == Msg.SLEEP) {
                LOG.info("I don't like sleep!");
                getContext().become(angry);
            }
        }
    };

    Procedure<Object> angry = new Procedure<Object>() {
        @Override
        public void apply(Object param) throws Exception {
            LOG.info("I am angray! "+ param);
            if(param ==Msg.SLEEP){
                getSender().tell("I am alrady angray!!", getSelf());
                LOG.info("I am alrady angray!!");
            } else if(param ==Msg.PLAY) {
                LOG.info("I like play.");
                getContext().become(happy);
            } else {
                unhandled(param);
            }
        }
    };

    @Override
    public void onReceive(Object message) throws Throwable {
        LOG.info("onReceive msg: " + message);
        if (message == Msg.SLEEP) {
            getContext().become(angry);
        } else if (message == Msg.PLAY) {
            getContext().become(happy);
        } else {
            unhandled(message);
        }
    }
}
