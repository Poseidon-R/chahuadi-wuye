package com.czl.module_car.utils

import com.annimon.stream.Stream

object RoomUtil {

    fun getRoomName(rooms: List<String>, houseId: String): String {
        var optional = Stream.of(rooms).filter { e -> e.indexOf(houseId) != -1 }
            .map { e -> e.substringAfter(houseId, "") }.findFirst()
        if (optional != null && optional.isPresent) {
            return optional.get()
        }
        return ""
    }
}