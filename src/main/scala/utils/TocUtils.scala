package utils

import ba.sake.hepek.core.RelativePath
import scalatags.Text.all._
import utils.Imports._

object TocUtils {

  def toc(implicit renderingPage: RelativePath) =
    ul(
      site.LexicalSyntax.categoryPosts.map { mp =>
        li(
          hyperlink(renderingPage.relTo(mp))(mp.pageSettings.title)
        )
      }
    )
}
