package nl.joozd.joozdcalendarapi

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.CalendarContract
import nl.joozd.joozdcalendarapi.helpers.mergeArrays

/**
 * Extract events from devices calendar.
 * Example:
 * <pre>
 *  val extractor = EventsExtractor{
 *      fromCalendar(calendar)
 *      endBeforeEpochMilli(latestEpochMilli)
 *  }.extract(context)
 *  </pre>
 */
class EventsExtractor private constructor(private val clause: String, private val args: Array<String>) {
    fun extract(context: Context): List<CalendarEvent>{
        val foundEvents = ArrayList<CalendarEvent>()
        context.buildCursor()?.use{ cur ->
            while (cur.moveToNext()) {
                if (cur.getInt(CalendarEvent.EVENT_DELETED_INDEX) == 0)
                    foundEvents.add(with(cur) {
                        CalendarEvent(
                            ID = getLong(CalendarEvent.EVENT_ID_INDEX),
                            calendarID = getLong(CalendarEvent.EVENT_CALENDAR_ID_INDEX),
                            title = getString(CalendarEvent.EVENT_TITLE_INDEX),
                            location = getString(CalendarEvent.EVENT_EVENT_LOCATION_INDEX),
                            description = getString(CalendarEvent.EVENT_DESCRIPTION_INDEX),
                            startEpochMillis = getLong(CalendarEvent.EVENT_DTSTART_INDEX),
                            endEpochMillis = getLong(CalendarEvent.EVENT_DTEND_INDEX),
                            timeZone = getString(CalendarEvent.EVENT_TIMEZONE_INDEX),
                            allDay = getLong(CalendarEvent.EVENT_ALL_DAY_INDEX) != 0L
                        )
                    })
            }
        }
        return foundEvents
    }

    private fun Context.buildCursor(): Cursor? {
        println("Cursor built\nclause: $clause\nargs: ${args.toList()}")
        val uri: Uri = CalendarContract.Events.CONTENT_URI
        return contentResolver.query(uri,CalendarEvent.EVENT_PROJECTION,clause,args,null)
    }


    @Suppress("unused")
    class Builder{
        private val clauses = ArrayList<String>()
        private val args = ArrayList<Array<String>>()

        /**
         * Only get events from [calendar]
         */
        fun fromCalendar(calendar: CalendarDescriptor){
            val selectionArgs = listOf(calendar.ID.toString())
            add(CALENDAR_ID_CLAUSE, selectionArgs)
        }

        /**
         * Only get events from calendar with ID [id]
         */
        fun fromCalendarID(id: Long){
            val selectionArgs = listOf(id.toString())
            add(CALENDAR_ID_CLAUSE, selectionArgs)
        }

        /**
         * Only get events whose _ID is in [ids]
         * @param ids: zero or more IDs to match
         */
        fun requireID(vararg ids: Long){
            val selectionClause = ids.joinToString(" OR ") { REQUIRE_ID_CLAUSE }
            val selectionArgs = ids.map { it.toString() }
            add(selectionClause, selectionArgs)
        }

        /**
         * Only get events starting before [epochMilli]
         * @param epochMilli Cutoff time in milliseconds since UTC Epoch.
         * @param inclusive If true, will return events starting on [epochMilli]
         */
        fun startBeforeEpochMilli(epochMilli: Long, inclusive: Boolean = false){
            val selectionClause = if (inclusive) START_BEFORE_OR_AT_CLAUSE else START_BEFORE_CLAUSE
            val selectionArgs = listOf(epochMilli.toString())
            add(selectionClause, selectionArgs)
        }

        /**
         * Only get events starting before [epochMilli]
         * @param epochMilli Cutoff time in milliseconds since UTC Epoch.
         * @param inclusive If true, will return events starting on [epochMilli]
         */
        fun startAfterEpochMilli(epochMilli: Long, inclusive: Boolean = false){
            val selectionClause = if (inclusive) START_AT_OR_AFTER_CLAUSE else START_AFTER_CLAUSE
            val selectionArgs = listOf(epochMilli.toString())
            add(selectionClause, selectionArgs)
        }

        /**
         * Only get events starting at exactly [epochMilli]
         * @param epochMilli Time in milliseconds since UTC Epoch.
         */
        fun startAtEpochMilli(epochMilli: Long){
            val selectionClause = START_AT_CLAUSE
            val selectionArgs = listOf(epochMilli.toString())
            add(selectionClause, selectionArgs)
        }

        /**
         * Only get events starting before [epochMilli]
         * @param epochMilli Cutoff time in milliseconds since UTC Epoch.
         * @param inclusive If true, will return events starting on [epochMilli]
         */
        fun endBeforeEpochMilli(epochMilli: Long, inclusive: Boolean = false){
            val selectionClause = if (inclusive) END_BEFORE_OR_AT_CLAUSE else END_BEFORE_CLAUSE
            val selectionArgs = listOf(epochMilli.toString())
            add(selectionClause, selectionArgs)
        }

        /**
         * Only get events starting before [epochMilli]
         * @param epochMilli Cutoff time in milliseconds since UTC Epoch.
         * @param inclusive If true, will return events starting on [epochMilli]
         */
        fun endAfterEpochMilli(epochMilli: Long, inclusive: Boolean = false){
            val selectionClause = if (inclusive) END_AT_OR_AFTER_CLAUSE else END_AFTER_CLAUSE
            val selectionArgs = listOf(epochMilli.toString())
            add(selectionClause, selectionArgs)
        }

        /**
         * Only get events ending at exactly [epochMilli]
         * @param epochMilli Time in milliseconds since UTC Epoch.
         */
        fun endAtEpochMilli(epochMilli: Long){
            val selectionClause = END_AT_CLAUSE
            val selectionArgs = listOf(epochMilli.toString())
            add(selectionClause, selectionArgs)
        }

        /**
         * Only get events that start in [range]
         * @param range: Range of times in milliseconds since UTC Epoch
         * @param inclusive: If true, will return events starting on start or end of [range]
         */
        fun startInEpochMilliRange(range: ClosedRange<Long>, inclusive: Boolean = true){
            startAfterEpochMilli(range.start, inclusive)
            startBeforeEpochMilli(range.endInclusive, inclusive)
        }

        /**
         * Builds the [EventsExtractor]
         */
        fun build(): EventsExtractor {
            val combinedClause = if (clauses.isEmpty()) "" else "(" + clauses.joinToString(" AND ") + ")"
            val combinedArgs = mergeArrays(args)
            return EventsExtractor(combinedClause, combinedArgs)
        }

        private fun add(selectionClause: String, selectionArgs: Collection<String>){
            clauses.add(selectionClause)
            args.add(selectionArgs.toTypedArray())
        }

        companion object{
            operator fun invoke(block: Builder.() -> Unit): EventsExtractor =
                Builder().apply{
                    block()
                }.build()
        }
    }


    companion object{
        operator fun invoke(block: Builder.() -> Unit): EventsExtractor =
            Builder().apply{
                block()
            }.build()

        private const val CALENDAR_ID_CLAUSE = "(${CalendarContract.Events.CALENDAR_ID} = ?)"
        private const val REQUIRE_ID_CLAUSE = "(${CalendarContract.Events._ID} = ?)"
        private const val START_BEFORE_CLAUSE = "(${CalendarContract.Events.DTSTART} < ?)"
        private const val START_BEFORE_OR_AT_CLAUSE = "(${CalendarContract.Events.DTSTART} <= ?)"
        private const val START_AFTER_CLAUSE = "(${CalendarContract.Events.DTSTART} > ?)"
        private const val START_AT_OR_AFTER_CLAUSE = "(${CalendarContract.Events.DTSTART} >= ?)"
        private const val START_AT_CLAUSE = "(${CalendarContract.Events.DTSTART} = ?)"
        private const val END_BEFORE_CLAUSE = "(${CalendarContract.Events.DTEND} < ?)"
        private const val END_BEFORE_OR_AT_CLAUSE = "(${CalendarContract.Events.DTEND} <= ?)"
        private const val END_AFTER_CLAUSE = "(${CalendarContract.Events.DTEND} > ?)"
        private const val END_AT_CLAUSE = "(${CalendarContract.Events.DTEND} = ?)"
        private const val END_AT_OR_AFTER_CLAUSE = "(${CalendarContract.Events.DTEND} >= ?)"
    }
}
