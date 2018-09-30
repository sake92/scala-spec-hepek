package site

import ba.sake.hepek.implicits._
import scalatags.Text.all._
import utils.Imports._

object Types extends templates.SpecBlogPage {

  override def pageSettings =
    super.pageSettings
      .withTitle("Types")

  override def blogSettings =
    super.blogSettings
      .withSections(functionTypesSection)

  def functionTypesSection = Section(
    "Function Types",
    frag(
      s"""
        ```ebnf
        Type              ::=  FunctionArgs ‘=>’ Type
        FunctionArgs      ::=  InfixType
                            |  ‘(’ [ ParamType {‘,’ ParamType } ] ‘)’
        ```
        
        The type ´(T_1 , \\ldots , T_n) \\Rightarrow U´ represents the set of function
        values that take arguments of types ´T1 , \\ldots , Tn´ and yield
        results of type ´U´.  In the case of exactly one argument type
        ´T \\Rightarrow U´ is a shorthand for ´(T) \\Rightarrow U´.
        An argument type of the form ´\\Rightarrow T´
        represents a [call-by-name parameter](04-basic-declarations-and-definitions.html#by-name-parameters) of type ´T´.
        
        Function types associate to the right, e.g.
        ´S \\Rightarrow T \\Rightarrow U´ is the same as
        ´S \\Rightarrow (T \\Rightarrow U)´.
        
        Function types are shorthands for class types that define `apply`
        functions.  Specifically, the ´n´-ary function type
        ´(T_1 , \\ldots , T_n) \\Rightarrow U´ is a shorthand for the class type
        `Function´_n´[´-T_1´ , … , ´T_n´, U]`. Such class
        types are defined in the Scala library for ´n´ between 0 and 22 as follows.

        ```scala
          package scala
          trait Function´_n´[´-T_1´ , ... , ´-T_n´, +R] {
            def apply(x1: ´T_1´ , ..., ´x_n´: ´T_n´): R
            override def toString = "<function>"
          }
        ```
      """.md
    )
  )

}
