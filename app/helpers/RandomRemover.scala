package helpers

import java.io.File
import javax.inject.{Inject, Singleton}

import scala.util.Random

@Singleton
class RandomRemover @Inject()() {

  def remove(path: String): Unit = {
    val file = Random.shuffle(new File(path).listFiles().toList).head
    removeAll(file)
  }

  private def removeAll(file: File): Unit = {
    if (file.isDirectory) {
      val childFiles = file.listFiles
      childFiles.filter(_.isDirectory).foreach(removeAll)
    }
    file.delete()
  }
}
