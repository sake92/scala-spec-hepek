package site

import scalatags.Text.all._
import ba.sake.hepek.implicits._
import utils.Imports._

object BasicDeclarationsDefinitions extends templates.SpecBlogPage {

  override def pageSettings =
    super.pageSettings
      .withTitle("Basic Declarations and Definitions")

  override def blogSettings =
    super.blogSettings
      .withSections(basicDeclarationsDefinitionsSection)

  /* CONTENT */
  val basicDeclarationsDefinitionsSection = Section(
    "Basic Declarations and Definitions",
    frag(
      s"""
        TODO
      """.md
    )
  )

}
