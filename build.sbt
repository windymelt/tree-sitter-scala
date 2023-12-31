scalaVersion := "3.3.0"

enablePlugins(ScalaNativePlugin, BindgenPlugin)

// set to Debug for compilation details (Info is default)
logLevel := Level.Info

// import to add Scala Native options
import scala.scalanative.build._

// defaults set with common options shown
nativeConfig := {
  val base = baseDirectory.value
  val c = nativeConfig.value
  c.withLTO(LTO.none) // thin
    .withMode(Mode.debug) // releaseFast
    .withGC(GC.immix) // commix
    .withLinkingOptions(
      List("-ltree-sitter", "-ltree-sitter-json", "-L", "/usr/local/lib/")
    )
}

import bindgen.interface.Binding
bindgenBindings := Seq(
  Binding
    .builder(
      /* 1 */  (Compile / resourceDirectory).value / "scala-native" / "api.h",
      /* 2 */  "treesitter"
    )
    .addCImport("api.h") /* 3 */
    .build
  )

