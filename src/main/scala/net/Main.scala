package net

import akka.actor.ActorSystem
import spray.routing.SimpleRoutingApp
import spray.client.pipelining._
import scala.concurrent.Future
import spray.http.HttpRequest
import scala.concurrent.ExecutionContext.Implicits.global
import spray.json.DefaultJsonProtocol._

object Main extends App with SimpleRoutingApp {
  
  implicit val system = ActorSystem("my-system")

  val pipeline: HttpRequest => Future[String] = sendReceive ~> unmarshal[String]

  startServer(interface = "localhost", port = 8080) {
    path("go") {
      get {
        complete {
          val req = Post("www.google.com/") ~> addHeader("Foo", "bar")
          pipeline(req).recoverWith[String]{ case _ => Future { "error!" } }
        }
      } 
    }    
  }
}