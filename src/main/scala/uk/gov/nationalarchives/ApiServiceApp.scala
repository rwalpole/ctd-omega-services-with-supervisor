package uk.gov.nationalarchives

import cats.effect.std.Supervisor
import cats.effect.{IO, IOApp}

import scala.concurrent.duration.DurationInt
import cats.effect.implicits._

object ApiServiceApp extends IOApp.Simple {

  private val service = new ApiService

  private val task = service.start

  val run: IO[Unit] = Supervisor[IO](await = false).use { supervisor =>
    for {
      _ <- supervisor.supervise[Unit](task.foreverM)
      _ <- IO.sleep(5.seconds).foreverM
    } yield ()
  }.onCancel(service.stop())




}
