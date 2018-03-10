package helpers

import javax.inject.{Inject, Singleton}

import akka.actor.{ActorSystem, Cancellable}
import com.github.nscala_time.time.Imports._
import org.joda.time.format.PeriodFormatterBuilder
import org.joda.time.{DateTime, Duration}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

@Singleton
class Timer @Inject()(system: ActorSystem) {

  var ring: () => Unit = () => {}

  private var currentTimer: Option[Cancellable] = Option.empty
  private var endDate: Option[DateTime] = Option.empty

  def isStopped: Boolean = endDate.isEmpty

  def current: String = {
    val formatter = new PeriodFormatterBuilder()
      .appendDays()
      .appendSuffix("d")
      .appendHours()
      .appendSuffix("h")
      .appendMinutes()
      .appendSuffix("m")
      .appendSeconds()
      .appendSuffix("s")
      .toFormatter

    formatter.print(
      new Duration(
        if (endDate.isEmpty) {
          0
        } else {
          if (endDate.get.isAfterNow) {
            (DateTime.now() to endDate.get).millis
          } else {
            0
          }
        }
      ).toPeriod()
    )
  }

  def cancel(): Unit = {
    currentTimer.foreach { c =>
      c.cancel()
    }
  }

  def next(date: DateTime): Unit = {
    if (currentTimer.nonEmpty) {
      return
    }
    val duration = (DateTime.now() to date).millis.millis
    endDate = Option.apply(date)
    currentTimer = Option.apply {
      system.scheduler.scheduleOnce(duration) {
        ring()
        currentTimer = Option.empty
      }
    }
  }
}
