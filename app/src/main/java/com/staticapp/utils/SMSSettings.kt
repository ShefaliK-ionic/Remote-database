package com.staticapp.utils

import android.content.Context
import android.preference.PreferenceManager
import com.klinker.android.send_message.Settings


class SMSSettings private constructor() {
    var mmsc: String? = null
        private set
    var mmsProxy: String? = null
        private set
    var mmsPort: String? = null
        private set

    companion object {
        private var settings: SMSSettings? = null
        private const val MMSC_PREF = "mmsc_url"
        private const val MMS_PROXY_PREF = "mms_proxy"
        private const val MMS_PORT_PREF = "mms_port"

        @JvmOverloads
        operator fun get(context: Context, forceReload: Boolean = false): SMSSettings? {
            if (settings == null || forceReload) {
                settings = init(context)
            }
            return settings
        }

        private fun init(context: Context): SMSSettings {
            val settings = SMSSettings()
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            settings.mmsc = sharedPreferences.getString(MMSC_PREF, "")
            settings.mmsProxy = sharedPreferences.getString(MMS_PROXY_PREF, "")
            settings.mmsPort = sharedPreferences.getString(MMS_PORT_PREF, "")
            return settings
        }
    }
}