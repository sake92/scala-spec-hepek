package site

import scalatags.Text.all._
import ba.sake.hepek.implicits._
import utils.Imports._

object PatternMatching extends templates.SpecBlogPage {

  override def pageSettings =
    super.pageSettings
      .withTitle("Pattern Matching")

  override def blogSettings =
    super.blogSettings
      .withSections(patternMatchingSection)

  def patternMatchingSection = Section(
    "Pattern Matching",
    frag(
      s"""
        TODO
      """.md
    )
  )

}
