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
      .withSections(
        lexicalSyntaxSection,
        identifiersSection,
        newlineCharactersSection,
        literalsSection,
        whitespaceAndCommentsSection,
        trailingCommasInMultiLineExpressionsSection,
        xmlModeSection
      )

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
        op          ::=  opchar {opchar}
        varid       ::=  lower idrest
        boundvarid  ::=  varid
                      | ‘`’ varid ‘`’
        plainid     ::=  upper idrest
                      |  varid
                      |  op
        id          ::=  plainid
                      |  ‘`’ { charNoBackQuoteOrNewline | UnicodeEscape | charEscapeSeq } ‘`’
        idrest      ::=  {letter | digit} [‘_’ op]
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
        underscore `‘_‘` is taken as lower case, and the ‘$$’ character
        is taken as upper case.

        The ‘$$’ character is reserved for compiler-synthesized identifiers.
        User programs should not define identifiers which contain ‘$$’ characters.

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
        as the special token ‘nl’ if the three following criteria are satisfied:

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
    "Literals",
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
    List(
      integerLiteralsSection,
      floatingPointLiteralsSection,
      booleanLiteralsSection,
      characterLiteralsSection,
      stringLiteralsSection,
      escapeSequencesSection,
      symbolLiteralsSection
    )
  )

  def integerLiteralsSection = Section(
    "Integer Literals",
    frag(
      s"""
        ```ebnf
        integerLiteral  ::=  (decimalNumeral | hexNumeral)
                              [‘L’ | ‘l’]
        decimalNumeral  ::=  ‘0’ | nonZeroDigit {digit}
        hexNumeral      ::=  ‘0’ (‘x’ | ‘X’) hexDigit {hexDigit}
        digit           ::=  ‘0’ | nonZeroDigit
        nonZeroDigit    ::=  ‘1’ | … | ‘9’
        ```

        Integer literals are usually of type `Int`, or of type
        `Long` when followed by a `L` or
        `l` suffix. Values of type `Int` are all integer
        numbers between ´-2^{31}´ and ´2^{31}-1´, inclusive.  Values of
        type `Long` are all integer numbers between ´-2^{63}´ and
        ´2^{63}-1´, inclusive. A compile-time error occurs if an integer literal
        denotes a number outside these ranges.

        However, if the expected type [_pt_](06-expressions.html#expression-typing) of a literal
        in an expression is either `Byte`, `Short`, or `Char`
        and the integer number fits in the numeric range defined by the type,
        then the number is converted to type _pt_ and the literal's type
        is _pt_. The numeric ranges given by these types are:

        <table class="table table-hover">
          <tr><td><code>Byte</code></td><td>´-2^7´to ´2^7-1´</td></tr>
          <tr><td><code>Short</code></td><td>´-2^{15}´ to ´2^{15}-1´</td></tr>
          <tr><td><code>Char</code></td><td>´0´ to ´2^{16}-1´</td></tr>
        </table>

        > ```scala
        > 0          21          0xFFFFFFFF       -42L
        > ```
       """.md
    )
  )

  def floatingPointLiteralsSection = Section(
    "Floating Point Literals",
    frag(
      s"""
        ```ebnf
        floatingPointLiteral  ::=  digit {digit} ‘.’ digit {digit} [exponentPart] [floatType]
                                |  ‘.’ digit {digit} [exponentPart] [floatType]
                                |  digit {digit} exponentPart [floatType]
                                |  digit {digit} [exponentPart] floatType
        exponentPart          ::=  (‘E’ | ‘e’) [‘+’ | ‘-’] digit {digit}
        floatType             ::=  ‘F’ | ‘f’ | ‘D’ | ‘d’
        ```

        Floating point literals are of type `Float` when followed by
        a floating point type suffix `F` or `f`, and are
        of type `Double` otherwise.  The type `Float`
        consists of all IEEE 754 32-bit single-precision binary floating point
        values, whereas the type `Double` consists of all IEEE 754
        64-bit double-precision binary floating point values.

        If a floating point literal in a program is followed by a token
        starting with a letter, there must be at least one intervening
        whitespace character between the two tokens.

        > ```scala
        > 0.0        1e30f      3.14159f      1.0e-100      .1
        > ```

        <!-- -->

        > The phrase `1.toString` parses as three different tokens:
        > the integer literal `1`, a `.`, and the identifier `toString`.

        <!-- -->

        > `1.` is not a valid floating point literal because the mandatory digit after the `.` is missing.
       """.md
    )
  )

  def booleanLiteralsSection = Section(
    "Boolean Literals",
    frag(
      s"""
        ```ebnf
        booleanLiteral  ::=  ‘true’ | ‘false’
        ```

        The boolean literals `true` and `false` are
        members of type `Boolean`.
       """.md
    )
  )

  def characterLiteralsSection = Section(
    "Character Literals",
    frag(
      s"""
        ```ebnf
        characterLiteral  ::=  ‘'’ (charNoQuoteOrNewline | UnicodeEscape | charEscapeSeq) ‘'’
        ```

        A character literal is a single character enclosed in quotes.
        The character can be any Unicode character except the single quote
        delimiter or `\\u000A` (LF) or `\\u000D` (CR);
        or any Unicode character represented by either a
        [Unicode escape](01-lexical-syntax.html) or by an [escape sequence](#escape-sequences).

        > ```scala
        > 'a'    '\\u0041'    '\\n'    '\\t'
        > ```

        Note that although Unicode conversion is done early during parsing,
        so that Unicode characters are generally equivalent to their escaped
        expansion in the source text, literal parsing accepts arbitrary
        Unicode escapes, including the character literal `'\\u000A'`,
        which can also be written using the escape sequence `'\\n'`.
       """.md
    )
  )

  def stringLiteralsSection = Section(
    "String Literals",
    frag(
      s"""
        ```ebnf
        stringLiteral  ::=  ‘"’ {stringElement} ‘"’
        stringElement  ::=  charNoDoubleQuoteOrNewline | UnicodeEscape | charEscapeSeq
        ```

        A string literal is a sequence of characters in double quotes.
        The characters can be any Unicode character except the double quote
        delimiter or `\\u000A` (LF) or `\\u000D` (CR);
        or any Unicode character represented by either a
        [Unicode escape](01-lexical-syntax.html) or by an [escape sequence](#escape-sequences).

        If the string literal contains a double quote character, it must be escaped using
        `"\\""`.

        The value of a string literal is an instance of class `String`.

        > ```scala
        > "Hello, world!\\n"
        > "\\"Hello,\\" replied the world."
        > ```
      """.md
    ),
    List(multiLineStringLiteralsSection, interpolatedStringSection)
  )

  def multiLineStringLiteralsSection = Section(
    "Multi-Line String Literals",
    frag(
      s"""
        ```ebnf
        stringLiteral   ::=  ‘\"\""’ multiLineChars ‘\"\""’
        multiLineChars  ::=  {[‘"’] [‘"’] charNoDoubleQuote} {‘"’}
        ```

        A multi-line string literal is a sequence of characters enclosed in
        triple quotes `\"\"" ... \"\""`. The sequence of characters is
        arbitrary, except that it may contain three or more consecutive quote characters
        only at the very end. Characters
        must not necessarily be printable; newlines or other
        control characters are also permitted.  Unicode escapes work as everywhere else, but none
        of the escape sequences [here](${escapeSequencesSection.ref}) are interpreted.

        > ```scala
        >   \"\""the present string
        >      spans three
        >      lines.\"\""
        > ```
        >
        > This would produce the string:
        >
        > ```scala
        > the present string
        >      spans three
        >      lines.
        > ```
        >
        > The Scala library contains a utility method `stripMargin`
        > which can be used to strip leading whitespace from multi-line strings.  
        > The expression
        >
        > ```scala
        >  \"\""the present string
        >    |spans three
        >    |lines.\"\"".stripMargin
        > ```
        >
        > evaluates to
        >
        > ```scala
        > the present string
        > spans three
        > lines.
        > ```
        >
        > Method `stripMargin` is defined in class
        > [scala.collection.immutable.StringLike](https://www.scala-lang.org/api/current/scala/collection/immutable/StringLike.html).
        > Because there is a predefined
        > [implicit conversion](06-expressions.html#implicit-conversions) from `String` to
        > `StringLike`, the method is applicable to all strings.
      """.md
    )
  )

  def interpolatedStringSection = Section(
    "Interpolated string",
    frag(
      s"""
        ```ebnf
        interpolatedString  ::= alphaid ‘"’ {printableChar (‘"’ | ‘$$’) | escape} ‘"’ 
                              |  alphaid ‘\"\""’ {[‘"’] [‘"’] char (‘"’ | ‘$$’) | escape} {‘"’} ‘\"\""’
        escape              ::= ‘$$$$’ 
                              | ‘$$’ id
                              | ‘$$’ BlockExpr
        alphaid             ::= upper idrest
                              |  varid
        ```

        Interpolated string consists of an identifier starting with a letter immediately 
          followed by a string literal. 
        There may be no whitespace characters or comments 
          between the leading identifier and the opening quote ‘"’ of the string. 
        The string literal in a interpolated string can be standard (single quote) 
          or multi-line (triple quote).

        Inside an interpolated string none of the usual escape characters are interpreted 
          (except for unicode escapes) no matter whether the string literal is normal 
          (enclosed in single quotes) or multi-line (enclosed in triple quotes). 
        Instead, there are two new forms of dollar sign escape. 
        The most general form encloses an expression in ‘$${’ and ‘}’, i.e. `$${expr}`. 
        The expression enclosed in the braces that follow the leading ‘$$’ character is of 
          syntactical category `BlockExpr`.
        Hence, it can contain multiple statements, and newlines are significant.
        Single ‘$$’-signs are not permitted in isolation in a interpolated string. 
        A single ‘$$’-sign can still be obtained by doubling the ‘$$’ character: ‘$$$$’.

        The simpler form consists of a ‘$$’-sign followed by an identifier starting with 
          a letter and followed only by letters, digits, and underscore characters, 
          e.g `$$id`. The simpler form is expanded by putting braces around the identifier, 
          e.g `$$id` is equivalent to `$${id}`. 
        In the following, unless we explicitly state otherwise, 
        we assume that this expansion has already been performed.

        The expanded expression is type checked normally. 
        Usually, `StringContext` will resolve to the default implementation in the scala package, 
          but it could also be user-defined. 
          Note that new interpolators can also be added through 
        implicit conversion of the built-in `scala.StringContext`.

        One could write an extension
        ```scala
        implicit class StringInterpolation(s: StringContext) {
          def id(args: Any*) = ???
        }
        ```
      """.md
    )
  )

  private val escapeSequences = List(
    ("b", "\\u0008", "backspace", "BS"),
    ("t", "\\u0009", "horizontal tab", "HT"),
    ("n", "\\u000a", "linefeed", "LF"),
    ("f", "\\u000c", "form feed", "FF"),
    ("r", "\\u000d", "carriage return", "CR"),
    ("\"", "\\u0022", " double quote", "\""),
    ("'", "\\u0027", "single quote", "'"),
    ("\\", "\\u005c", "backslash", "\\"),
  )

  def escapeSequencesSection = Section(
    "Escape Sequences",
    frag(
      "The following escape sequences are recognized in character and string literals.",
      table(cls := "table table-hover")(
        thead(
          tr(
            th("charEscapeSeq"),
            th("unicode"),
            th("name"),
            th("char")
          )
        ),
        tbody(
          escapeSequences.map {
            case (esc, unicode, name, char) =>
              tr(
                td(code(s"‘\\‘ ‘$esc‘")),
                td(code(unicode)),
                td(name),
                td(code(char))
              )
          }: _*
        )
      ),
      s"""
        It is a compile time error if a backslash character in a character or
        string literal does not start a valid escape sequence.
      """.md
    )
  )

  def symbolLiteralsSection = Section(
    "Symbol literals",
    frag(
      s"""
        ```ebnf
        symbolLiteral  ::=  ‘'’ plainid
        ```

        A symbol literal `'x` is a shorthand for the expression `scala.Symbol("x")` and
          is of the [literal type](03-types#literal-types) `'x`. 
        `Symbol` is a [case class](05-classes-and-objects.html#case-classes), which is defined as follows.

        ```scala
        package scala
        final case class Symbol private (name: String) {
          override def toString: String = "'" + name
        }
        ```

        The `apply` method of `Symbol`'s companion object
          caches weak references to `Symbol`s, thus ensuring that
          identical symbol literals are equivalent with respect to reference equality.
      """.md
    )
  )

  def whitespaceAndCommentsSection = Section(
    "Whitespace and Comments",
    frag(
      s"""
        Tokens may be separated by whitespace characters and/or comments. 
        Comments come in two forms:

        A single-line comment is a sequence of characters which starts with
          `//` and extends to the end of the line.

        A multi-line comment is a sequence of characters between `/*` and `*/`. 
        Multi-line comments may be nested, but are required to be properly nested. 
        Therefore, a comment like `/* /* */` will be rejected as having an unterminated comment.
      """.md
    )
  )

  def trailingCommasInMultiLineExpressionsSection = Section(
    "Trailing Commas in Multi-line Expressions",
    frag(
      s"""
        If a comma (`,`) is followed immediately, ignoring whitespace, by a newline and
          a closing parenthesis (`)`), bracket (`]`), or brace (`}`), then the comma is
          treated as a "trailing comma" and is ignored. For example:

        ```scala
        foo(
          23,
          "bar",
          true,
        )
        ```
      """.md
    )
  )

  def xmlModeSection = Section(
    "XML mode",
    frag(
      s"""
        In order to allow literal inclusion of XML fragments, lexical analysis
          switches from Scala mode to XML mode when encountering an opening
          angle bracket ‘<’ in the following circumstance: The ‘<’ must be
          preceded either by whitespace, an opening parenthesis or an opening
          brace and immediately followed by a character starting an XML name.

        ```ebnf
        ( whitespace | ‘(’ | ‘{’ ) ‘<’ (XNameStart | ‘!’ | ‘?’)

          XNameStart ::= ‘_’ | BaseChar | Ideographic // as in W3C XML, but without ‘:’
        ```

        The scanner switches from XML mode to Scala mode if either of these is true:

        - the XML expression or the XML pattern started by the initial ‘<’ has been
          successfully parsed
        - the parser encounters an embedded Scala expression or pattern and
          forces the Scanner
          back to normal mode, until the Scala expression or pattern is
          successfully parsed. In this case, since code and XML fragments can be
          nested, the parser has to maintain a stack that reflects the nesting
          of XML and Scala expressions adequately

        Note that no Scala tokens are constructed in XML mode, and that comments are interpreted
        as text.

        > The following value definition uses an XML literal with two embedded
        > Scala expressions:
        >
        > ```scala
        > val b = <book>
        >           <title>The Scala Language Specification</title>
        >           <version>{scalaBook.version}</version>
        >           <authors>{scalaBook.authors.mkList("", ", ", "")}</authors>
        >         </book>
        > ```
      """.md
    )
  )
}
