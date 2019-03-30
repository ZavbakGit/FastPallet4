package com.anit.fastpallet4.data.repositories

import io.realm.Realm

class RealmInitLocal {

    private val localRealms = ThreadLocal<Realm>()

    /**
     * Opens a reference-counted local Realm instance.
     *
     * @return the open Realm instance
     */
    fun openLocalInstance(): Realm {
        checkDefaultConfiguration()
        val realm = Realm.getDefaultInstance() // <-- maybe this should be configurable
        if (localRealms.get() == null) {
            localRealms.set(realm)
        }
        return realm
    }

    /**
     * Returns the local Realm instance without adding to the reference count.
     *
     * @return the local Realm instance
     * @throws IllegalStateException when no Realm is open
     */
    fun getLocalInstance(): Realm {
        return localRealms.get() ?: throw IllegalStateException(
            "No open Realms were found on this thread."
        )
    }


    private fun checkDefaultConfiguration() {
        if (Realm.getDefaultConfiguration() == null) {
            throw IllegalStateException("No default configuration is set.")
        }
    }

    /**
     * Closes local Realm instance, decrementing the reference count.
     *
     * @throws IllegalStateException if there is no open Realm.
     */
    fun closeLocalInstance() {
        checkDefaultConfiguration()
        val realm = localRealms.get() ?: throw IllegalStateException(
            "Cannot close a Realm that is not open."
        )
        realm.close()

        if (Realm.getLocalInstanceCount(Realm.getDefaultConfiguration()!!) <= 0) {
            localRealms.set(null)
        }
    }

}