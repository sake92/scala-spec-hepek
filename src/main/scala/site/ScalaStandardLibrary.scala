package site

import scalatags.Text.all._
import ba.sake.hepek.implicits._
import utils.Imports._

object ScalaStandardLibrary extends templates.SpecBlogPage {

  override def pageSettings =
    super.pageSettings
      .withTitle("The Scala Standard Library")

  override def blogSettings =
    super.blogSettings
      .withSections(scalaStandardLibrarySection)

  def scalaStandardLibrarySection = Section(
    "The Scala Standard Library",
    frag(
      s"""
        TODO
      """.md
    )
  )

}
