import scala.scalanative.libc.string.strlen
import treesitter.all.*
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
    val rootNode = ts_tree_root_node(tree)
    // printChildren(rootNode)
  }

  // from https://github.com/indoorvivants/sn-bindgen-examples
  def printChildren(start: TSNode)(implicit zone: Zone): Unit =
    def go(node: TSNode, level: Int): Unit =
      val nodeType = fromCString(ts_node_type(node))
      val startByte = ts_node_start_byte(node)
      val endByte = ts_node_end_byte(node)
      print(" " * level)
      println(s"${nodeType}:${startByte}:${endByte}")
      // asInstanceOf is a bug in codegen
      val childrenCount = ts_node_child_count(node)
      if (childrenCount != 0.toUInt) {
        for {childId <- 0 until childrenCount.toInt} do {
          val childNode =
            ts_node_child(node, childId.toUInt)
          go(childNode, level + 1)
        }
      }
    end go

    go(start, 0)
  end printChildren
}
