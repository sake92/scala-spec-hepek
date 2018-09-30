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
      .withSections(lexicalSyntaxSection,
                    identifiersSection,
                    newlineCharactersSection,
                    literalsSection)

  def lexicalSyntaxSection = Section(
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

      <!--
      TODO scala/bug#4583: UnicodeEscape used to allow additional backslashes,
      and there is something in the code `evenSlashPrefix` that alludes to it,
      but I can't make it work nor can I imagine how this would make sense,
      so I removed it for now.
      -->
      
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

  def identifiersSection = Section(
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
        There are three ways to form an identifier.  
        First, an identifier can start with a letter which can be followed by 
          an arbitrary sequence of letters and digits. 
        This may be followed by underscore `‘_‘` characters and another string composed of 
          either letters and digits or of operator characters.  
        Second, an identifier can start with an operator
          character followed by an arbitrary sequence of operator characters.
        The preceding two forms are called _plain identifiers_.  
        Finally, an identifier may also be formed by an arbitrary string between
          back-quotes (host systems may impose some restrictions on which
          strings are legal for identifiers). 
        The identifier then is composed of all characters excluding the backquotes themselves.
          
        As usual, a longest match rule applies. For instance, the string ``big_bob++=`def` ``
          decomposes into the three identifiers `big_bob`, `++=`, and `def`.
        
        The rules for pattern matching further distinguish between
        _variable identifiers_, which start with a lower case letter, and
        _constant identifiers_, which do not. For this purpose,
        underscore `‘_‘` is taken as lower case, and the ‘\\$$’ character
        is taken as upper case.

        The ‘\\$$’ character is reserved for compiler-synthesized identifiers.
        User programs should not define identifiers which contain ‘\\$$’ characters.

        The following names are reserved words instead of being members of the
        syntactic class `id` of lexical identifiers.

        ```scala
        abstract    case        catch       class       def
        do          else        extends     false       final
        finally     for         forSome     if          implicit
        import      lazy        macro       match       new
        null        object      override    package     private
        protected   return      sealed      super       this
        throw       trait       try         true        type
        val         var         while       with        yield
        _    :    =    =>    <-    <:    <%     >:    #    @
        ```

        The Unicode operators `\\u21D2` ´\\Rightarrow´ and `\\u2190` ´\\leftarrow´, which have the ASCII
        equivalents `=>` and `<-`, are also reserved.

        > Here are examples of identifiers:
        > ```scala
        >     x         Object        maxIndex   p2p      empty_?
        >     +         `yield`       αρετη     _y       dot_product_*
        >     __system  _MAX_LEN_
        > ```
       """.md
    )
  )

  def newlineCharactersSection = Section(
    "Newline Characters",
    frag(
      s"""
        ```ebnf
        semi ::= ‘;’ |  nl {nl}
        ```

        Scala is a line-oriented language where statements may be terminated by
        semi-colons or newlines. A newline in a Scala source text is treated
        as the special token “nl” if the three following criteria are satisfied:

        1. The token immediately preceding the newline can terminate a statement.
        1. The token immediately following the newline can begin a statement.
        1. The token appears in a region where newlines are enabled.

        The tokens that can terminate a statement are: literals, identifiers
        and the following delimiters and reserved words:

        ```scala
        this    null    true    false    return    type    <xml-start>
        _       )       ]       }
        ```

        The tokens that can begin a statement are all Scala tokens _except_
        the following delimiters and reserved words:

        ```scala
        catch    else    extends    finally    forSome    match
        with    yield    ,    .    ;    :    =    =>    <-    <:    <%
        >:    #    [    )    ]    }
        ```

        A `case` token can begin a statement only if followed by a
        `class` or `object` token.

        Newlines are enabled in:

        1. all of a Scala source file, except for nested regions where newlines
          are disabled
        1. the interval between matching `{` and `}` brace tokens,
          except for nested regions where newlines are disabled

        Newlines are disabled in:

        1. the interval between matching `(` and `)` parenthesis tokens, except for
          nested regions where newlines are enabled
        1. the interval between matching `[` and `]` bracket tokens, except for nested
          regions where newlines are enabled
        1. the interval between a `case` token and its matching
          `=>` token, except for nested regions where newlines are enabled
        1. any regions analyzed in [XML mode](#xml-mode)

        Note that the brace characters of `{...}` escapes in XML and
          string literals are not tokens,
          and therefore do not enclose a region where newlines are enabled.

        Normally, only a single `nl` token is inserted between two
          consecutive non-newline tokens which are on different lines, even if there are multiple lines
          between the two tokens. 
        However, if two tokens are separated by at
          least one completely blank line (i.e a line which contains no
          printable characters), then two `nl` tokens are inserted.

        The Scala grammar (given in full [here](13-syntax-summary.html))
          contains productions where optional `nl` tokens, but not
          semicolons, are accepted.
        This has the effect that a newline in one of these
        positions does not terminate an expression or statement. 
        These positions can be summarized as follows:

        Multiple newline tokens are accepted in the following places (note
          that a semicolon in place of the newline would be illegal in every one
          of these cases):

        - between the condition of a
          [conditional expression](06-expressions.html#conditional-expressions)
          or [while loop](06-expressions.html#while-loop-expressions) and the next
          following expression
        - between the enumerators of a
          [for-comprehension](06-expressions.html#for-comprehensions-and-for-loops)
          and the next following expression
        - after the initial `type` keyword in a
          [type definition or declaration](04-basic-declarations-and-definitions.html#type-declarations-and-type-aliases)

        A single new line token is accepted

        - in front of an opening brace ‘{’, if that brace is a legal
          continuation of the current statement or expression
        - after an [infix operator](06-expressions.html#prefix,-infix,-and-postfix-operations),
          if the first token on the next line can start an expression
        - in front of a [parameter clause](04-basic-declarations-and-definitions.html#function-declarations-and-definitions)
        - after an [annotation](11-annotations.html#user-defined-annotations)
                
        > The newline tokens between the two lines are not
        > treated as statement separators.
        >
        > ```scala
        > if (x > 0)
        >   x = x - 1
        >
        > while (x > 0)
        >   x = x / 2
        >
        > for (x <- 1 to 10)
        >   println(x)
        >
        > type
        >   IntList = List[Int]
        > ```

        > ```scala
        > new Iterator[Int]
        > {
        >   private var x = 0
        >   def hasNext = true
        >   def next = { x += 1; x }
        > }
        > ```
        >
        > With an additional newline character, the same code is interpreted as
        > an object creation followed by a local block:
        >
        > ```scala
        > new Iterator[Int]
        >
        > {
        >   private var x = 0
        >   def hasNext = true
        >   def next = { x += 1; x }
        > }
        > ```

        > ```scala
        >   x < 0 ||
        >   x > 10
        > ```
        >
        > With an additional newline character, the same code is interpreted as
        > two expressions:
        >
        > ```scala
        >   x < 0 ||
        >
        >   x > 10
        > ```

        > ```scala
        > def func(x: Int)
        >         (y: Int) = x + y
        > ```
        >
        > With an additional newline character, the same code is interpreted as
        > an abstract function definition and a syntactically illegal statement:
        >
        > ```scala
        > def func(x: Int)
        >
        >         (y: Int) = x + y
        > ```

        > ```scala
        > @serializable
        > protected class Data { ... }
        > ```
        >
        > With an additional newline character, the same code is interpreted as
        > an attribute and a separate statement (which is syntactically illegal).
        >
        > ```scala
        > @serializable
        >
        > protected class Data { ... }
        > ```
       """.md
    )
  )

  def literalsSection = Section(
    "Identifiers",
    frag(
      s"""
        There are literals for integer numbers, floating point numbers,
          characters, booleans, symbols, strings.  The syntax of these literals is in
          each case same as in Java.

        <!-- TODO
          say that we take values from Java, give examples of some lits in
          particular float and double.
        -->

        ```ebnf
        Literal  ::=  [‘-’] integerLiteral
                  |  [‘-’] floatingPointLiteral
                  |  booleanLiteral
                  |  characterLiteral
                  |  stringLiteral
                  |  interpolatedString
                  |  symbolLiteral
                  |  ‘null’
        ```
       """.md
    ),
    List(integerLiteralsSection,
         floatingPointLiteralsSection,
         booleanLiteralsSection,
         characterLiteralsSection,
         stringLiteralsSection)
  )

  def integerLiteralsSection = Section(
    "Identifiers",
    frag(
      s"""
        
       """.md
    )
  )

  def floatingPointLiteralsSection = Section(
    "Identifiers",
    frag(
      s"""
        
       """.md
    )
  )

  def booleanLiteralsSection = Section(
    "booleanLiteralsSection",
    frag(
      s"""
        
       """.md
    )
  )

  def characterLiteralsSection = Section(
    "characterLiteralsSection",
    frag(
      s"""
        
       """.md
    )
  )

  def stringLiteralsSection = Section(
    "stringLiteralsSection",
    frag(
      s"""
        
       """.md
    )
  )

  def multiLineStringLiteralsSection = Section(
    "multiLineStringLiteralsSection",
    frag(
      s"""
        
       """.md
    )
  )

  def interpolatedStringSection = Section(
    "interpolatedStringSection",
    frag(
      s"""
        
       """.md
    )
  )

  def escapeSequencesSection = Section(
    "escapeSequencesSection",
    frag(
      s"""
        
       """.md
    )
  )

  def symbolLiteralsSection = Section(
    "symbolLiteralsSection",
    frag(
      s"""
        
       """.md
    )
  )

  def whitespaceAndCommentsSection = Section(
    "whitespaceAndCommentsSection",
    frag(
      s"""
        
       """.md
    )
  )

  def trailingCommasInMultiLineExpressionsSection = Section(
    "trailingCommasInMultiLineExpressionsSection",
    frag(
      s"""
        
       """.md
    )
  )

  def xmlModeSection = Section(
    "xmlModeSection",
    frag(
      s"""
        
       """.md
    )
  )
}
