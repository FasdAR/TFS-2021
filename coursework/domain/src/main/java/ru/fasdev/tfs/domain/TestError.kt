package ru.fasdev.tfs.domain

class TestError {
    companion object {
        fun testError(dopInfo: String) {
            val randomError = (0..10).random()
            if (randomError >= 9) error("$dopInfo: RANDOM ERROR")
        }
    }
}