package com.maximum.moe.benchiez

import cats.effect.Effect
import cats.syntax.flatMap._
import io.circe.Json
import org.http4s.HttpService
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl

class PingService[F[_]: Effect] extends Http4sDsl[F] {

  val service: HttpService[F] = {
    HttpService[F] {
      case GET -> Root =>
        Ok("SmallText")
      case GET -> Root / "greeting" / name =>
        Ok(Json.obj("greeting" -> Json.fromString("name")))
      case r @ POST -> Root / "consumeBody" =>
        r.body.compile.drain >> Ok()
    }
  }
}
