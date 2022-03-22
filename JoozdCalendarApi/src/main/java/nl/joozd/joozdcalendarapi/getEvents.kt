package nl.joozd.joozdcalendarapi

import android.content.Context

/**
 * Get all events in all calendars.
 */
fun Context.getEvents(): List<CalendarEvent> =
    with(EventsExtractor.Builder().build()){
        extract(this@getEvents)
    }

fun Context.getEvents(calendar: CalendarDescriptor): List<CalendarEvent> =
    with(EventsExtractor.Builder().apply{
        fromCalendar(calendar)
    }.build()){
        extract(this@getEvents)
    }
