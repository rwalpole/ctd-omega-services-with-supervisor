package uk.gov.nationalarchives

import cats.effect.std.Supervisor
import cats.effect.testing.scalatest.{AsyncIOSpec, CatsResourceIO}
import cats.effect.{IO, OutcomeIO, Resource}
import org.scalatest.matchers.must.Matchers.convertToAnyMustWrapper
import org.scalatest.time.SpanSugar.convertIntToGrainOfTime
import org.scalatest.wordspec.FixtureAsyncWordSpec
import uk.gov.nationalarchives.ApiServiceApp.{service, task}

class ApiServiceSpec extends FixtureAsyncWordSpec with AsyncIOSpec with CatsResourceIO[IO[OutcomeIO[Unit]]] {

  private val service = new ApiService

  private val task: IO[Unit] = service.start

  // TODO(AR) what about `IO.interruptible` - see https://typelevel.org/cats-effect/docs/tutorial#dealing-with-cancelation

  override val resource: Resource[IO, IO[OutcomeIO[Unit]]] = task.onCancel(service.stop()).background


  "cats resource specifications" should {
    "run a resource modification" in { res =>   // resource is started 1st time here when it is injected.
      resource.surround {   // resources will be started a 2nd time here when the test is evaluated (as it is explicitly converted into an IO[Unit] here)
        for {
          _ <- IO.sleep(5.seconds)
          _ <- IO.println("Test 1")
          res <- IO.pure(2 + 3).asserting(_ mustBe 5)
        } yield res
      }
    }

//    "be shared between tests" in { _ =>
//      resource.surround {
//        for {
//          _ <- IO.sleep(5.seconds)
//          _ <- IO.println("Test 2")
//          res <- IO.pure(2 + 3).asserting(_ mustBe 5)
//        } yield res
//      }
//    }
//
//    "be shared between three" in { _ =>
//      resource.surround {
//        for {
//          _ <- IO.sleep(5.seconds)
//          _ <- IO.println("Test 3")
//          res <- IO.pure(2 + 3).asserting(_ mustBe 5)
//        } yield res
//      }
//    }
  }
}
