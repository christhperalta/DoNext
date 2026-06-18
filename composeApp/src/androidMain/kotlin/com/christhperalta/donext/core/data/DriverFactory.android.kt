package com.christhperalta.donext.core.data

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver

object AndroidAppContext {
    lateinit var context: Context
}

actual fun createDriver(): SqlDriver {
    return AndroidSqliteDriver(DoNextDatabase.Schema, AndroidAppContext.context, "donext.db")
}
