import treesitter.all.*
import scalanative.libc.string.strlen
import scalanative.unsafe.*
import scalanative.unsigned.UnsignedRichInt

// dynamic link
def tree_sitter_json(): Ptr[TSLanguage] = extern

object Main {
  def main(args: Array[String]): Unit = Zone { implicit zone =>
    val parser = ts_parser_new()
    ts_parser_set_language(parser, tree_sitter_json())
    val source = c"""{"foo":42, "bar": "windymelt"}"""
    val tree = ts_parser_parse_string(parser, null, source, strlen(source).toUInt)
    ts_tree_print_dot_graph(tree, 1 /* stdout*/)
  }
}
