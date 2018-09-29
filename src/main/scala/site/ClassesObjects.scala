package site

import scalatags.Text.all._
import ba.sake.hepek.implicits._
import utils.Imports._

object ClassesObjects extends templates.SpecBlogPage {

  override def pageSettings =
    super.pageSettings
      .withTitle("Classes and Objects")

  override def blogSettings =
    super.blogSettings
      .withSections(classesObjectsSection)

  /* CONTENT */
  val classesObjectsSection = Section(
    "Classes and Objects",
    frag(
      s"""
        TODO
      """.md
    )
  )

}
