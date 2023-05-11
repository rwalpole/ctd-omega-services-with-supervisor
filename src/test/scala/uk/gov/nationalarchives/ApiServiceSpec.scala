package uk.gov.nationalarchives

import cats.effect.std.Supervisor
import cats.effect.testing.scalatest.{AsyncIOSpec, CatsResourceIO}
import cats.effect.{IO, Resource}
import org.scalatest.matchers.must.Matchers.convertToAnyMustWrapper
import org.scalatest.time.SpanSugar.convertIntToGrainOfTime
import org.scalatest.wordspec.FixtureAsyncWordSpec

class ApiServiceSpec extends FixtureAsyncWordSpec with AsyncIOSpec with CatsResourceIO[Supervisor[IO]] {

  private val service = new ApiService

  private val task: IO[Unit] = service.start.onCancel(service.stop()) // TODO(AR) the `service.stop()` is never run due to a known bug - https://github.com/typelevel/cats-effect-testing/issues/300#issuecomment-1544442344

  override val resource: Resource[IO, Supervisor[IO]] = Supervisor[IO](await = false)

  "cats resource specifications" should {
    "run a resource modification" in {supervisor =>
      for {
        //fiber <- supervisor.supervise[Unit](task.foreverM)    // TODO(AR) the `foreverM` is not needed here as `task` i.e. `service.start` will run forever
        fiber <- supervisor.supervise[Unit](task)
        _ <- IO.sleep(5.seconds)
        res <- IO.pure(2 + 3).asserting(_ mustBe 5)
//        _ <- fiber.cancel         // TODO(AR) I don't think this is required because the Release of the Resource will stop the supervisor, which will stop all fibers that are supervised
      } yield res
    }

//    "be shared between tests" in { supervisor =>
//      for {
//        fiber <- supervisor.supervise[Unit](task.foreverM)
//        _ <- IO.sleep(5.seconds)
//        res <- IO.pure(2 + 3).asserting(_ mustBe 5)
//        _ <- fiber.cancel
//      } yield res
//    }
  }
}
