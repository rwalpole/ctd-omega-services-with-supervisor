package uk.gov.nationalarchives

import cats.effect.IO

import scala.concurrent.duration.DurationInt

class ApiService {

  def start: IO[Unit] = IO.println("Starting API Service") *> print().foreverM

  def stop(): IO[Unit] = IO.println("Closing the API Service")

  private def print(): IO[Unit] =
    for {
      time <- IO.realTimeInstant
      _ <- IO.println(s"Current data and time is $time")
      _ <- IO.sleep(1.second)
    } yield ()

}