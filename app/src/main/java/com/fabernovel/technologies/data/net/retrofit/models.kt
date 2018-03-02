package com.fabernovel.technologies.data.net.retrofit

data class RestError(val error: String?)

// Add REST models here. Use data classes.

data class RestUsers(
    val results: List<RestUser>
)

data class RestUser(
    val gender: String,
    val email: String,
    val registered: String,
    val dob: String,
    val phone: String,
    val cell: String,
    val nat: String,
    val name: RestName,
    val location: RestLocation,
    val login: RestLogin,
    val id: RestId,
    val picture: RestPicture
)

data class RestName(
    val title: String,
    val first: String,
    val last: String
)

data class RestLocation(
    val street: String,
    val city: String,
    val state: String,
    val postcode: String
)

data class RestLogin(
    val username: String,
    val password: String,
    val salt: String,
    val md5: String,
    val sha1: String,
    val sha256: String
)

data class RestId(
    val name: String,
    val value: String?
)

data class RestPicture(
    val large: String,
    val medium: String,
    val thumbnail: String
)
