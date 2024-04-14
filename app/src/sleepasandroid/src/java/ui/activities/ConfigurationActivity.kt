package ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import com.kieronquinn.app.smartspacer.sdk.provider.SmartspacerTargetProvider
import nodomain.pacjo.smartspacer.plugin.R
import nodomain.pacjo.smartspacer.plugin.ui.components.PreferenceHeading
import nodomain.pacjo.smartspacer.plugin.ui.components.PreferenceLayout
import nodomain.pacjo.smartspacer.plugin.ui.components.PreferenceSwitch
import nodomain.pacjo.smartspacer.plugin.utils.savePreference
import nodomain.pacjo.smartspacer.plugin.ui.theme.PacjoTheme
import nodomain.pacjo.smartspacer.plugin.utils.isFirstRun
import org.json.JSONObject
import targets.SleepMessagesTarget
import java.io.File

class ConfigurationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current
            isFirstRun(context)

            // get number of forecast points (as we need it to show the default)
            val file = File(context.filesDir, "data.json")
            val jsonObject = JSONObject(file.readText())
            val preferences = jsonObject.getJSONObject("preferences")
            val simpleStyle = preferences.optBoolean("simple_style", false)
            val showTimeToBed = preferences.optBoolean("show_time_to_bed", true)
            val showAlarmDismissed = preferences.optBoolean("show_alarm_dismissed", true)

            PacjoTheme{
                PreferenceLayout(title = "Sleep as Android") {
                    PreferenceHeading(
                        heading = "Messages target"
                    )

                    PreferenceSwitch(
                        icon = R.drawable.cog,
                        title = "Simple style",
                        description = "Show basic target instead\n of image one",
                        onCheckedChange = {
                                value -> savePreference(context,"simple_style", value)
                            SmartspacerTargetProvider.notifyChange(context, SleepMessagesTarget::class.java)
                        },
                        checked = simpleStyle
                    )

                    PreferenceSwitch(
                        icon = R.drawable.bed_outline,
                        title = "Time to bed",
                        description = "Show time to bed message",
                        onCheckedChange = {
                                value -> savePreference(context,"show_time_to_bed", value)
                            SmartspacerTargetProvider.notifyChange(context, SleepMessagesTarget::class.java)
                        },
                        checked = showTimeToBed
                    )

                    PreferenceSwitch(
                        icon = R.drawable.tea_outline,
                        title = "Alarm dismissed",
                        description = "Show alarm dismissed message",
                        onCheckedChange = {
                                value -> savePreference(context,"show_alarm_dismissed", value)
                            SmartspacerTargetProvider.notifyChange(context, SleepMessagesTarget::class.java)
                        },
                        checked = showAlarmDismissed
                    )
                }
            }
        }
    }
}
