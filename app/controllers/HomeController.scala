package controllers

import javax.inject._

import helpers.Timer
import play.api.mvc._

@Singleton
class HomeController @Inject()(timer: Timer, cc: ControllerComponents) extends AbstractController(cc) {

  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }
}
