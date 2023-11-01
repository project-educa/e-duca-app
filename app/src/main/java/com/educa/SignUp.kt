package com.educa

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.educa.api.model.Student
import com.educa.api.service.ApiClient
import com.educa.api.service.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import kotlinx.coroutines.*

class SignUp : AppCompatActivity() {
    private lateinit var clickCalendar: com.google.android.material.textfield.TextInputEditText
    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        apiClient = ApiClient()
        sessionManager = SessionManager(this)

        val signUpButton = findViewById<Button>(R.id.btn_cadastro)
        val teacherSignUp = findViewById<TextView>(R.id.linkText)
        val backToLogin = findViewById<Button>(R.id.btn_backToLogin)
        val loadingView = findViewById<View>(R.id.loadingView)

        teacherSignUp.setOnClickListener {
            val redirect = Intent(applicationContext, Redirect::class.java)
            startActivity(redirect)
        }

        backToLogin.setOnClickListener {
            val login = Intent(applicationContext, Login::class.java)
            startActivity(login)
        }

        signUpButton.setOnClickListener {

            val nameField = findViewById<EditText>(R.id.ipt_name)
            val name = nameField.text.toString()

            val lastNameField = findViewById<EditText>(R.id.ipt_lastName)
            val lastName = lastNameField.text.toString()

            val birthdateField = findViewById<EditText>(R.id.ipt_birthdate)
            val birthdate = birthdateField.text.toString()

            val emailField = findViewById<EditText>(R.id.ipt_email)
            val email = emailField.text.toString()

            val passwordField = findViewById<EditText>(R.id.ipt_password)
            val password = passwordField.text.toString()

            val confirmPasswordField = findViewById<EditText>(R.id.ipt_confirmPassword)
            val confirmPassword = confirmPasswordField.text.toString()

            if (name.isNotBlank() &&
                lastName.isNotBlank() &&
                birthdate.isNotBlank() &&
                email.isNotBlank() &&
                password.isNotBlank() &&
                confirmPassword.isNotBlank()
            ) {
                if (confirmPassword == password) {
                    if (birthdate.length == 10) {
                        val newStudent = Student(
                            nome = name,
                            sobrenome = lastName,
                            email = email,
                            dataNasc = updateLableBack(birthdate),
                            senha = password
                        )
                        signUp(newStudent, loadingView)

                    } else {
                        Toast.makeText(
                            baseContext,
                            "Data deve estar preenchida no formato: dd-mm-aaaa.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        baseContext,
                        "As senhas não coincidem.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    baseContext,
                    "Os campos não podem estar vazios!",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }

        clickCalendar = findViewById(R.id.ipt_birthdate)


        val myCalendar = Calendar.getInstance()

        val datePicker = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLable(myCalendar)
        }

        clickCalendar.setOnClickListener {
            //closeKeyboard()
            DatePickerDialog(
                this, datePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun closeKeyboard() {
        val view = this.currentFocus
        if (view != null) {

            val manager: InputMethodManager = getSystemService(
                Context.INPUT_METHOD_SERVICE
            ) as InputMethodManager
            manager
                .hideSoftInputFromWindow(
                    view.windowToken, 0
                )
        }
    }

    private fun updateLable(myCalendar: Calendar) {
        val myFormat = "dd-MM-yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.UK)
        clickCalendar.setText(sdf.format(myCalendar.time))
    }

    @SuppressLint("NewApi")
    private fun updateLableBack(date: String): String {
        val formatInput = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val formatOutput = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        val dataFormatter = formatOutput.format(formatInput.parse(date))

        return LocalDate.parse(dataFormatter).toString()
    }

    fun signUp(newStudent: Student, loadingView: View) {
        // Aparecer o loadingView após a validação
        loadingView.visibility = View.VISIBLE

        val coroutineScope = CoroutineScope(Dispatchers.Main)

        coroutineScope.launch {
            // Delay de 2 segundos
            delay(400)
            apiClient.getMainApiService(this@SignUp).registerStudent(newStudent)
                .enqueue(object : Callback<Student> {
                    override fun onResponse(
                        call: Call<Student>,
                        response: Response<Student>
                    ) {
                        if (response.isSuccessful) {
                            val student = response.body()
                            Log.w("newStudent", "${newStudent}")
                            Toast.makeText(
                                baseContext,
                                "Cadastro realizado com sucesso! Você será redirecionado(a) à tela de login.",
                                Toast.LENGTH_SHORT
                            ).show()

                            val login =
                                Intent(applicationContext, Login::class.java)
                            startActivity(login)

                        } else {
                            Toast.makeText(
                                baseContext,
                                "Erro ao fazer cadastro, confirme seus dados e tente novamente!",
                                Toast.LENGTH_SHORT
                            ).show()

                            Log.e(
                                "ERRO AO CRIAR NOVO ESTUDANTE",
                                "Call: ${call} Response: ${response} NewStudent: ${newStudent}"
                            )
                        }

                        // Esconder o loadingView após a validação
                        loadingView.visibility = View.GONE
                    }

                    override fun onFailure(call: Call<Student>, t: Throwable) {
                        Toast.makeText(
                            baseContext,
                            "Erro no servidor! Por favor, tente novamente mais tarde. ERRO: ${t.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                        t.printStackTrace()

                        // Esconder o loadingView após a falha
                        loadingView.visibility = View.GONE
                    }
                })
        }
    }
}
