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
import targets.LocalBatteryTarget
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
            val showEstimate = preferences.optBoolean("target_show_estimate", true)
            val disableComplications = preferences.optBoolean("target_disable_complications", false)

            PacjoTheme {
                PreferenceLayout(
                    title = "Local Battery"
                ) {
                    PreferenceHeading(
                        heading = "Charging target"
                    )
                    PreferenceSwitch(
                        icon = R.drawable.clock_time_ten_outline,
                        title = "Charging estimate",
                        description = "Show time to charge",
                        onCheckedChange = {
                                value -> savePreference(context,"target_show_estimate", value)
                            SmartspacerTargetProvider.notifyChange(context, LocalBatteryTarget::class.java)
                        },
                        checked = showEstimate
                    )
                    PreferenceSwitch(
                        icon = R.drawable.card_off_outline,
                        title = "Force no complications",
                        description = "Disables complication spot",
                        onCheckedChange = {
                                value -> savePreference(context,"target_disable_complications", value)
                            SmartspacerTargetProvider.notifyChange(context, LocalBatteryTarget::class.java)
                        },
                        checked = disableComplications
                    )
                }
            }
        }
    }
}
