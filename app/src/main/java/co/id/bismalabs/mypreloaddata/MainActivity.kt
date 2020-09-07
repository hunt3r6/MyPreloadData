package co.id.bismalabs.mypreloaddata

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import co.id.bismalabs.mypreloaddata.services.DataManagerService
import co.id.bismalabs.mypreloaddata.services.DataManagerService.Companion.CANCEL_MESSAGE
import co.id.bismalabs.mypreloaddata.services.DataManagerService.Companion.FAILED_MESSAGE
import co.id.bismalabs.mypreloaddata.services.DataManagerService.Companion.PREPARATION_MESSAGE
import co.id.bismalabs.mypreloaddata.services.DataManagerService.Companion.SUCCESS_MESSAGE
import co.id.bismalabs.mypreloaddata.services.DataManagerService.Companion.UPDATE_MESSAGE
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.ref.WeakReference

class MainActivity : AppCompatActivity(), HandlerCallback {
    private lateinit var mBoundService: Messenger
    private var mServiceBound: Boolean = false

    private val mServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            mBoundService = Messenger(service)
            mServiceBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            mServiceBound = false
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mBoundServiceIntent = Intent(this@MainActivity, DataManagerService::class.java)
        val mActivityMessenger = Messenger(InComingHandler(this))
        mBoundServiceIntent.putExtra(DataManagerService.ACTIVITY_HANDLER, mActivityMessenger)
        bindService(mBoundServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(mServiceConnection)
    }

    override fun onPreparation() {
        Toast.makeText(this, "MEMULAI MEMUAT DATA", Toast.LENGTH_SHORT).show()
    }

    override fun updateProgress(progress: Long) {
        Log.d("PROGRESS", "updateProgress: $progress ")
        progress_bar.progress = progress.toInt()
    }

    override fun loadSuccess() {
        Toast.makeText(this, "BERHASIl", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this@MainActivity, MahasiswaActivity::class.java))
        finish()
    }

    override fun loadFailed() {
        Toast.makeText(this, "GAGAL", Toast.LENGTH_SHORT).show()
    }

    override fun loadCancel() {
        finish()
    }

    private class InComingHandler(callback: HandlerCallback) : Handler() {
        private var weakCallback: WeakReference<HandlerCallback> = WeakReference(callback)

        override fun handleMessage(msg: Message) {
            when (msg.what) {
                PREPARATION_MESSAGE -> weakCallback.get()?.onPreparation()
                UPDATE_MESSAGE -> {
                    val bundle = msg.data
                    val progress = bundle.getLong("KEY_PROGRESS")
                    weakCallback.get()?.updateProgress(progress)
                }
                SUCCESS_MESSAGE -> weakCallback.get()?.loadSuccess()
                FAILED_MESSAGE -> weakCallback.get()?.loadFailed()
                CANCEL_MESSAGE -> weakCallback.get()?.loadCancel()
            }
        }
    }
}

private interface HandlerCallback {
    fun onPreparation()
    fun updateProgress(progress: Long)
    fun loadSuccess()
    fun loadFailed()
    fun loadCancel()
}