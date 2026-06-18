package com.christhperalta.donext.core.data

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver

actual fun createDriver(): SqlDriver {
    return NativeSqliteDriver(DoNextDatabase.Schema, "donext.db")
}
