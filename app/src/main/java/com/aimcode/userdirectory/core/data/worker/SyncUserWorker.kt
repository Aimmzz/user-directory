package com.aimcode.userdirectory.core.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.aimcode.userdirectory.core.data.source.local.dao.PendingUserDao
import com.aimcode.userdirectory.core.data.source.remote.UserService
import com.aimcode.userdirectory.core.model.request.UserRequest
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import timber.log.Timber

@HiltWorker
class SyncUserWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val userService: UserService,
    private val pendingUserDao: PendingUserDao,
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val pending = pendingUserDao.getAll()

            pending.forEach { entity ->
                userService.addUser(
                    UserRequest(
                        name = entity.name,
                        address = entity.address,
                        email = entity.email,
                        phoneNumber = entity.phoneNumber,
                        city = entity.city,
                        gender = entity.gender,
                    )
                )
                pendingUserDao.deleteById(entity.id)
            }

            Result.success()
        } catch (e: Exception) {
            Timber.e(e)
            Result.retry()
        }
    }

    companion object {
        const val WORK_NAME = "sync_user_worker"
    }
}