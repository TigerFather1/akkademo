/*
 * Copyright (c) 2018 duanxitao All rights reserved.
 */

package com.qit.agent;

import akka.actor.*;
import akka.agent.Agent;
import akka.dispatch.ExecutionContexts;
import akka.dispatch.Futures;
import akka.dispatch.Mapper;
import akka.dispatch.OnComplete;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.typesafe.config.ConfigFactory;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @ProjectName akkademo
 * @Description TODO
 * @Autohr DuanXiTao
 * @Date 2018/5/22 22:45
 * @Version 1.0
 */
public class AgentTest extends UntypedActor {
    private final LoggingAdapter LOG = Logging.getLogger(getContext().system(), this);

    public static CountDownLatch latch = new CountDownLatch(10);
    public static Agent<Integer> countAgent = Agent.create(10, ExecutionContexts.global());
    public static ConcurrentLinkedQueue<Future<Integer>> queue = new ConcurrentLinkedQueue<>();

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof Integer) {
            for (int i = 0; i < 10000; i++) {
                Future<Integer> future =  countAgent.alter(new Mapper<Integer, Integer>() {
                    @Override
                    public Integer apply(Integer parameter) {
                        return parameter + 1;
                    }
                });
                queue.add(future);
            }
            getContext().stop(getSelf());
        } else {
            unhandled(message);
        }
    }

    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("inbox", ConfigFactory.load("akka.conf"));
        ActorRef[] actorRefs = new ActorRef[10];
        for (int i = 0; i < 10; i++) {
            actorRefs[i] = system.actorOf(Props.create(AgentTest.class), "AtentTest" + i);
        }
        Inbox inbox  = Inbox.create(system);
        for (ActorRef ref : actorRefs) {
            inbox.send(ref, 1);
            inbox.watch(ref);
        }
        System.out.println("countAgent 1: " + countAgent.get());

        int closeCount = 0;
        while(true) {
            Object obj = null;
            try {
                obj = inbox.receive(Duration.create(1, TimeUnit.SECONDS));
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
            if (obj instanceof Terminated) {
                closeCount++;
                if (closeCount == actorRefs.length) {
                    break;
                }
            } else {
                System.out.println("obj: " + obj);
            }
        }

        System.out.println("countAgent 2; " + countAgent.get());
        Futures.sequence(queue, system.dispatcher()).onComplete(new OnComplete<Iterable<Integer>>() {
            @Override
            public void onComplete(Throwable failure, Iterable<Integer> success) throws Throwable {
                System.out.println("countAgent 3: " + countAgent.get());
                system.terminate();
            }
        }, system.dispatcher());
    }
}
