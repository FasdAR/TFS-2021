package ru.fasdev.tfs.domain

import kotlin.random.Random

class TestError {
    companion object {
        fun testError() {
            val randomError = Random.nextBoolean()
            if (randomError) error("YEEEEE, IT IS A RANDOM ERROR")
        }
    }
}