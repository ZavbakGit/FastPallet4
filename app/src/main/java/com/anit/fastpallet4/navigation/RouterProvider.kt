package com.anit.fastpallet4.navigation

import ru.terrakok.cicerone.Router

/**
 * Created by agladkov on 07.02.18.
 */
interface RouterProvider {
    fun getRouter(): Router
}