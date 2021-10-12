package fixtures

import com.typesafe.config.{Config, ConfigFactory}
import play.api.Configuration

object ConfigurationFixtures {

  def emptyConf = new Configuration(ConfigFactory.empty())

  def awsConf: Config = ConfigFactory.load()

}
