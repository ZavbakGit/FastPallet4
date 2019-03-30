package com.anit.fastpallet4.navigation

import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.Router


class LocalCiceroneHolder {
    val containers = hashMapOf<String, Cicerone<Router>>()

    fun getCicerone(containerTag: String): Cicerone<Router> {
        if (!containers.containsKey(containerTag)) {
            containers[containerTag] = Cicerone.create()
        }
        return containers[containerTag]!!
    }


}
