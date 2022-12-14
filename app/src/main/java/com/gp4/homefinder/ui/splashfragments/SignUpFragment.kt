package com.gp4.homefinder.ui.splashfragments

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.gp4.homefinder.ui.activity.MainActivity
import com.gp4.homefinder.ui.activity.SplashScreenActivity
import com.gp4.homefinder.data.DataSource
import com.gp4.homefinder.data.repos.UserRepository
import com.gp4.homefinder.databinding.FragmentSignUpBinding

class SignUpFragment : Fragment() {

    //TAG
    private val TAG = this.javaClass.canonicalName

    //bind
    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    //local data
    private lateinit var dataSource: DataSource
    private lateinit var prefs: SharedPreferences
    private val packageName:String = "com.gp4.homefinder"

    //nav con
    private lateinit var navController: NavController
    private lateinit var auth: FirebaseAuth

    //db
    private lateinit var userRepository: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefs = (activity as SplashScreenActivity).getSharedPreferences(packageName, AppCompatActivity.MODE_PRIVATE)
        auth = Firebase.auth
        userRepository = UserRepository()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnCreateAccount.setOnClickListener {
            validateData()
        }
    }

    private fun validateData() {
        var validData = true
        var email = ""
        var password = ""

        if (binding.editEmail.getText().toString().isEmpty()) {
            binding.editEmail.setError("Email Cannot be Empty")
            validData = false
        } else {
            email = binding.editEmail.getText().toString()
        }

        if (binding.editPassword.getText().toString().isEmpty()) {
            binding.editPassword.setError("Password Cannot be Empty")
            validData = false
        } else {
            if (binding.editConfirmPassword.getText().toString().isEmpty()) {
                binding.editConfirmPassword.setError("Confirm Password Cannot be Empty")
                validData = false
            } else {
                if (!binding.editPassword.getText().toString()
                        .equals(binding.editConfirmPassword.getText().toString())
                ) {
                    binding.editConfirmPassword.setError("Both passwords must be same")
                    validData = false
                } else {
                    password = binding.editPassword.getText().toString()
                }
            }
        }

        if (validData) {
            createAccount(email, password)
        } else {
            Toast.makeText(activity as SplashScreenActivity, "Please provide correct inputs", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createAccount(email: String, password: String) {
        try{
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{ task ->
                if(task.isSuccessful){
                    Log.d(TAG, "CreateUserWithEmail:success")
                    val authuser = auth.currentUser
                    saveToPrefs(email, password)
                    if (authuser != null) {
                        userRepository.addUserToDb((activity as SplashScreenActivity), authuser)
                    }
                    goToMain()
                    (activity as SplashScreenActivity).finish()
                    //TODO reload the page / redirect
                }else{
                    Log.w(TAG, "CreateUserWithEmail:failure", task.exception)
                    Toast.makeText(activity as SplashScreenActivity, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
        }catch (err: com.google.firebase.auth.FirebaseAuthUserCollisionException){
            Toast.makeText(activity as SplashScreenActivity,
                "The email address is already in use by another account",
                Toast.LENGTH_SHORT).show()
        }catch (err: com.google.firebase.auth.FirebaseAuthWeakPasswordException){
            Toast.makeText(activity as SplashScreenActivity,
                "Password too weak",
                Toast.LENGTH_SHORT).show()
        }


    }

    private fun saveToPrefs(email: String, password: String) {
        val prefs = (activity as SplashScreenActivity).getSharedPreferences(packageName,
            AppCompatActivity.MODE_PRIVATE
        )
        prefs.edit().putString("USER_EMAIL", email).apply()
        prefs.edit().putString("USER_PASSWORD", password).apply()
    }

    private fun goToMain() {
        val mainIntent = Intent((activity as SplashScreenActivity), MainActivity::class.java)
        startActivity(mainIntent)
    }

}