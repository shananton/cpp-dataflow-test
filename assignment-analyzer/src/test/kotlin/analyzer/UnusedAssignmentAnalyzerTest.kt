package analyzer

import com.github.h0tk3y.betterParse.grammar.parseToEnd
import embedding.*
import parser.ProgramGrammar
import kotlin.test.*

class UnusedAssignmentAnalyzerTest {
    private fun String.toProgram() = ProgramGrammar.parseToEnd(this.trimIndent())

    @Test
    fun analyzeSampleProgram() {
        val program = """
                a = 1
                b = a
                x = 3
                y = 4

                while (b < 5)
                  z = x
                  b = b + 1
                  x = 9
                  y = 10
                end
            """.toProgram()
        assertEquals(
            listOf("y" assn !4, "z" assn !"x", "y" assn !10),
            UnusedAssignmentAnalyzer.analyze(program)
        )
    }

    @Test
    fun unusedAssnInIf() {
        val program = """
            x = 1
            y = 2
            z = 10
            t = x + y + z
            if y < 5
              x = 3
              y = z
              z = 1
            end
        """.toProgram()
        assertEquals(
            listOf("t" assn !"x" + !"y" + !"z", "x" assn !3, "y" assn !"z", "z" assn !1),
            UnusedAssignmentAnalyzer.analyze(program)
        )
    }

    @Test
    fun unusedAssnInWhile() {
        val program = """
            x = 1
            y = 2
            z = 10
            t = x + y + z
            while y < 5
              x = 3
              y = z
              z = 1
            end
        """.toProgram()
        assertEquals(
            listOf("t" assn !"x" + !"y" + !"z", "x" assn !3),
            UnusedAssignmentAnalyzer.analyze(program)
        )
    }
}