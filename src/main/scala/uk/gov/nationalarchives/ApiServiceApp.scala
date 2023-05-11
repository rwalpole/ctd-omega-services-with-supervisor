package uk.gov.nationalarchives

import cats.effect.{ExitCode, IO, IOApp}

import scala.concurrent.duration.DurationInt

object ApiServiceApp extends IOApp {

  override def run(args: List[String]): IO[ExitCode] = {

    val service = new ApiService
    service.start
      .onCancel(service.stop())
      .as(ExitCode.Success)

  }
}
