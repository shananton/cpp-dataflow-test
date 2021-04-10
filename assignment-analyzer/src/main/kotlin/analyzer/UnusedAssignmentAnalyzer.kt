package analyzer

import lang.*

class UnusedAssignmentAnalyzer(private val node: ASTNode) {

    private var currentAssnIndex = 0
    private val unusedAssignments: MutableList<OrderedAssnStmt> = mutableListOf()
    private val currentlyUnused: MutableMap<Name, OrderedAssnStmt> = mutableMapOf()

    private data class OrderedAssnStmt(val index: Int, val assnStmt: AssnStmt)

    companion object {
        fun analyze(node: ASTNode): List<AssnStmt> {
            val analyzer = UnusedAssignmentAnalyzer(node)
            node.accept(analyzer.Visitor())
            return (analyzer.unusedAssignments + analyzer.currentlyUnused.values)
                .sortedBy { it.index }.map { it.assnStmt }
        }
    }

    private inner class Visitor : ASTNodeVisitor {
        val maybeUsed: MutableSet<Name> = mutableSetOf()

        override fun visit(block: Block) {
            block.statements.forEach { it.accept(this) }
        }

        override fun visit(assnStmt: AssnStmt) {
            assnStmt.expr.accept(this)
            currentlyUnused.put(assnStmt.id, OrderedAssnStmt(currentAssnIndex++, assnStmt))
                ?.let { unusedAssignments.add(it) }
        }

        override fun visit(ifStmt: IfStmt) {
            ifStmt.cond.accept(this)
            ifStmt.body.accept(this)
        }

        override fun visit(whileStmt: WhileStmt) {
            val innerVisitor = Visitor()
            whileStmt.condition.accept(innerVisitor)
            whileStmt.body.accept(innerVisitor)
            for (usedId in innerVisitor.maybeUsed) {
                currentlyUnused.remove(usedId)
            }
            maybeUsed += innerVisitor.maybeUsed
        }

        override fun visit(constExpr: ConstExpr) {}

        override fun visit(varExpr: VarExpr) {
            val id = varExpr.id
            currentlyUnused.remove(id)
            maybeUsed.add(id)
        }

        override fun visit(binOpExpr: BinOpExpr) {
            binOpExpr.lhs.accept(this)
            binOpExpr.rhs.accept(this)
        }
    }
}