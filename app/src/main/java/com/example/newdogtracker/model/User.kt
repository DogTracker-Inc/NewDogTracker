package com.example.newdogtracker.model

data class User(
    var userId: String? = null,
    var userName: String? = null,
    var userPhone: String? = null,
    var userGroup: String? = null
) {
    constructor(userName: String, userGroup: String) : this(
        userId = null,
        userName = userName,
        userPhone = null,
        userGroup = userGroup
    )

    constructor(userName: String, userPhone: String, userGroup: String) : this(
        userId = null,
        userName = userName,
        userPhone = userPhone,
        userGroup = userGroup
    )

    fun validate(): Boolean {
        return !userName.isNullOrBlank() && !userGroup.isNullOrBlank()
    }
}
