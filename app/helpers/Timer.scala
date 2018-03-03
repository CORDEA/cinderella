package helpers

import javax.inject.{Inject, Singleton}

import akka.actor.{ActorSystem, Cancellable}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.FiniteDuration

@Singleton
class Timer @Inject()(system: ActorSystem) {

  var ring: () => Unit = () => {}

  private var currentTimer: Option[Cancellable] = Option.empty

  def cancel(): Unit = {
    currentTimer.foreach { c =>
      c.cancel()
    }
  }

  def next(duration: FiniteDuration): Unit = {
    if (currentTimer.nonEmpty) {
      return
    }
    currentTimer = Option.apply {
      system.scheduler.scheduleOnce(duration) {
        ring()
        currentTimer = Option.empty
      }
    }
  }
}
