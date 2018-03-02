package com.fabernovel.technologies.utils

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.fabernovel.technologies.Settings
import com.fabernovel.technologies.app.common.BaseActivity
import net.hockeyapp.android.CrashManagerListener
import timber.log.Timber
import java.lang.ref.WeakReference
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HockeyAppCrashManagerListener @Inject internal constructor() :
    CrashManagerListener(),
    Application.ActivityLifecycleCallbacks
{
    private val activityStack = LinkedList<String>()
    private var mostRecentActivity: WeakReference<Activity>? = null

    override fun shouldAutoUploadCrashes(): Boolean {
        return ENABLED
    }

    override fun getDescription(): String? {
        if (!ADDITIONAL_DATA) {
            return null
        }

        val stack = stackDescription
        val view = viewDescription
        val trace = traceDescription

        return stack + view + trace
    }


    private fun formatLogs(logs: LinkedList<String>, title: String): String {
        val sb = StringBuilder()

        sb.append(title).append('\n')

        val underline = String(CharArray(title.length)).replace('\u0000', '=')
        sb.append(underline).append("\n\n")

        for (s in logs) {
            sb.append(s).append('\n')
        }

        sb.append("\n\n")
        return sb.toString()
    }

    private val traceDescription: String
        get() {
            val tree = Timber.forest().find { it is HockeyAppTree }
            val logs = (tree as HockeyAppTree?)?.logs ?: LinkedList()
            return formatLogs(logs, "TRACE LOGS")
        }

    private val stackDescription: String
        get() = formatLogs(activityStack, "ACTIVITY STACK")

    private val viewDescription: String
        get() {
            val sb = StringBuilder(BUFFER_SIZE)
            sb.append("VIEW DUMP\n=========\n\n")

            if (mostRecentActivity != null) {
                val activity = mostRecentActivity!!.get()
                if (activity == null) {
                    sb.append("Unable to dump view hierarchy\n")
                } else {
                    val content = activity.findViewById<View>(android.R.id.content)
                    appendViewHierarchy(sb, "", content)
                }
            }

            sb.append("\n\n")
            return sb.toString()
        }


    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        val desc = getActivityDesc(activity)
        activityStack.add(desc)
    }

    private fun getActivityDesc(activity: Activity): String {
        var desc = getObjectDescription(activity)

        if (activity is BaseActivity<*, *, *>) {
            val fragmentManager = (activity as AppCompatActivity).supportFragmentManager
            val backstackCount = fragmentManager.backStackEntryCount
            val builder = StringBuilder(desc)
            for (i in 0 until backstackCount) {
                builder.append("\n ").append(fragmentManager.getBackStackEntryAt(i).name)
            }
            desc = builder.toString()
        }

        return desc
    }

    override fun onActivityStarted(activity: Activity) {
        // no-op
    }

    override fun onActivityResumed(activity: Activity) {
        mostRecentActivity = WeakReference(activity)
    }

    override fun onActivityPaused(activity: Activity) {
        // no-op
    }

    override fun onActivityStopped(activity: Activity) {
        // no-op
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle?) {
        // no-op
    }

    override fun onActivityDestroyed(activity: Activity) {
        activityStack.pop()
    }

    companion object {
        private val ENABLED = Settings.crashes.enabled
        private val ADDITIONAL_DATA = Settings.crashes.additional_data
        private val BUFFER_SIZE = 128

        private fun appendViewHierarchy(sb: StringBuilder, prefix: String, view: View) {
            val desc = getPrintableView(prefix, view)
            sb.append(desc).append('\n')
            if (view is ViewGroup) {
                val deepPrefix = deepenPrefix(prefix)
                for (i in 0 until view.childCount) {
                    val v = view.getChildAt(i)
                    appendViewHierarchy(sb, deepPrefix, v)
                }
            }
        }

        private fun deepenPrefix(prefix: String): String {
            return if (prefix.isEmpty()) {
                "| " + prefix
            } else "  " + prefix
        }

        private fun getPrintableView(prefix: String, view: View): String {
            return String.format(Locale.getDefault(),
                "%s%s (%.0f, %.0f, %d, %d) %s",
                prefix,
                view.javaClass.simpleName,
                view.x,
                view.y,
                view.width,
                view.height,
                getPrintableId(view))
        }

        private fun getPrintableId(view: View): String {
            return if (view.id == -1) {
                ""
            } else view.resources.getResourceName(view.id)
        }

        private fun getObjectDescription(`object`: Any): String {
            val hash = System.identityHashCode(`object`)
            return String.format(
                Locale.getDefault(),
                "%s@%X",
                `object`.javaClass.simpleName,
                hash
            )
        }
    }
}

class HockeyAppTree : Timber.Tree() {

    val logs = LinkedList<String>()

    override fun log(priority: Int, tag: String?, message: String?, t: Throwable?) {
        synchronized(this) {
            if (priority < Log.DEBUG) return
            logs.add(formatLogs(priority, tag, message, t))
            while (logs.size > MAX_TRACE) logs.removeFirst()
        }
    }

    private fun formatLogs(priority: Int, tag: String?, message: String?, t: Throwable?): String {
        val p = when (priority) {
            Log.DEBUG -> "D"
            Log.INFO -> "I"
            Log.WARN -> "W"
            Log.ERROR -> "E"
            Log.ASSERT -> "WTF"
            else -> "X"
        }
        return "$p/$tag: ${message ?: ""}${t ?: ""}"
    }

    companion object {
        val MAX_TRACE = Settings.crashes.max_trace
    }
}
