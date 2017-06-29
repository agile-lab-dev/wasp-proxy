package it.agilelab.bigdata.wasp.core.utils

import java.util.Properties

import com.typesafe.config.Config

/**
  * Created by Agile Lab s.r.l. on 29/06/2017.
  */
object ConfigUtils {
  def propsFromConfig(config: Config): Properties = {
    import scala.collection.JavaConversions._

    val props = new Properties()

    val map: Map[String, Object] = config.entrySet().map({ entry =>
      entry.getKey -> entry.getValue.unwrapped()
    })(collection.breakOut)

    props.putAll(map)
    props
  }

}
