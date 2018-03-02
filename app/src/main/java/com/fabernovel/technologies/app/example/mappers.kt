package com.fabernovel.technologies.app.example

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

fun mapTime(time: DateTime): String = DateTimeFormat.fullDateTime().print(time)
