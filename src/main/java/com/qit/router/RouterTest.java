/*
 * Copyright (c) 2018 duanxitao All rights reserved.
 */

package com.qit.router;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.Terminated;
import akka.actor.UntypedActor;
import akka.routing.ActorRefRoutee;
import akka.routing.RoundRobinRoutingLogic;
import akka.routing.Routee;
import akka.routing.Router;
import com.qit.inbox.InboxTest;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @ProjectName akkademo
 * @Description TODO
 * @Autohr DuanXiTao
 * @Date 2018/5/20 23:17
 * @Version 1.0
 */
public class RouterTest extends UntypedActor {
    public Router router;
    {
        ArrayList<Routee> routees = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ActorRef worker = getContext().actorOf(Props.create(InboxTest.class), "worker_" + i);
            getContext().watch(worker);
            routees.add(new ActorRefRoutee(worker));
        }
        /**
         * RoundRobinRoutingLogic: 轮询
         * BroadcastRoutingLogic: 广播
         * RandomRoutingLogic: 随机
         * SmallestMailboxRoutingLogic: 空闲
         */
        router  = new Router(new RoundRobinRoutingLogic(), routees);
    }

    public static AtomicBoolean flag = new AtomicBoolean(true);

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof InboxTest.Msg) {
            router.route(message, getSender());
        } else if (message instanceof Terminated) {
            router = router.removeRoutee(((Terminated) message).actor());
            System.out.println(((Terminated) message).actor().path() + " 该actor已经删除，router.size = " + router.routees().size());
            if (router.routees().size() == 0) {
                System.out.println("没有可用的actor了，系统关闭");
                flag.compareAndSet(true, false);
                getContext().system().terminate();
            }
        } else {
            unhandled(message);
        }
    }
}
