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

  def classesObjectsSection = Section(
    "Classes and Objects",
    frag(
      s"""
        Classes and Objects
      """.md
    )
  )

  def classMembersSection = Section(
    "Class Members",
    frag(
      s"""
        Class Members
      """.md
    )
  )

}
