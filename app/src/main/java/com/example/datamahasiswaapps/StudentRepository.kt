package com.example.datamahasiswaapps


import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.example.datamahasiswaapps.room.Student
import com.example.datamahasiswaapps.room.StudentDao

class StudentRepository(private val studentDao: StudentDao) {

    val allStudents: LiveData<List<Student>> = studentDao.getAllStudents()

    @WorkerThread
    suspend fun insert(student: Student) {
        studentDao.insert(student)
    }

    @WorkerThread
    suspend fun update(student: Student) {
        studentDao.update(student)
    }

    @WorkerThread
    suspend fun delete(student: Student) {
        studentDao.delete(student)
    }

    fun searchStudentsByName(name: String): LiveData<List<Student>> {
        return studentDao.searchStudentsByName("%$name%")
    }
}


