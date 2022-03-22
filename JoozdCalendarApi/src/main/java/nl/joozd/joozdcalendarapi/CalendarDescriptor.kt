package nl.joozd.joozdcalendarapi

import android.provider.CalendarContract

data class CalendarDescriptor(
    val ID: Long,
    val displayName: String,
    val accountName: String,
    val accountType: String,
    val owner: String,
){
    companion object{
        internal val CALENDAR_PROJECTION: Array<String> = arrayOf(
            CalendarContract.Calendars._ID,
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
            CalendarContract.Calendars.ACCOUNT_NAME,
            CalendarContract.Calendars.ACCOUNT_TYPE,
            CalendarContract.Calendars.OWNER_ACCOUNT
        )

        // The indices for the projection array above.
        internal val PROJECTION_ID_INDEX: Int = CALENDAR_PROJECTION.indexOf(CalendarContract.Calendars._ID)
        internal val PROJECTION_DISPLAY_NAME_INDEX: Int = CALENDAR_PROJECTION.indexOf(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME)
        internal val PROJECTION_ACCOUNT_NAME_INDEX: Int = CALENDAR_PROJECTION.indexOf(CalendarContract.Calendars.ACCOUNT_NAME)
        internal val PROJECTION_ACCOUNT_TYPE_INDEX: Int = CALENDAR_PROJECTION.indexOf(CalendarContract.Calendars.ACCOUNT_TYPE)
        internal val PROJECTION_OWNER_ACCOUNT_INDEX: Int = CALENDAR_PROJECTION.indexOf(CalendarContract.Calendars.OWNER_ACCOUNT)
    }
}
