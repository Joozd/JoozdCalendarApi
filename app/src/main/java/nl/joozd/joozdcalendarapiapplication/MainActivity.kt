package nl.joozd.joozdcalendarapiapplication

import android.Manifest
import android.content.DialogInterface
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import nl.joozd.joozdcalendarapi.CalendarDescriptor
import nl.joozd.joozdcalendarapi.EventsExtractor
import nl.joozd.joozdcalendarapi.getCalendars
import nl.joozd.joozdcalendarapi.getEvents
import nl.joozd.joozdcalendarapiapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val activity = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityMainBinding.inflate(layoutInflater).apply {
            if (!checkReadCalendarPermission()) requestReadCalendarPermission()
            else {
                test1()
                result2.setOnClickListener {
                    buildCalendarPickerDialog(getCalendars()).show()
                }
            }

            setContentView(root)
        }
    }

    override fun onRequestPermissionsResult(requestCode : Int ,
                                            permissions: Array<String>,
                                            grantResults: IntArray){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        recreate()
    }

    @RequiresPermission(Manifest.permission.READ_CALENDAR)
    private fun ActivityMainBinding.test1(){
        lifecycleScope.launch{
            val cals = getCalendars().joinToString("\n-\n")
            val events = getEvents().joinToString("\n-\n")
            result1.text = cals
            result2.text = events
        }
    }

    private fun ActivityMainBinding.buildCalendarPickerDialog(calendars: List<CalendarDescriptor>) =
        AlertDialog.Builder(activity).apply{
            setTitle("Pick calendar")
            setItems(calendars.map { "${it.ID} - ${it.displayName}"}.toTypedArray(),
                DialogInterface.OnClickListener { _, p ->
                    test2(calendars[p])
            })
        }.create()

    private fun ActivityMainBinding.test2(calendar: CalendarDescriptor){
        result2.text = getEvents(calendar).joinToString("\n-\n")
    }

    private fun checkReadCalendarPermission() =
        (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR)
                == PackageManager.PERMISSION_GRANTED)

    private fun requestReadCalendarPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR), 0
        )
    }

}