package net

import akka.actor.ActorSystem
import spray.routing.SimpleRoutingApp
import spray.client.pipelining._
import scala.concurrent.{Future, Await}
import scala.concurrent.duration._
import spray.http.HttpRequest
import scala.concurrent.ExecutionContext.Implicits.global
import spray.json.DefaultJsonProtocol._
import spray.http.StatusCodes
import scala.util._

object Main extends App with SimpleRoutingApp {
  
  implicit val system = ActorSystem("my-system")

  // val pipeline: HttpRequest => Future[String] = sendReceive ~> unmarshal[String]

  // val req = Post("http://www.google.com") ~> addHeader("Foo", "bar")
  // pipeline(req).recoverWith[String]{ case _ => Future { "error!" } }

  startServer(interface = "localhost", port = 8080) {
    path("go") {
      get { 
        // try { 
            def result: Future[Int] = Future.failed(new Exception("failed"))
            onComplete(result) { x => x match {
              case Success(_) => complete(StatusCodes.OK -> "good")
              case Failure(_) => complete(StatusCodes.InternalServerError -> "bad in try")
            }
          }
        // }
        // catch { 
        //   case _: Exception => complete(StatusCodes.InternalServerError -> "bad in catch")
        // }
     }  
   }
  }
} 