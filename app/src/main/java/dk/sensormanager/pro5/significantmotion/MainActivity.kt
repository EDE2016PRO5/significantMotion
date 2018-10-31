package dk.sensormanager.pro5.significantmotion

import android.content.Context
import android.hardware.*
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(){
    private var mSensorManager: SensorManager? = null
    private var mSignificantMotion : Sensor?= null
    private var mListener: TriggerListener? = null


    class TriggerListener(private val mContext : Context, private val mTextView: TextView) : TriggerEventListener() {
        override fun onTrigger(event: TriggerEvent?) {
            mTextView.append(mContext.getString(R.string.sig_motion) + "\n")
            mTextView.append(mContext.getString(R.string.sig_motion_auto_disabled) + "\n")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mSignificantMotion= mSensorManager?.getDefaultSensor(Sensor.TYPE_SIGNIFICANT_MOTION)

        mListener=TriggerListener(this, txtWalk)

        mSignificantMotion?.also { sensor ->
            mSensorManager?.requestTriggerSensor(mListener, sensor)
        }
    }

    override fun onResume() {
        super.onResume()
        if (mSignificantMotion != null)
            mSensorManager?.requestTriggerSensor(mListener, mSignificantMotion)
        txtWalk.append(getString(R.string.sig_motion_enabled) + "\n")
    }

    override fun onPause() {
        super.onPause()
        // Call disable only if needed for cleanup.
        // The sensor is auto disabled when triggered.
        if (mSignificantMotion != null) {
            mSensorManager?.cancelTriggerSensor(mListener, mSignificantMotion)
            txtWalk.append(getString(R.string.sig_motion_disabled) + "\n")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mSignificantMotion != null) mSensorManager?.cancelTriggerSensor(mListener, mSignificantMotion);

    }
}
