package br.edu.ifsp.aluno.vander.gabriel.conceitoservice.services

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder

class LifetimeBoundService : Service() {
    var lifetime: Int = 0
        private set

    private val lifetimeBoundServiceBinder = LifetimeBoundServiceBinder()

    private lateinit var workerThread: WorkerThread

    override fun onCreate() {
        super.onCreate()
        workerThread = WorkerThread()
    }

    override fun onDestroy() {
        super.onDestroy()
        workerThread.running = false
    }

    override fun onBind(intent: Intent?): IBinder {
        if (!workerThread.running) {
            workerThread.start()
        }
        return lifetimeBoundServiceBinder
    }

    inner class LifetimeBoundServiceBinder : Binder() {
        fun getService() = this@LifetimeBoundService
    }

    private inner class WorkerThread : Thread() {
        var running = false
        override fun run() {
            running = true
            while (running) {
                sleep(1000)
                lifetime++
            }

        }
    }

}
