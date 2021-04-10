package embedding

import lang.*


operator fun String.not() = VarExpr(this)
operator fun Int.not() = ConstExpr(this)

operator fun Expr.plus(other: Expr) = BinOpExpr(this, BinOp.PLUS, other)
operator fun Expr.minus(other: Expr) = BinOpExpr(this, BinOp.MINUS, other)
operator fun Expr.times(other: Expr) = BinOpExpr(this, BinOp.MUL, other)
operator fun Expr.div(other: Expr) = BinOpExpr(this, BinOp.DIV, other)
infix fun String.assn(expr: Expr) = AssnStmt(this, expr)
infix fun Expr.lt(other: Expr) = BinOpExpr(this, BinOp.LT, other)
infix fun Expr.gt(other: Expr) = BinOpExpr(this, BinOp.GT, other)

fun block(vararg statements: Stmt) = Block(statements.toList())