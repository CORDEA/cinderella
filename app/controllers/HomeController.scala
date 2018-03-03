package controllers

import javax.inject._

import forms.TimeForm
import helpers.Timer
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import play.api.data.Forms._
import play.api.data._
import play.api.i18n.I18nSupport
import play.api.libs.json.Json
import play.api.mvc._


@Singleton
class HomeController @Inject()(
                                timer: Timer,
                                cc: ControllerComponents
                              ) extends AbstractController(cc) with I18nSupport {

  val timeForm = Form(
    mapping(
      "time" -> nonEmptyText
    )(TimeForm.apply)(TimeForm.unapply)
  )

  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  def submit() = Action { implicit request: Request[AnyContent] =>
    timeForm.bindFromRequest.fold(
      _ => BadRequest(""),
      input => {
        val date = parseDate(input.time)
        timer.next(date)
        Ok(views.html.index())
      }
    )
  }

  def current() = Action { implicit request: Request[AnyContent] =>
    Ok(Json.obj(
      "is_stopped" -> timer.isStopped,
      "current" -> timer.current
    ))
  }

  private def parseDate(date: String): DateTime = {
    val d = DateTime.parse(date, DateTimeFormat.forPattern("HH:mm"))
    DateTime.now()
      .withHourOfDay(d.hourOfDay().get())
      .withMinuteOfHour(d.minuteOfHour().get())
  }
}
