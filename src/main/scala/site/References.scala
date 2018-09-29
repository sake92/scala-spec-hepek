package site

import scalatags.Text.all._
import ba.sake.hepek.implicits._
import utils.Imports._

object References extends templates.SpecBlogPage {

  override def pageSettings =
    super.pageSettings
      .withTitle("References")

  override def blogSettings =
    super.blogSettings
      .withSections(referencesSection)

  /* CONTENT */
  val referencesSection = Section(
    "References",
    frag(
      s"""
        TODO
      """.md
    )
  )

}
