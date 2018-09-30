package site

import scalatags.Text.all._
import ba.sake.hepek.implicits._
import utils.Imports._

object SyntaxSummary extends templates.SpecBlogPage {

  override def pageSettings =
    super.pageSettings
      .withTitle("Syntax Summary")

  override def blogSettings =
    super.blogSettings
      .withSections(syntaxSummarySection)

  def syntaxSummarySection = Section(
    "Syntax Summary",
    frag(
      s"""
        TODO
      """.md
    )
  )

}
