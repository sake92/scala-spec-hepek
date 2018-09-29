package pdf
import java.io.File
import scala.util.Properties
import ba.sake.hepek.core.Renderable
import ba.sake.hepek.pdf.PdfGenerator

object PdfGenApp {

  val pages: List[Renderable] = List(site.Index) ++ site.LexicalSyntax.categoryPosts

  def main(args: Array[String]): Unit = {
    val webDriverPath =
      if (Properties.isLinux) "linux64/chromedriver"
      else if (Properties.isMac) "mac64/chromedriver"
      else "win32/chromedriver.exe"

    System.setProperty("webdriver.chrome.driver", "./webdrivers/" + webDriverPath)
    val targetFolder = "./target/web/public/main"
    val file         = new File(targetFolder + "/site/pdfs/SLS.pdf")
    PdfGenerator.generate(file, targetFolder, pages)
  }
}
