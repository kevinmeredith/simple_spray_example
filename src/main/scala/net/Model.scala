package net

import spray.json._

case class Model(name: String, age: Int)

object Model extends DefaultJsonProtocol {
	implicit object ModelFormat extends JsonFormat[Model] {
		def write(model: Model): JsValue = JsObject(
	      "name" -> JsString(model.name),
	      "age" -> JsNumber(model.age)
	    )
	    def read(value: JsValue): Model = value match {
    		case obj @ JsObject(_) => obj.getFields("name", "age") match {
    			case Seq(JsString(n), JsNumber(a)) if (n == "foo") => Model(n, a.toInt)
    			case _											   => deserializationError("not named 'foo'!")
    		}
    		case _ => deserializationError("not a JsObject!")
	    }
	}
}