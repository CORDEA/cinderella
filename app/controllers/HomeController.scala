package controllers

import javax.inject._

import com.github.nscala_time.time.Imports._
import forms.TimeForm
import helpers.Timer
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import play.api.data.Forms._
import play.api.data._
import play.api.i18n.I18nSupport
import play.api.mvc._

import scala.concurrent.duration._

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
        val duration = (DateTime.now() to date).millis
        timer.next(duration.millis)
        Ok(views.html.index())
      }
    )
  }

  private def parseDate(date: String): DateTime = {
    DateTime.parse(date, DateTimeFormat.forPattern("HH:mm"))
  }
}
