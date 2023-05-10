package uk.gov.nationalarchives

import cats.effect.std.Supervisor
import cats.effect.testing.scalatest.{AsyncIOSpec, CatsResourceIO}
import cats.effect.{IO, OutcomeIO, Resource}
import org.scalatest.matchers.must.Matchers.convertToAnyMustWrapper
import org.scalatest.time.SpanSugar.convertIntToGrainOfTime
import org.scalatest.wordspec.FixtureAsyncWordSpec
import uk.gov.nationalarchives.ApiServiceApp.{service, task}

class ApiServiceSpec extends FixtureAsyncWordSpec with AsyncIOSpec with CatsResourceIO[ApiService] {

  //private val service = new ApiService

  //private val task: IO[Unit] = service.start

  //override val resource: Resource[IO, IO[OutcomeIO[Unit]]] = task.onCancel(service.stop()).background

  override val resource: Resource[IO, ApiService] = Resource.make({
    val service = IO.delay(new ApiService)
    service.flatTap(_.start)
  })(_.stop())


  "cats resource specifications" should {
    "run a resource modification" in { _ =>
      //resource.surround {
        for {
          _ <- IO.sleep(5.seconds)
          _ <- IO.println("Test 1")
          res <- IO.pure(2 + 3).asserting(_ mustBe 5)
        } yield res
      //}
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
