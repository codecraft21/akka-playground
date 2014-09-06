package ch.codecraft.akkaplayground

import akka.actor.Actor.Receive
import akka.actor.{ActorRef, ActorSystem, Props, Actor, Inbox}
import scala.concurrent.duration._

/**
 * Starting with HelloAkkaScala from activator-hello-akka
 *
 * Created by codecraft on 2014-09-06.
 */
object AkkaPlayground {

  def main(args: Array[String]): Unit = {
    println("AkkaPlayground")
  }

}

case object Greet
case class WhoToGreet(who: String)
case class Greeting(message: String)

class Greeter extends Actor {
  var greeting = ""

  override def receive: Receive = {
    case WhoToGreet(who) => greeting = s"Hello, $who"
    case Greeting => sender ! Greeting(greeting)
  }

}