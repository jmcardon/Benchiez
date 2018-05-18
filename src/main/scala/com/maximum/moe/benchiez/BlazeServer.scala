package com.maximum.moe.benchiez

import java.net.InetSocketAddress
import java.nio.channels.AsynchronousChannelGroup
import java.util.concurrent.Executors

import cats.effect.{Effect, IO}
import fs2.{Stream, StreamApp, async}
import org.http4s.{Response, Status}
import org.http4s.server.blaze.BlazeBuilder
import org.http4s.server.netty.NettyBuilder

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

object BlazeServer extends StreamApp[IO] {

  def stream(args: List[String],
             requestShutdown: IO[Unit]): fs2.Stream[IO, StreamApp.ExitCode] =
    BlazeBuilder[IO]
      .bindHttp(8080, "0.0.0.0")
      .withWebSockets(false)
      .mountService(new PingService[IO].service)
      .serve
}

object NettyServer extends StreamApp[IO] {

  def stream(args: List[String],
             requestShutdown: IO[Unit]): fs2.Stream[IO, StreamApp.ExitCode] =
    NettyBuilder[IO]
      .bindHttp(8080, "0.0.0.0")
      .withWebSockets(false)
      .mountService(new PingService[IO].service)
      .serve
}

object EmberServer extends StreamApp[IO] {

  def stream(args: List[String],
             requestShutdown: IO[Unit]): Stream[IO, StreamApp.ExitCode] = {
    val address = "0.0.0.0"
    val port = 8080
    val inetAddress = new InetSocketAddress(address, port)

    val acg = AsynchronousChannelGroup.withFixedThreadPool(
      100,
      Executors.defaultThreadFactory)

    // Defaults
    val maxConcurrency: Int = Int.MaxValue
    val receiveBufferSize: Int = 256 * 1024
    val maxHeaderSize: Int = 10 * 1024
    val requestHeaderReceiveTimeout: Duration = 5.seconds

    for {
      terminatedSignal <- Stream.eval(
        async.signalOf[IO, Boolean](false)(Effect[IO], global))
      exitCode <- io.chrisdavenport.ember.server[IO](
        maxConcurrency,
        receiveBufferSize,
        maxHeaderSize,
        requestHeaderReceiveTimeout,
        inetAddress,
        new PingService[IO].service,
        Response.notFound[IO],
        _ => Stream(Response[IO](Status.InternalServerError)),
        (_, _, _) => Stream.empty,
        global,
        acg,
        terminatedSignal
      )
    } yield exitCode
  }
}
