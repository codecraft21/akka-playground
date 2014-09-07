package ch.codecraft.akkaplayground

import akka.actor.{ActorRef, ActorSystem, Props, Actor, Inbox}
import scala.concurrent.duration._

/**
 * Starting with HelloAkkaScala from activator-hello-akka
 *
 * Created by codecraft on 2014-09-06.
 */

case object Greet

case class WhoToGreet(who: String)

case class Greeting(message: String)

class Greeter extends Actor {
  var greeting = ""

  override def receive: Receive = {
    case WhoToGreet(who) => greeting = s"Hello, $who"
    case Greet => sender ! Greeting(greeting)
  }

}

class GreetPrinter extends Actor {
  override def receive: Actor.Receive = {
    case Greeting(message) => println(message)
  }
}

object AkkaPlayground {

  def main(args: Array[String]): Unit = {
    println("AkkaPlayground starting!")
    helloWorld()
    println("AkkaPlayground done!")
  }

  def helloWorld(): Unit = {
    val system = ActorSystem("akkaplayground")
    val greeter = system.actorOf(Props[Greeter], "greeter")
    val inbox = Inbox.create(system)
    println("set up")

    greeter.tell(WhoToGreet("codecraft"), ActorRef.noSender)
    inbox.send(greeter, Greet)
    println("sent 1")

    val Greeting(message1) = inbox.receive(5.seconds)
    println(s"Greeting: $message1")

    greeter.tell(WhoToGreet("typesafe"), ActorRef.noSender)
    inbox.send(greeter, Greet)
    println("sent 2")

    val Greeting(message2) = inbox.receive(5.seconds)
    println(s"Greeting: $message2")

    val greetPrinter = system.actorOf(Props[GreetPrinter])
    system.scheduler.schedule(0.seconds, 1.second, greeter, Greet)(system.dispatcher, greetPrinter)
  }

}