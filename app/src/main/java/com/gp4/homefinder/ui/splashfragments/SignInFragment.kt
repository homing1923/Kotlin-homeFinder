package com.gp4.homefinder.ui.splashfragments

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.gp4.homefinder.R
import com.gp4.homefinder.ui.activity.SplashScreenActivity
import com.gp4.homefinder.data.DataSource
import com.gp4.homefinder.data.repos.UserRepository

import com.gp4.homefinder.databinding.FragmentSignInBinding

class SignInFragment : Fragment() {

    //TAG
    private val TAG = this.javaClass.canonicalName

    //bind
    private var _binding: FragmentSignInBinding? = null
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
        FirebaseApp.initializeApp(activity as SplashScreenActivity)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataSource = DataSource.getInstance()
        userRepository = UserRepository()

        if (prefs.contains("USER_EMAIL")) {
            binding.signinEditEmail.setText(this.prefs.getString("USER_EMAIL", ""))
        }
        if (prefs.contains("USER_PASSWORD")) {
            binding.signinEditPassword.setText(this.prefs.getString("USER_PASSWORD", ""))
        }

        val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.container1) as NavHostFragment
        this.navController = navHostFragment.navController
        attachBtnFunc()
    }

    private fun attachBtnFunc() {
        binding.signinSignInBtn.setOnClickListener {
            validateData()
        }
    }

    private fun validateData() {
        var validData = true
        var email = ""
        var password = ""
        if (binding.signinEditEmail.text.toString().isEmpty()) {
            binding.signinEditEmail.error = "Email Cannot be Empty"
            validData = false
        } else {
            email = binding.signinEditEmail.text.toString()
        }
        if (binding.signinEditPassword.text.toString().isEmpty()) {
            binding.signinEditPassword.error = "Password Cannot be Empty"
            validData = false
        } else {
            password = binding.signinEditPassword.text.toString()
        }
        if (validData) {
            signIn(email, password)
        } else {
            Toast.makeText(activity as SplashScreenActivity, "Please provide correct inputs", Toast.LENGTH_SHORT).show()
        }
    }

    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener (activity as SplashScreenActivity){ task ->
            if(task.isSuccessful){
                Log.d(TAG, "SignInUserWithEmail:success")
                val user = auth.currentUser
                if (user != null) {
                    saveToPrefs(email, password, user.uid)
                }
                userRepository.getUserData(activity as SplashScreenActivity)
                checkPrefCampus()
                val action = SignInFragmentDirections.actionSignInFragmentToMainActivity()
                navController.navigate(action)
            }else{
                Log.w(TAG, "SignInUserWithEmail:failure", task.exception)
                Toast.makeText(activity as SplashScreenActivity, "Authentication failed.",
                    Toast.LENGTH_SHORT).show()
            }
        }
        //signIn using FirebaseAuth
    }

    private fun checkPrefCampus() {
        if(prefs.contains("PREF_CAMPUS")){
            dataSource.currentCampusId = prefs.getInt("PREF_CAMPUS", -1)
            if(dataSource.currentCampusId == -1){
                dataSource.currentCampusId = null
            }else{
                dataSource.currentCampus = dataSource.listOfCampus[dataSource.currentCampusId!!]
            }
        }
    }

    private fun saveToPrefs(email: String, password: String, id:String) {
        if (binding.signinSwtRemember.isChecked) {
            prefs.edit().putString("USER_EMAIL", email).apply()
            prefs.edit().putString("USER_PASSWORD", password).apply()
            prefs.edit().putString("USER_ID", id).apply()
        } else {
            if (prefs.contains("USER_EMAIL")) {
                prefs.edit().remove("USER_EMAIL").apply()
            }
            if (prefs.contains("USER_PASSWORD")) {
                prefs.edit().remove("USER_PASSWORD").apply()
            }
            if (prefs.contains("USER_ID")) {
                prefs.edit().remove("USER_ID").apply()
            }
        }
    }

}