package lang

interface ASTNode {
    fun accept(visitor: ASTNodeVisitor)
}

typealias Name = String
typealias Program = Block

data class Block(val statements: List<Stmt>) : ASTNode {
    override fun accept(visitor: ASTNodeVisitor) {
        visitor.visit(this)
    }
}

abstract class Stmt : ASTNode
data class AssnStmt(val id: Name, val expr: Expr) : Stmt() {
    override fun accept(visitor: ASTNodeVisitor) {
        visitor.visit(this)
    }
}
data class IfStmt(val cond: Expr, val body: Block) : Stmt() {
    override fun accept(visitor: ASTNodeVisitor) {
        visitor.visit(this)
    }
}
data class WhileStmt(val condition: Expr, val body: Block) : Stmt() {
    override fun accept(visitor: ASTNodeVisitor) {
        visitor.visit(this)
    }
}

abstract class Expr : ASTNode
data class VarExpr(val id: Name) : Expr() {
    override fun accept(visitor: ASTNodeVisitor) {
        visitor.visit(this)
    }
}
data class ConstExpr(val value: Int) : Expr() {
    override fun accept(visitor: ASTNodeVisitor) {
        visitor.visit(this)
    }
}
data class BinOpExpr(val lhs: Expr, val op: BinOp, val rhs: Expr) : Expr() {
    override fun accept(visitor: ASTNodeVisitor) {
        visitor.visit(this)
    }
}

enum class BinOp {
    PLUS,
    MINUS,
    MUL,
    DIV,
    LT,
    GT,
}

interface ASTNodeVisitor {
    fun visit(block: Block)
    fun visit(assnStmt: AssnStmt)
    fun visit(ifStmt: IfStmt)
    fun visit(whileStmt: WhileStmt)
    fun visit(constExpr: ConstExpr)
    fun visit(varExpr: VarExpr)
    fun visit(binOpExpr: BinOpExpr)
}