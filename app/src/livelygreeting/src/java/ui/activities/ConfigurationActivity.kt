package ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.platform.LocalContext
import com.kieronquinn.app.smartspacer.sdk.provider.SmartspacerTargetProvider
import nodomain.pacjo.smartspacer.plugin.R
import nodomain.pacjo.smartspacer.plugin.ui.components.PreferenceHeading
import nodomain.pacjo.smartspacer.plugin.ui.components.PreferenceLayout
import nodomain.pacjo.smartspacer.plugin.ui.components.PreferenceSwitch
import nodomain.pacjo.smartspacer.plugin.ui.theme.PacjoTheme
import nodomain.pacjo.smartspacer.plugin.utils.getBoolFromDataStore
import nodomain.pacjo.smartspacer.plugin.utils.saveToDataStore
import targets.LivelyGreetingTarget
import targets.dataStore

class ConfigurationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current

            val hideNoComplications = getBoolFromDataStore(context.dataStore, "target_hide_no_complications") ?: false

            PacjoTheme {
                PreferenceLayout("Lively Greeting") {
                    PreferenceHeading(
                        heading = "Greeting target"
                    )
                    PreferenceSwitch(
                        icon = R.drawable.eye_off,
                        title = "Dynamically hide",
                        description = "Hide target when no complications are available",
                        onCheckedChange = {
                            value -> saveToDataStore(context.dataStore, "target_hide_no_complications", value)
                            SmartspacerTargetProvider.notifyChange(context, LivelyGreetingTarget::class.java)
                        },
                        checked = hideNoComplications
                    )
                }
            }
        }
    }
}
