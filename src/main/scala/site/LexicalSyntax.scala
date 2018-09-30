package site

import scalatags.Text.all._
import ba.sake.hepek.implicits._
import utils.Imports._, grid._

object LexicalSyntax extends templates.SpecBlogPage {

  override def pageSettings =
    super.pageSettings
      .withTitle("Lexical Syntax")

  override def blogSettings =
    super.blogSettings
      .withSections(lexicalSyntaxSection, identifiersSection)

  val lexicalSyntaxSection = Section(
    "Lexical Syntax",
    frag(
      s"""
      Scala programs are written using the Unicode Basic Multilingual Plane
        (_BMP_) character set; Unicode supplementary characters are not presently supported.  
      This chapter defines the two modes of Scala's
        lexical syntax, the Scala mode and the _XML mode_.  
      If not otherwise mentioned, the following descriptions of Scala tokens refer
        to _Scala mode_, and literal characters (marked as `c`) refer to the ASCII fragment
        `\\u0000` – `\\u007F`.
      
      In Scala mode, _Unicode escapes_ are replaced by the corresponding
        Unicode character with the given hexadecimal code.
      
      ```ebnf
      UnicodeEscape ::= ‘\\’ ‘u’ {‘u’} hexDigit hexDigit hexDigit hexDigit
      hexDigit      ::= ‘0’ | … | ‘9’ | ‘A’ | … | ‘F’ | ‘a’ | … | ‘f’
      ```
      
      To construct tokens, characters are distinguished according to the following
        classes (Unicode general category given in parentheses):
      
      1. Whitespace characters. `\\u0020 | \\u0009 | \\u000D | \\u000A`.
      1. Letters, which include lower case letters (`Ll`), upper case letters (`Lu`),
         titlecase letters (`Lt`), other letters (`Lo`), letter numerals (`Nl`) and the
         two characters `\\u0024 "$$"` and `\\u005F "_"`.
      1. Digits `"0" | … | "9"`.
      1. Parentheses `"(" | ")" | "[" | "]" | "{" | "}" `.
      1. Delimiter characters ``"`" | "'" | \"\"" | "." | ";" | "," ``.
      1. Operator characters. These consist of all printable ASCII characters
         (`\\u0020` - `\\u007E`) that are in none of the sets above, mathematical
         symbols (`Sm`) and other symbols (`So`).
    """.md
    )
  )

  val identifiersSection = Section(
    "Identifiers",
    frag(
      s"""
        ```ebnf
        op       ::=  opchar {opchar}
        varid    ::=  lower idrest
        boundvarid ::=  varid
                     | ‘`’ varid ‘`’
        plainid  ::=  upper idrest
                   |  varid
                   |  op
        id       ::=  plainid
                   |  ‘`’ { charNoBackQuoteOrNewline | UnicodeEscape | charEscapeSeq } ‘`’
        idrest   ::=  {letter | digit} [‘_’ op]
        ```
        There are three ways to form an identifier. First, an identifier can
          start with a letter which can be followed by an arbitrary sequence of
          letters and digits. This may be followed by underscore `‘_‘`
          characters and another string composed of either letters and digits or
          of operator characters.  Second, an identifier can start with an operator
          character followed by an arbitrary sequence of operator characters.
          The preceding two forms are called _plain_ identifiers.  Finally,
          an identifier may also be formed by an arbitrary string between
          back-quotes (host systems may impose some restrictions on which
          strings are legal for identifiers).  The identifier then is composed
          of all characters excluding the backquotes themselves.
          
          As usual, a longest match rule applies. For instance, the string ``big_bob++=`def` ``
          decomposes into the three identifiers `big_bob`, `++=`, and `def`.
       """.md,
    )
  )

}
