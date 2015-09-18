package net

import spray.json._

case class FooWithStrings(xs: Foo.NonEmptyFoo)

object Foo extends DefaultJsonProtocol {

	type NonEmptySeq[A] = (A, Seq[A])

	type NonEmptyFoo = NonEmptySeq[String]

	def toSeq[A](neq: NonEmptySeq[A]): Seq[A] = 
		neq._1 +: neq._2

	implicit object NonEmptyModelFormat extends JsonFormat[NonEmptyFoo] {
		def write(neq: NonEmptyFoo): JsValue = {
	     val jsonStrings: Seq[JsString] = toSeq(neq).map(JsString(_))
	     JsArray( jsonStrings: _* ) 
	 	}
	   
	    def read(value: JsValue): NonEmptyFoo = {
	      def buildStringArrayOrFail(xs: Seq[JsValue]): Seq[JsString] = xs match {
		    case Seq(x@JsString(_), tail @ _ *) => x +: buildStringArrayOrFail(tail)
		    case Seq()							=> Seq()
		    case _ 					   		    => deserializationError("Array must consist of all JsString's.")
		  }

	      value match {
		    case JsArray(Seq())     				        => deserializationError("must be non-empty array of JsString's!")
		    case JsArray(Seq(head@JsString(_), tail @ _ *)) => (head.value, buildStringArrayOrFail(tail).map(_.value))
		    case _ 			  						        => deserializationError("must be non-empty array of JsString's")
	      }
		}
	}
}