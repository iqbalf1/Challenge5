package com.binar.chapter5.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController

import com.binar.chapter5.database.createDB.UserDatabase
import com.binar.chapter5.databinding.FragmentLoginBinding



class LoginFragment : Fragment() {


//    private val arguments : DetailFragmentArgs by navArgs()
    private var user_db : UserDatabase?= null
    lateinit var binding : FragmentLoginBinding
    private val sharedPref = "sharedpreferences"
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        user_db = UserDatabase.getInstance(requireContext())

        val sharedPreferences : SharedPreferences = requireActivity().getSharedPreferences(sharedPref, Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = sharedPreferences.edit()

        val toHome = sharedPreferences.getBoolean("loginPref",false)
        if (toHome == true){
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToMainFragment())
        }
        var username = "";
        var password = "";

        binding.btnRegister.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
        }

        binding.btnLogin.setOnClickListener {

            username = binding.etUsername.text.toString()
            password = binding.etPassword.text.toString()
            editor.putString("username",username)
            editor.putBoolean("loginPref",true)
            editor.apply()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Silahkan isi username dan password anda",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                Thread {
                    val result = user_db?.UserDao()?.login(username, password)
                    activity?.runOnUiThread {
                        if (result != null) {
                            Toast.makeText(
                                requireContext(),
                                "Login berhasil",
                                Toast.LENGTH_LONG
                            ).show()
                            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToMainFragment())
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Gagal Login ",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }.start()
            }
        }

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding = FragmentLoginBinding.inflate(inflater,container,false)
        return binding.root
    }


}