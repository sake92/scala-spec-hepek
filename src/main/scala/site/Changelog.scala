package site

import scalatags.Text.all._
import ba.sake.hepek.implicits._
import utils.Imports._

object Changelog extends templates.SpecBlogPage {

  override def pageSettings =
    super.pageSettings
      .withTitle("Changelog")

  override def blogSettings =
    super.blogSettings
      .withSections(changelogSection)

  def changelogSection = Section(
    "Changelog",
    frag(
      s"""
        TODO
      """.md
    )
  )

}
