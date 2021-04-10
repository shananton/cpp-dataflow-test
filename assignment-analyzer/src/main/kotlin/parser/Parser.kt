package parser

import lang.*
import com.github.h0tk3y.betterParse.combinators.*
import com.github.h0tk3y.betterParse.grammar.Grammar
import com.github.h0tk3y.betterParse.grammar.parser
import com.github.h0tk3y.betterParse.lexer.literalToken
import com.github.h0tk3y.betterParse.lexer.regexToken
import com.github.h0tk3y.betterParse.parser.Parser

object ProgramGrammar : Grammar<Program>() {
    // Ignore whitespace
    val ws by regexToken("\\s+", ignore = true)

    // The tokenizer (lexer) is automatically built from these tokens:

    val numLit by regexToken("[+-]?[1-9]\\d*")
    val ident by regexToken("[a-z]\\b")

    val lbr by literalToken("(")
    val rbr by literalToken(")")

    val opPlus by literalToken("+")
    val opMinus by literalToken("-")
    val opMul by literalToken("*")
    val opDiv by literalToken("/")
    val opLt by literalToken("<")
    val opGt by literalToken(">")

    val ifKw by regexToken("if\\b")
    val whileKw by regexToken("while\\b")
    val endKw by regexToken("end\\b")

    val eqOp by literalToken("=")

    // Parsers:

    val sumOp by (opPlus asJust BinOp.PLUS) or (opMinus asJust BinOp.MINUS)
    val mulOp by (opMul asJust BinOp.MUL) or (opDiv asJust BinOp.DIV)
    val compOp by (opLt asJust BinOp.LT) or (opGt asJust BinOp.GT)

    val constExpr by numLit use { ConstExpr(text.toInt()) }
    val varExpr by ident use { VarExpr(text) }

    val atom by -lbr * parser { expr } * -rbr or constExpr or varExpr
    val mulExpr by leftAssociative(atom, mulOp, ::BinOpExpr)
    val sumExpr by leftAssociative(mulExpr, sumOp, ::BinOpExpr)
    val compExpr by (sumExpr * compOp * sumExpr map { (lhs, op, rhs) -> BinOpExpr(lhs, op, rhs) }) or sumExpr
    val expr: Parser<Expr> by compExpr

    val block by oneOrMore(parser { stmt }) map ::Block

    val assnStmt by ident * -eqOp * expr map { (id, expr) -> AssnStmt(id.text, expr) }
    val ifStmt by -ifKw * expr * block * -endKw map { (cond, body) -> IfStmt(cond, body) }
    val whileStmt by -whileKw * expr * block * -endKw map { (cond, body) -> WhileStmt(cond, body) }

    val stmt: Parser<Stmt> by assnStmt or ifStmt or whileStmt

    override val rootParser: Parser<Program> by block
}