package com.example.datamahasiswaapps

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.datamahasiswaapps.adapter.StudentAdapter
import com.example.datamahasiswaapps.databinding.ActivityMainBinding
import com.example.datamahasiswaapps.room.Student

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: StudentViewModel
    private lateinit var adapter: StudentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(StudentViewModel::class.java)
        setupRecyclerView()

        // Observe the LiveData directly and update the adapter whenever data changes
        viewModel.allStudents.observe(this) { students ->
            adapter.updateData(students)
        }

        binding.fab.setOnClickListener {
            showStudentDialog(null)
        }

        binding.searchEditText.addTextChangedListener { text ->
            val query = text.toString()
            viewModel.searchStudentsByName(query).observe(this) { students ->
                adapter.updateData(students)
            }
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = StudentAdapter(emptyList(), ::showStudentDialog, ::deleteStudent)
        binding.recyclerView.adapter = adapter
    }

    @SuppressLint("MissingInflatedId")
    private fun showStudentDialog(student: Student?) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_student, null)
        val nameEditText = dialogView.findViewById<EditText>(R.id.nameEditText)
        val nimEditText = dialogView.findViewById<EditText>(R.id.nimEditText)
        val majorEditText = dialogView.findViewById<EditText>(R.id.majorEditText)

        val dialogTitle = if (student == null) "Add Student" else "Edit Student"
        val positiveButtonText = if (student == null) "Add" else "Update"

        AlertDialog.Builder(this)
            .setTitle(dialogTitle)
            .setView(dialogView)
            .setPositiveButton(positiveButtonText) { _, _ ->
                val name = nameEditText.text.toString()
                val nim = nimEditText.text.toString()
                val major = majorEditText.text.toString()
                if (student == null) {
                    viewModel.insert(Student(name = name, nim = nim, major = major))
                } else {
                    viewModel.update(Student(id = student?.id ?: 0, name = name, nim = nim, major = major))
                }
            }
            .setNegativeButton("Cancel", null)
            .show()

        student?.let {
            nameEditText.setText(it.name)
            nimEditText.setText(it.nim)
            majorEditText.setText(it.major)
        }
    }

    private fun deleteStudent(student: Student) {

        AlertDialog.Builder(this)
            .setTitle("Hapus Mahasiswa")
            .setMessage("Apakah kamu yakin ingin menghapus ${student.name}?")
            .setPositiveButton("Hapus") { _, _ ->
                viewModel.delete(student)
            }
            .setNegativeButton("Batal", null)
            .show()
    }

}
