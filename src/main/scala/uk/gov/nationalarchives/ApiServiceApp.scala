package uk.gov.nationalarchives

import cats.effect.{IO, IOApp}

import scala.concurrent.duration.DurationInt

object ApiServiceApp extends IOApp.Simple {

  private val service = new ApiService

  private val task = service.start

  private val backgroundApiService = task.onCancel(service.stop()).background

  val run: IO[Unit] = backgroundApiService.useForever



}
