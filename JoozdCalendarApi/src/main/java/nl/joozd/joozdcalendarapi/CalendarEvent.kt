package nl.joozd.joozdcalendarapi

import android.provider.CalendarContract

data class CalendarEvent(
    val ID: Long,
    val calendarID: Long,       // The _ID of the calendar the event belongs to.
    val title: String,          // The title of the event.
    val location: String,       // Where the event takes place.
    val description: String,    // The description of the event.
    val startEpochMillis: Long, // The time the event starts in UTC milliseconds since the epoch.
    val endEpochMillis: Long,   // The time the event ends in UTC milliseconds since the epoch.
    val timeZone: String,       // The time zone for the event.
    val allDay: Boolean         // true means this event occupies the entire day, as defined by the local time zone.
                                // false means it is a regular event that may start and end at any time during a day.
) {
    companion object{
        internal val EVENT_PROJECTION: Array<String> = arrayOf(
            CalendarContract.Events._ID,
            CalendarContract.Events.CALENDAR_ID,
            CalendarContract.Events.TITLE,
            CalendarContract.Events.EVENT_LOCATION,
            CalendarContract.Events.DESCRIPTION,
            CalendarContract.Events.DTSTART,
            CalendarContract.Events.DTEND,
            CalendarContract.Events.EVENT_TIMEZONE,
            CalendarContract.Events.ALL_DAY,
            CalendarContract.Events.DELETED
        )

        internal val EVENT_ID_INDEX: Int = EVENT_PROJECTION.indexOf(CalendarContract.Events._ID)
        internal val EVENT_CALENDAR_ID_INDEX: Int = EVENT_PROJECTION.indexOf(CalendarContract.Events.CALENDAR_ID)
        internal val EVENT_TITLE_INDEX: Int = EVENT_PROJECTION.indexOf(CalendarContract.Events.TITLE)
        internal val EVENT_EVENT_LOCATION_INDEX: Int = EVENT_PROJECTION.indexOf(CalendarContract.Events.EVENT_LOCATION)
        internal val EVENT_DESCRIPTION_INDEX: Int = EVENT_PROJECTION.indexOf(CalendarContract.Events.DESCRIPTION)
        internal val EVENT_DTSTART_INDEX: Int = EVENT_PROJECTION.indexOf(CalendarContract.Events.DTSTART)
        internal val EVENT_DTEND_INDEX: Int = EVENT_PROJECTION.indexOf(CalendarContract.Events.DTEND)
        internal val EVENT_TIMEZONE_INDEX: Int = EVENT_PROJECTION.indexOf(CalendarContract.Events.EVENT_TIMEZONE)
        internal val EVENT_ALL_DAY_INDEX: Int = EVENT_PROJECTION.indexOf(CalendarContract.Events.ALL_DAY)
        internal val EVENT_DELETED_INDEX: Int = EVENT_PROJECTION.indexOf(CalendarContract.Events.DELETED) // this is here so we can remove deleted events from results
    }
}