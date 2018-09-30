package site

import scalatags.Text.all._
import ba.sake.hepek.implicits._
import utils.Imports._

object Implicits extends templates.SpecBlogPage {

  override def pageSettings =
    super.pageSettings
      .withTitle("Implicits")

  override def blogSettings =
    super.blogSettings
      .withSections(implicitsSection)

  def implicitsSection = Section(
    "Implicits",
    frag(
      s"""
        TODO
      """.md
    )
  )

}
