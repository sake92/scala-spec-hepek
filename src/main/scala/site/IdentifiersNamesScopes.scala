package site

import ba.sake.hepek.implicits._
import scalatags.Text.all._
import utils.Imports._

object IdentifiersNamesScopes extends templates.SpecBlogPage {

  override def pageSettings =
    super.pageSettings
      .withTitle("Identifiers, Names & Scopes")

  override def blogSettings =
    super.blogSettings
      .withSections(identifiersNamesScopesSection)

  /* CONTENT */
  val identifiersNamesScopesSection = Section(
    "Identifiers, Names and Scopes",
    frag(
      s"""
        Names in Scala identify types, values, methods, and classes which are
          collectively called _entities_. Names are introduced by local
          [definitions and declarations](04-basic-declarations-and-definitions.html#basic-declarations-and-definitions),
          [inheritance](05-classes-and-objects.html#class-members),
          [import clauses](04-basic-declarations-and-definitions.html#import-clauses), or
          [package clauses](09-top-level-definitions.html#packagings)
          which are collectively called _bindings_.
          
        Bindings of different kinds have a precedence defined on them:

        1. Definitions and declarations that are local, inherited, or made
           available by a package clause and also defined in the same compilation unit
           as the reference to them, have highest precedence.
        1. Explicit imports have next highest precedence.
        1. Wildcard imports have next highest precedence.
        1. Definitions made available by a package clause, but not also defined in the
           same compilation unit as the reference to them, as well as imports which
           are supplied by the compiler but not explicitly written in source code,
           have lowest precedence.
        
        There are two different name spaces, one for [types](03-types.html#types)
        and one for [terms](06-expressions.html#expressions). The same name may designate a
        type and a term, depending on the context where the name is used.
        
        A binding has a _scope_ in which the entity defined by a single
        name can be accessed using a simple name. Scopes are nested.  A binding
        in some inner scope _shadows_ bindings of lower precedence in the
        same scope as well as bindings of the same or lower precedence in outer
        scopes.
        
        Note that shadowing is only a partial order. In the following example,
        neither binding of `x` shadows the other. Consequently, the
        reference to `x` in the last line of the block is ambiguous.
     
        ```scala
        val x = 1
        locally {
          import p.X.x
          x
        }
        ```
      """.md
    )
  )

}
