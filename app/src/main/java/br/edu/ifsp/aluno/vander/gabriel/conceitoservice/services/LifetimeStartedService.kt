package br.edu.ifsp.aluno.vander.gabriel.conceitoservice.services

import android.app.Service
import android.content.Intent
import android.os.IBinder

class LifetimeStartedService : Service() {
    private var lifetimeInSeconds: Int = 0
    private lateinit var workerThread: WorkerThread

    companion object {
        const val LIFETIME_EXTRA = "LIFETIME_EXTRA"
        const val ACTION_RECEIVE_LIFETIME = "ACTION_RECEIVE_LIFETIME"
    }

    override fun onCreate() {
        super.onCreate()
        workerThread = WorkerThread()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (!workerThread.running) {
            workerThread.start()
        }

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        workerThread.running = false
    }

    override fun onBind(intent: Intent): IBinder? = null

    private inner class WorkerThread : Thread() {
        var running = false

        override fun run() {
            running = true
            while (running) {
                sleep(1000)

                sendBroadcast(
                    Intent(ACTION_RECEIVE_LIFETIME).also {
                        it.putExtra(LIFETIME_EXTRA, ++lifetimeInSeconds)
                    }
                )
            }
        }
    }
}