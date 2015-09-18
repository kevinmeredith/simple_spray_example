package net

import spray.json._
import Foo.NonEmptySeq

class NonEmptyCustomFormatter[A](implicit ev: A => JsValue, 
							              ev2: JsValue => A, 
							              manifest: scala.reflect.Manifest[A]) extends JsonFormat[NonEmptySeq[A]] {
  override def read(json: JsValue): NonEmptySeq[A] = {

    // Throws exception upon de-serialization error!
    def buildArrayOfTypeA(xs: Seq[JsValue]): Seq[A] = xs match {
      case Seq(x, tail @ _ *) => ev2(x) +: buildArrayOfTypeA(tail)
	  case Seq()			  => Seq()
	  case _ 				  => deserializationError(s"Array must consist of all ${manifest.toString}'s")
    }

  	json match {
  	  case JsArray(Seq(head, tail @ _ *)) => ( ev2(head), buildArrayOfTypeA(tail) )
  	  case JsArray(Seq())				  => deserializationError("must be non-empty array of JsString's")
    }
  }
    
  override def write(xs: NonEmptySeq[A]): JsValue = {
    JsArray( ev(xs._1) +: xs._2.map(ev(_)): _* )
  }
}

object Foo /* extends DefaultJsonProtocol */ {

	type NonEmptySeq[A] = (A, Seq[A])
	// type NonEmptyFoo = NonEmptySeq[String]

	implicit def stringToJsValue(x: String): JsValue = JsString(x)

	implicit def jsValueToString(json: JsValue): String = json match {
		case JsString(x) => x
		case _ 		     => deserializationError(s"${json} is not a String!")
	}
}