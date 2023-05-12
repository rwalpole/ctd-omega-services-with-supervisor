package uk.gov.nationalarchives

import weaver.IOSuite
import cats.effect._
import cats.effect.std.Supervisor

import scala.concurrent.duration.DurationInt

object FirstSuite extends IOSuite {

  private val delay = 3

  type Res = ApiService

  // Create a Supervisor of ApiService.start() as a shared Resource
  override def sharedResource : Resource[IO, Res] = ApiService.resource

    /* Supervisor[IO](await = false).flatMap { supervisor =>
    Resource.make(
      IO.pure(new ApiService())
        .flatTap(apiService => supervisor.supervise(apiService.start))
    )(_.stop())   // TODO(AR) can we use .stop() here or should this go into an .onCancel in the Acquire phase?
  } */

  test("test 1") { supervisor =>
    for {
      _ <- IO.println(s"test 1 supervisor is: ${supervisor.hashCode()}")
      _ <- IO.println(s"Test waiting for $delay seconds...")
      _ <- IO.sleep(delay.seconds)
      _ <- IO.println(s"Test waited for $delay seconds; DONE")
    } yield expect(1 == 1)
  }

  test("test 2") { supervisor =>
    for {
      _ <- IO.println(s"test 2 supervisor is: ${supervisor.hashCode()}")
      _ <- IO.println(s"Test waiting for $delay seconds...")
      _ <- IO.sleep(delay.seconds)
      _ <- IO.println(s"Test waited for $delay seconds; DONE")
    } yield expect(1 == 1)
  }

}
