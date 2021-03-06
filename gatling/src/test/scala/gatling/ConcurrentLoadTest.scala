package gatling

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._

class ConcurrentLoadTest extends Simulation {
  val baseUrl = "http://52.43.212.119:8080"
  val route = "/"
  val constant = 50000

  val httpConf = http
    .baseURL(baseUrl)
    .acceptHeader("text/html;q=0.9,*/*;q=0.8")
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .contentTypeHeader("text/plain")
    .userAgentHeader("Mozilla/5.0 (Windows NT 5.1; rv:31.0) ScalaBench Ayy")
    .shareConnections

  val scn = scenario(s"Hitting $baseUrl$route")
    .exec(
      http("request_1")
        .get(route))

  setUp(
    scn.inject(constantUsersPerSec(constant).during(180.seconds))
  ).protocols(httpConf)
}
