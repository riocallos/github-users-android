package com.riocallos.githubusers.utils

import android.text.TextUtils
import android.util.Log
import com.riocallos.githubusers.BuildConfig
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

/**
 * Utility class for app debug message logging.
 *
 */
object AppLogger {

    private const val TAG = "GitHubUsers"

    /**
     * Log information with tag.
     *
     * @property tag [String] of the log.
     * @property message [String] of the log.
     */
    fun info(tag: String?, message: String) {
        if (BuildConfig.DEBUG) {
            if (!TextUtils.isEmpty(message)) {
                if (message.length > 4000) {
                    Log.i(tag, message.substring(0, 4000))
                    info(tag, message.substring(4000))
                } else {
                    Log.i(tag, message)
                }
            } else {
                Log.e(TAG, "Nothing to log")
            }
        }
    }

    /**
     * Log information.
     *
     * @property message [String] of the log.
     */
    private fun info(message: String?) {
        if (BuildConfig.DEBUG) {
            if (!TextUtils.isEmpty(message)) {
                Log.i(TAG, message!!)
            } else {
                Log.e(TAG, "Nothing to log")
            }
        }
    }

    /**
     * Log error.
     *
     * @property message [String] of the log.
     */
    fun error(message: String) {
        if (BuildConfig.DEBUG) {
            if (!TextUtils.isEmpty(message)) {
                val maxLogSize = 4000
                for (i in 0..message.length / maxLogSize) {
                    val start = i * maxLogSize
                    var end = (i + 1) * maxLogSize
                    end = if (end > message.length) message.length else end
                    Log.e(TAG, message.substring(start, end))
                }
            } else {
                Log.e(TAG, "Nothing to log")
            }
        }
    }

    /**
     * Log warning.
     *
     * @property message [String] of the log.
     */
    private fun warn(message: String?) {
        if (BuildConfig.DEBUG) {
            if (!TextUtils.isEmpty(message)) {
                Log.w(TAG, message!!)
            } else {
                Log.e(TAG, "Nothing to log")
            }
        }
    }

    /**
     * Log long information with tag.
     *
     * @property tag [String] of the log.
     * @property message [String] of the log.
     */
    fun longInfo(tag: String?, message: String) {
        if (BuildConfig.DEBUG) {
            if (!TextUtils.isEmpty(message )) {
                if (message .length > 4000) {
                    Log.i(tag, message .substring(0, 4000))
                    longInfo(tag, message .substring(4000))
                } else {
                    Log.i(tag, message )
                }
            } else {
                error("LongInfo msg is empty")
            }
        }
    }

    /**
     * Log long information.
     *
     * @property message [String] of the log.
     */
    private fun longInfo(message: String) {
        if (!TextUtils.isEmpty(message)) {
            if (message.length > 4000) {
                info(message.substring(0, 4000))
                longInfo(message.substring(4000))
            } else {
                info(message)
            }
        } else {
            error("LongInfo msg is empty")
        }
    }

    /**
     * Log long error.
     *
     * @property message [String] of the log.
     */
    fun longError(message: String) {
        if (!TextUtils.isEmpty(message)) {
            if (message.length > 4000) {
                error(message.substring(0, 4000))
                longError(message.substring(4000))
            } else {
                error(message)
            }
        } else {
            error("LongError msg is empty")
        }
    }

    /**
     * Log long warning.
     *
     * @property message [String] of the log.
     */
    fun longWarn(message: String) {
        if (!TextUtils.isEmpty(message)) {
            if (message.length > 4000) {
                warn(message.substring(0, 4000))
                longWarn(message.substring(4000))
            } else {
                warn(message)
            }
        } else {
            error("LongWarn msg is empty")
        }
    }

    /**
     * Log [JSONObject].
     *
     * @property `object` [JSONObject] to log.
     */
    fun prettyPrint(`object`: JSONObject) {
        try {
            val text = `object`.toString(2)
            val temp = text.split("\n").toTypedArray()
            for (s in temp) {
                info(s)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    /**
     * Log [JSONObject] with tag.
     *
     * @property tag [String] of the log.
     * @property `object` [JSONObject] to log.
     */
    fun printJSONObject(tag: String?, `object`: JSONObject) {
        if (BuildConfig.DEBUG) {
            try {
                val text = `object`.toString(2)
                val temp = text.split("\n").toTypedArray()
                for (s in temp) {
                    Log.i(tag, s)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
                printJson(`object`)
            }
        }
    }

    /**
     * Log object.
     *
     * @property any [Any] to log.
     */
    fun printJson(any: Any?) {
        if (any is JSONObject) {
            try {
                longInfo(any.toString(2))
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        } else if (any is JSONArray) {
            try {
                longInfo(any.toString(2))
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        } else if (any != null) {
            longInfo(any.toString())
        } else {
            error("Nothing to Log")
        }
    }

    /**
     * Log Retrofit error.
     *
     * @property error [Any] to log.
     */
    fun printRetrofitError(error: Any?) {
        if (error is JSONObject) {
            try {
                longError(error.toString(2))
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        } else if (error is Exception) {
            if (BuildConfig.DEBUG) {
                error("Exception Stacktrace")
                error.printStackTrace()
            }
        } else if (error is Throwable) {
            if (BuildConfig.DEBUG) {
                error("Throwable Stacktrace")
                error.printStackTrace()
            }
        } else if (error != null) {
            longError(error.toString())
        } else {
            error("Nothing to log")
        }
    }

    /**
     * Log [Map].
     *
     * @property map [Map] to log.
     */
    fun printMap(map: Map<String, String?>) {
        for (key in map.keys) {
            longInfo(key + ": " + map[key])
        }
    }

}