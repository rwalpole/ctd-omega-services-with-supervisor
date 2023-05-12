package uk.gov.nationalarchives

import cats.effect.{IO, Resource}
import cats.effect.std.Supervisor

import scala.concurrent.duration.DurationInt

class ApiService {

  def start: IO[Unit] = IO.println("Starting API Service") *> print()

  def stop(): IO[Unit] = IO.println("Closing the API Service")

  private def print(): IO[Unit] = {
    for {
      time <- IO.realTimeInstant
      _ <- IO.println(s"Current data and time is $time")
      _ <- IO.cede
      _ <- IO.sleep(1.second)
      _ <- print()
    } yield ()
  }

}

object ApiService {

  def resource: Resource[IO, ApiService] = {
    Supervisor[IO](await = false).flatMap(resource(_))
  }

  def resource(supervisor: Supervisor[IO]) : Resource[IO, ApiService] = {
      Resource.make(
        IO.pure(new ApiService())
          .flatTap(apiService => supervisor.supervise(apiService.start))
      )(_.stop()) // TODO(AR) can we use .stop() here or should this go into an .onCancel in the Acquire phase?
  }
}

