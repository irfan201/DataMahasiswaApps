package com.example.datamahasiswaapps.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.datamahasiswaapps.room.Student
import com.example.datamahasiswaapps.databinding.ItemStudentBinding

class StudentAdapter(
    private var students: List<Student>,
    private val onEdit: (Student) -> Unit,
    private val onDelete: (Student) -> Unit
) : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val binding = ItemStudentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StudentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        holder.bind(students[position])
    }

    override fun getItemCount(): Int = students.size

    fun updateData(newStudents: List<Student>) {
        students = newStudents
        notifyDataSetChanged()
    }

    inner class StudentViewHolder(private val binding: ItemStudentBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val student = students[adapterPosition]
                onEdit(student)
            }

            binding.deleteButton.setOnClickListener {
                val student = students[adapterPosition]
                onDelete(student)
            }
        }

        fun bind(student: Student) {
            binding.nameTextView.text = student.name
            binding.nimTextView.text = student.nim
            binding.majorTextView.text = student.major
        }
    }
}
