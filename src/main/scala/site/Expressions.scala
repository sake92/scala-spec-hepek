package site

import scalatags.Text.all._
import ba.sake.hepek.implicits._
import utils.Imports._

object Expressions extends templates.SpecBlogPage {

  override def pageSettings =
    super.pageSettings
      .withTitle("Expressions")

  override def blogSettings =
    super.blogSettings
      .withSections(expressionsSection)

  def expressionsSection = Section(
    "Expressions",
    frag(
      s"""
        TODO
      """.md
    )
  )

}
