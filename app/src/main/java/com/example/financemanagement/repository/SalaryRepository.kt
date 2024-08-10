package com.example.financemanagement.repository


import com.example.financemanagement.services.FirebaseService
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.tasks.await

object SalaryRepository {

    private val database by lazy { FirebaseService.firebaseDatabase }

    suspend fun addRemainingSalary(salary: Int) {
        val user = FirebaseService.user
        if (user != null) {
            val uid = user.uid
            try {
                database.getReference("Users/$uid/Remaining Salary")
                    .setValue(salary)
                    .await()
            } catch (e: Exception) {
                throw e
            }
        } else {
            throw IllegalStateException("User is not authenticated.")
        }
    }

    suspend fun addSalaryDate(salary: Int,date: Long) {
        val user = FirebaseService.user
        if (user != null) {
            val uid = user.uid
            try {
                database.getReference("Users/$uid/Salary and Date/Salary")
                    .setValue(salary)
                    .await()

                database.getReference("Users/$uid/Salary and Date/Date")
                    .setValue(date)
                    .await()
            } catch (e: Exception) {
                throw e
            }
        } else {
            throw IllegalStateException("User is not authenticated.")
        }
    }

    fun getSalary(onChange: (Int) -> Unit, onFailure: (Exception) -> Unit){
        val user = FirebaseService.user
        if (user != null) {
            val uid = user.uid
            database.getReference("Users/$uid/Remaining Salary")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val salary = snapshot.getValue(Int::class.java)
                        onChange(salary ?: 0)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        onFailure(Exception("Failed to get salary"))
                    }
                })
        }
    }

    suspend fun getSalaryDate(): Long {
        val user = FirebaseService.user
        if (user != null) {
            val uid = user.uid
            try {
                val snapshot = database.getReference("Users/$uid/Salary and Date")
                    .get().
                    await()

                val dateMillis = snapshot.child("Date").getValue(Long::class.java) ?: 0L
                return dateMillis

            } catch (e: Exception) {
                throw e
            }
        } else {
            throw IllegalStateException("User is not authenticated.")
        }
    }

    //This function is used to get the salary that is stored in the "Salary and Date" from the DB
    suspend fun getConstantSalary(): Int {
        val user = FirebaseService.user
        if (user != null) {
            val uid = user.uid
            try {
                val snapshot = database.getReference("Users/$uid/Salary and Date")
                    .get().
                    await()

                val constSalary = snapshot.child("Salary").getValue(Int::class.java) ?: 0
                return constSalary

            } catch (e: Exception) {
                throw e
            }
        } else {
            throw IllegalStateException("User is not authenticated.")
        }
    }


    suspend fun updateSalaryDateAndRemainingSalary(newDate: Long, newAmount: Int) {
        val user = FirebaseService.user
        if (user != null) {
            val uid = user.uid
            try {
                database.getReference("Users/$uid/Salary and Date/Date")
                    .setValue(newDate)
                    .await()
                database.getReference("Users/$uid/Remaining Salary")
                    .setValue(newAmount)
                    .await()
            } catch (e: Exception) {
                throw e
            }
        } else {
            throw IllegalStateException("User is not authenticated.")
        }
    }
}
