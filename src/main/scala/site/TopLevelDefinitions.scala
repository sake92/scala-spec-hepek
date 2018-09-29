package site

import scalatags.Text.all._
import ba.sake.hepek.implicits._
import utils.Imports._

object TopLevelDefinitions extends templates.SpecBlogPage {

  override def pageSettings =
    super.pageSettings
      .withTitle("Top-Level Definitions")

  override def blogSettings =
    super.blogSettings
      .withSections(topLevelDefinitionsSection)

  /* CONTENT */
  val topLevelDefinitionsSection = Section(
    "Top-Level Definitions",
    frag(
      s"""
        TODO
      """.md
    )
  )

}
