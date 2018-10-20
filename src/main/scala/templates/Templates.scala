package templates

import ba.sake.hepek.anchorjs.AnchorjsDependencies
import ba.sake.hepek.bootstrap3.statik.BootstrapStaticPage
import ba.sake.hepek.html.structure.ComponentSettings
import ba.sake.hepek.implicits._
import ba.sake.hepek.katex.KatexDependencies
import ba.sake.hepek.prismjs._
import ba.sake.hepek.theme.bootstrap3.{HepekBootstrap3BlogPage, TocType}
import utils.CssUtils
import utils.Imports.resources._

trait SpecBlogPage extends SpecStaticPage with HepekBootstrap3BlogPage {
  override def categoryPosts =
    List(
      site.LexicalSyntax,
      site.IdentifiersNamesScopes,
      site.Types,
      site.BasicDeclarationsDefinitions,
      site.ClassesObjects,
      site.Expressions,
      site.Implicits,
      site.PatternMatching,
      site.TopLevelDefinitions,
      site.XMLExpressionsPatterns,
      site.Annotations,
      site.ScalaStandardLibrary,
      site.SyntaxSummary,
      site.References,
      site.Changelog
    )
  override def pageHeader = None

  override def tocSettings = super.tocSettings.copy(
    tocType = Some(TocType.Scrollspy(65))
  )

  override def stylesInline: List[String] =
    super.stylesInline :+
      CssUtils.numberings(categoryPosts.indexOf(this) + 1)
}

trait SpecStaticPage
    extends BootstrapStaticPage
    with KatexDependencies
    with PrismDependencies
    with AnchorjsDependencies {

  override def siteSettings =
    super.siteSettings
      .withName("Scala Language Specification")
      .withIndexPage(site.Index)
      .withFaviconNormal(images.ico("favicon").ref)
      .withFaviconInverted(images.png("scala-icon-small").ref)

  override def styleURLs  = super.styleURLs ++ List(styles.css("main").ref)
  override def scriptURLs = super.scriptURLs ++ List(scripts.js("main").ref)

  // nice theme
  override def bootstrapDependencies = super.bootstrapDependencies.withCssDependencies(
    Dependencies()
      .withDeps(Dependency("readable/bootstrap.min.css", bootstrapSettings.version, "bootswatch"))
  )

  // math
  override def katexSettings =
    super.katexSettings
      .withIgnoredTags(List("script", "noscript", "style", "textarea")) // don't ignore <pre> and <code>

  // code highlighting
  private val hlLangs = List("core", "clike", "scala", "java", "markup")

  override def prismSettings: PrismSettings =
    super.prismSettings
      .withTheme(Themes.Default)
      .withLanguages(PrismConsts.languages filter hlLangs.contains)
      .withShowLanguage(false)

}
