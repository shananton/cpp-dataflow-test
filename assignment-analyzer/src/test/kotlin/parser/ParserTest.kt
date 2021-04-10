package parser

import com.github.h0tk3y.betterParse.grammar.parseToEnd
import com.github.h0tk3y.betterParse.lexer.noneMatched
import kotlin.test.*
import embedding.*
import lang.IfStmt

class ParserTest {
    private fun String.tokenize() = ProgramGrammar.tokenizer.tokenize(this).toList()
        .filter { !it.type.ignored && it.type != noneMatched }
        .map { it.text }

    @Test
    fun tokenize() {
        assertEquals(
            listOf("if", "while", "+1", "-", "x", "-1"),
            "if while +1-x-1".tokenize()
        )
        assertEquals(
            emptyList(),
            "ifwhile".tokenize(),
            "Keyword tokens should not cross word boundaries"
        )
    }

    @Test
    fun parseSampleProgram() {
        val program = """ 
            x = 1
            y = x * (2 + 3)
            if x + y * x < 15
                z = 1
            end
        """.trimIndent()
        assertEquals(
            block(
                "x" assn !1,
                "y" assn !"x" * (!2 + !3),
                IfStmt(
                    !"x" + !"y" * !"x" lt !15,
                    block("z" assn !1)
                ),
            ),
            ProgramGrammar.parseToEnd(program)
        )
    }
}