/*
 * Copyright (c) 2018 duanxitao All rights reserved.
 */

package com.qit.inbox;

import akka.actor.*;
import com.typesafe.config.ConfigFactory;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

/**
 * @ProjectName akkademo
 * @Description TODO
 * @Autohr DuanXiTao
 * @Date 2018/5/20 23:06
 * @Version 1.0
 */
public class InboxMain {
    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("inbox", ConfigFactory.load("akka.conf"));
        ActorRef inboxTest = system.actorOf(Props.create(InboxTest.class), "InboxTest");

        // 创建system的邮箱
        Inbox inbox = Inbox.create(system);
        // 这个邮箱监听inboxTest这个actor发送来的消息
        inbox.watch(inboxTest);

        // system用自己的邮箱向actor发送消息
        inbox.send(inboxTest, InboxTest.Msg.WORKING);
        inbox.send(inboxTest, InboxTest.Msg.DONE);
        inbox.send(inboxTest, InboxTest.Msg.CLOSE);

        while (true) {
            try {
                Object receiveMsg = inbox.receive(Duration.create(2, TimeUnit.SECONDS));
                if (receiveMsg == InboxTest.Msg.CLOSE) {
                    System.out.println("InboxTest actor is closing");
                } else if (receiveMsg instanceof Terminated) {
                    // 收到actor中断的消息
                    System.out.println("InboxTest actor is closed");
                    system.terminate();
                } else {
                    System.out.println(receiveMsg);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
