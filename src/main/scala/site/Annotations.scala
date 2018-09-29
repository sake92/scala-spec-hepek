package site

import scalatags.Text.all._
import ba.sake.hepek.implicits._
import utils.Imports._

object Annotations extends templates.SpecBlogPage {

  override def pageSettings =
    super.pageSettings
      .withTitle("Annotations")

  override def blogSettings =
    super.blogSettings
      .withSections(annotationsSection)

  /* CONTENT */
  val annotationsSection = Section(
    "Annotations",
    frag(
      s"""
        TODO
      """.md
    )
  )

}
