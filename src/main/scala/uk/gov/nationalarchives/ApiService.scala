package uk.gov.nationalarchives

import cats.effect.IO

import java.util.concurrent.atomic.AtomicInteger
import scala.concurrent.duration.DurationInt

class ApiService {

  val counter = new AtomicInteger()

  def start: IO[Unit] = IO.blocking(counter.incrementAndGet()).flatMap(count => IO.println(s"Starting API Service: $count")) *> print()

  def stop(): IO[Unit] = IO.println("Closing the API Service")

  private def print(): IO[Unit] = {
    for {
      time <- IO.realTimeInstant
      _ <- IO.println(s"Current data and time is $time")
      _ <- IO.sleep(1.second)
      _ <- IO.cede
      _ <- print()
    } yield ()
  }

}