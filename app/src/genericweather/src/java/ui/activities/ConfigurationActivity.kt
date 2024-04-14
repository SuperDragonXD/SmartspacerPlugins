package ui.activities

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.platform.LocalContext
import com.kieronquinn.app.smartspacer.sdk.provider.SmartspacerComplicationProvider
import com.kieronquinn.app.smartspacer.sdk.provider.SmartspacerTargetProvider
import nodomain.pacjo.smartspacer.plugin.R
import complications.GenericSunTimesComplication
import complications.GenericWeatherComplication
import nodomain.pacjo.smartspacer.plugin.ui.components.PreferenceHeading
import nodomain.pacjo.smartspacer.plugin.ui.components.PreferenceInput
import nodomain.pacjo.smartspacer.plugin.ui.components.PreferenceLayout
import nodomain.pacjo.smartspacer.plugin.ui.components.PreferenceMenu
import nodomain.pacjo.smartspacer.plugin.ui.components.PreferenceSlider
import nodomain.pacjo.smartspacer.plugin.ui.components.PreferenceSwitch
import nodomain.pacjo.smartspacer.plugin.utils.savePreference
import nodomain.pacjo.smartspacer.plugin.ui.theme.PacjoTheme
import targets.GenericWeatherTarget
import nodomain.pacjo.smartspacer.plugin.utils.isFirstRun
import org.json.JSONObject
import java.io.File

class ConfigurationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            isFirstRun(context)

            // get number of forecast points (as we need it to show the default)
            val file = File(context.filesDir, "data.json")
            val jsonObject = JSONObject(file.readText())
            val preferences = jsonObject.getJSONObject("preferences")
            val dataPoints = preferences.optInt("target_points_visible", 4)
            val launchPackage = preferences.optString("launch_package", "")

            val conditionComplicationTrimToFit = preferences.optBoolean("condition_complication_trim_to_fit", true)
            val sunTimesComplicationTrimToFit = preferences.optBoolean("suntimes_complication_trim_to_fit", true)

            PacjoTheme {
                PreferenceLayout("Generic Weather") {
                    PreferenceHeading(heading = "Weather target")

                    PreferenceSlider(
                        icon = R.drawable.calendar_range_outline,
                        title = "Forecast points to show",
                        description = "Select number of visible forecast days/hours",
                        onSliderChange = {
                                value -> savePreference(context,"target_points_visible", value)
                            Log.i("pacjodebug", "callback, value: $value")
                            SmartspacerTargetProvider.notifyChange(context, GenericWeatherTarget::class.java)
                        },
                        range = (0..4),
                        defaultPosition = dataPoints.toFloat()
                    )

                    PreferenceMenu(
                        icon = R.drawable.palette_outline,
                        title = "Style",
                        description = "Select target style",
                        onItemChange = {
                                value -> savePreference(context,"target_style", value)
                            SmartspacerTargetProvider.notifyChange(context, GenericWeatherTarget::class.java)
                        },
                        items = listOf(
                            Pair("Temperature only", "temperature"),
                            Pair("Condition only", "condition"),
                            Pair("Temperature and condition", "both")
                        )
                    )

                    PreferenceMenu(
                        icon = R.drawable.thermometer,
                        title = "Unit",
                        description = "Select temperature unit",
                        onItemChange = {
                                value -> savePreference(context,"target_unit", value)
                            SmartspacerTargetProvider.notifyChange(context, GenericWeatherTarget::class.java)
                        },
                        items = listOf(
                            Pair("Kelvin", "K"),
                            Pair("Celsius", "C"),
                            Pair("Fahrenheit", "F")
                        )
                    )

                    PreferenceInput(
                        icon = R.drawable.package_variant,
                        title = "Launch Package",
                        description = "Select package name of an app to open when target is clicked",
                        onTextChange = {
                                value -> savePreference(context,"target_launch_package", value)
                            SmartspacerComplicationProvider.notifyChange(context, GenericWeatherComplication::class.java)
                        },
                        dialogText = "Enter package name",
                        defaultText = launchPackage
                    )

                    PreferenceHeading(
                        heading = "Weather complication"
                    )

                    PreferenceMenu(
                        icon = R.drawable.palette_outline,
                        title = "Style",
                        description = "Select complication style",
                        onItemChange = {
                                value -> savePreference(context,"condition_complication_style", value)
                            SmartspacerComplicationProvider.notifyChange(context, GenericWeatherComplication::class.java)
                        },
                        items = listOf(
                            Pair("Temperature only", "temperature"),
                            Pair("Condition only", "condition"),
                            Pair("Temperature and condition", "both")
                        )
                    )

                    PreferenceMenu(
                        icon = R.drawable.thermometer,
                        title = "Unit",
                        description = "Select temperature unit",
                        onItemChange = {
                                value -> savePreference(context,"condition_complication_unit", value)
                            SmartspacerComplicationProvider.notifyChange(context, GenericWeatherComplication::class.java)
                        },
                        items = listOf(
                            Pair("Kelvin", "K"),
                            Pair("Celsius", "C"),
                            Pair("Fahrenheit", "F")
                        )
                    )

                    PreferenceInput(
                        icon = R.drawable.package_variant,
                        title = "Launch Package",
                        description = "Select package name of an app to open when complication is clicked",
                        onTextChange = {
                                value -> savePreference(context,"condition_complication_launch_package", value)
                            SmartspacerComplicationProvider.notifyChange(context, GenericWeatherComplication::class.java)
                        },
                        dialogText = "Enter package name",
                        defaultText = launchPackage
                    )

                    PreferenceSwitch(
                        icon = R.drawable.content_cut,
                        title = "Complication text trimming",
                        description = "Disable this if text is getting cut off. \nMay cause unexpected results",
                        onCheckedChange = {
                                value -> savePreference(context,"condition_complication_trim_to_fit", value)
                            SmartspacerComplicationProvider.notifyChange(context, GenericWeatherComplication::class.java)
                        },
                        checked = conditionComplicationTrimToFit
                    )

                    PreferenceHeading(
                        heading = "Sun times target"
                    )

                    PreferenceSwitch(
                        icon = R.drawable.content_cut,
                        title = "Complication text trimming",
                        description = "Disable this if text is getting cut off. \nMay cause unexpected results",       // TODO: fix (also exchange with RelativeSizeSpan)
                        onCheckedChange = {
                                value -> savePreference(context,"suntimes_complication_trim_to_fit", value)
                            SmartspacerComplicationProvider.notifyChange(context, GenericSunTimesComplication::class.java)
                        },
                        checked = sunTimesComplicationTrimToFit
                    )
                }
            }
        }
    }
}

