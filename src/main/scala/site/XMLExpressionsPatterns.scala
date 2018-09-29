package site

import scalatags.Text.all._
import ba.sake.hepek.implicits._
import utils.Imports._

object XMLExpressionsPatterns extends templates.SpecBlogPage {

  override def pageSettings =
    super.pageSettings
      .withTitle("XML Expressions and Patterns")

  override def blogSettings =
    super.blogSettings
      .withSections(xmlExpressionsPatternsSection)

  /* CONTENT */
  val xmlExpressionsPatternsSection = Section(
    "XML Expressions and Patterns",
    frag(
      s"""
        TODO
      """.md
    )
  )

}
