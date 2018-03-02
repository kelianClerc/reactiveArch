package com.fabernovel.technologies.data.net.retrofit

import com.fabernovel.technologies.core.*

// Add REST mappers here. Prefer pure functions rather than classes when possible.

fun mapUser(toMap: RestUsers?): List<User> = toMap?.results?.map {
    User(
        it.gender,
        it.email,
        it.registered,
        it.dob,
        it.phone,
        it.cell,
        it.nat,
        mapName(it.name),
        mapLocation(it.location),
        mapLogin(it.login),
        mapId(it.id),
        mapPicture(it.picture)
    )
} ?: emptyList()

fun mapName(toMap: RestName) = Name(
    toMap.title,
    toMap.first,
    toMap.last
)

fun mapLocation(toMap: RestLocation) = Location(
    toMap.street,
    toMap.city,
    toMap.state,
    toMap.postcode
)

fun mapLogin(toMap: RestLogin) = Login(
    toMap.username,
    toMap.password,
    toMap.salt,
    toMap.md5,
    toMap.sha1,
    toMap.sha256
)

fun mapId(toMap: RestId) = Id(
    toMap.name,
    toMap.value
)

fun mapPicture(toMap: RestPicture) = Picture(
    toMap.large,
    toMap.medium,
    toMap.thumbnail
)
