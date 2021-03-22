package ru.fasdev.tfs.domain.model

class User(val id: Int, val avatarUrl: String = "", val fullName: String, val email: String = "") {
    companion object {
        const val USER_IN_MEETING = 1
        const val USER_IS_FREE = 2
    }
}
