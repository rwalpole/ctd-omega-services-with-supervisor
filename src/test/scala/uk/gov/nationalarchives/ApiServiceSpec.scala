package uk.gov.nationalarchives

import cats.effect.std.Supervisor
import cats.effect.testing.scalatest.{AsyncIOSpec, CatsResourceIO}
import cats.effect.{IO, Resource}
import org.scalatest.matchers.must.Matchers.convertToAnyMustWrapper
import org.scalatest.time.SpanSugar.convertIntToGrainOfTime
import org.scalatest.wordspec.FixtureAsyncWordSpec

class ApiServiceSpec extends FixtureAsyncWordSpec with AsyncIOSpec with CatsResourceIO[Supervisor[IO]] {

  private val service = new ApiService

  private val task = service.start

  override val resource: Resource[IO, Supervisor[IO]] = {
    task.onCancel(service.stop()) // this is never run
    Supervisor[IO](await = false) //.onCancel(service.stop()) // not allowed here
  }

  "cats resource specifications" should {
    "run a resource modification" in {supervisor =>
      for {
        fiber <- supervisor.supervise[Unit](task.foreverM)
        _ <- IO.sleep(5.seconds)
        res <- IO.pure(2 + 3).asserting(_ mustBe 5)
        _ <- fiber.cancel
      } yield res
    }

    "be shared between tests" in { supervisor =>
      for {
        fiber <- supervisor.supervise[Unit](task.foreverM)
        _ <- IO.sleep(5.seconds)
        res <- IO.pure(2 + 3).asserting(_ mustBe 5)
        _ <- fiber.cancel
      } yield res
    }
  }
}
