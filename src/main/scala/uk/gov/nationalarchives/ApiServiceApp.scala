package uk.gov.nationalarchives

import cats.effect.{IO, IOApp}

object ApiServiceApp extends IOApp.Simple {

  private val service = new ApiService

  private val task: IO[Unit] = service.start

  private val backgroundApiService = task.onCancel(service.stop()).background

  val run: IO[Unit] = backgroundApiService.useForever



}
