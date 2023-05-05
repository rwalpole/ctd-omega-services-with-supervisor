package uk.gov.nationalarchives

import cats.effect.IO
import cats.effect.kernel.Fiber
import cats.effect.std.Supervisor

import scala.concurrent.duration.DurationInt

class ApiService {

  def start: IO[Unit] = print().foreverM

  def print() =
    for {
      time <- IO.realTimeInstant
      _ <- IO.println(s"Current data and time is $time")
      _ <- IO.sleep(1.second)
    } yield ()

  def stop() = IO.println("Closing the background task")

}
