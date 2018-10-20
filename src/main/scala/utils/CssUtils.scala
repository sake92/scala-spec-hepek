package utils

object CssUtils {

  def numberings(pageChapter: Int): String =
    s"""
      body {
        counter-reset: chapter $pageChapter section;
      }
      
      h3 {
       counter-reset: subsection;
      }
      h3:before {        
        counter-increment: section;
        content: counter(chapter) "." counter(section) " ";
      }

      h4:before {
        counter-increment: subsection;
        content:  counter(chapter) "." counter(section) "." counter(subsection) " ";
      }
    """

}
