package nl.joozd.joozdcalendarapi

import android.Manifest
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.CalendarContract
import androidx.annotation.RequiresPermission

@RequiresPermission(Manifest.permission.READ_CALENDAR)
fun Context.getCalendars(): List<CalendarDescriptor> {
    val results = ArrayList<CalendarDescriptor>()
    buildCalendarCursor()?.use { cur ->
        while (cur.moveToNext()) {
            results.add(cur.getCalendarDescriptor())
        }
    }
    return results
}

private fun Context.buildCalendarCursor(): Cursor?{
    val uri: Uri = CalendarContract.Calendars.CONTENT_URI
    return contentResolver.query(uri, CalendarDescriptor.CALENDAR_PROJECTION, null, null, null)
}

private fun Cursor.getCalendarDescriptor(): CalendarDescriptor {
    val calID: Long = getLong(CalendarDescriptor.PROJECTION_ID_INDEX)
    val displayName: String = getString(CalendarDescriptor.PROJECTION_DISPLAY_NAME_INDEX)
    val accountName: String = getString(CalendarDescriptor.PROJECTION_ACCOUNT_NAME_INDEX)
    val accountType: String = getString(CalendarDescriptor.PROJECTION_ACCOUNT_TYPE_INDEX)
    val ownerName: String = getString(CalendarDescriptor.PROJECTION_OWNER_ACCOUNT_INDEX)

    return CalendarDescriptor(
        calID,
        displayName,
        accountName,
        accountType,
        ownerName
    )
}
