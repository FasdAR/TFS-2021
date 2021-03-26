package ru.fasdev.tfs.domain.user.repo

import ru.fasdev.tfs.domain.model.User
import ru.fasdev.tfs.domain.model.UserStatus

class TestUserRepoImpl : UserRepo {
    override fun getAllUsers(): List<User> = listOf(
        User(1, "", "Darrell Steward", "darrel@company.com"),
        User(2, "", "Reagan Emery", "reagan@company.com"),
        User(3, "", "Jayce Hester", "jayce@company.com"),
        User(4, "", "Kady Benitez", "kady@company.com"),
        User(5, "", "Mac Hutton", "mac@company.com"),
        User(6, "", "Hope Mercado", "hope@company.com"),
        User(7, "", "Anabelle Davies", "anabelle@company.com"),
        User(8, "", "Macaulay Wormald", "macaulay@company.com"),
        User(10, "", "Eleanor Arroyo", "eleanor@company.com"),
        User(11, "", "Patience Leblanc", "patience@company.com"),
        User(12, "", "Nakita Beck", "nakita@company.com"),
        User(13, "", "Celine Gaines", "celine@company.com"),
        User(14, "", "Cindy Watkins", "cindy@company.com"),
        User(15, "", "Manveer Robson", "manveer@company.com")
    )

    override fun getUserById(id: Int): User? = getAllUsers().find { it.id == id }

    override fun getStatusUser(id: Int): UserStatus {
        return if ((id % 2) == 0) UserStatus.MEETING
            else UserStatus.FREE
    }

    override fun isOnlineUser(id: Int): Boolean = (id % 2) == 0
}
