package com.binar.chapter5.fragment

import android.app.AlertDialog
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
import com.binar.chapter5.database.modelDB.User
import com.binar.chapter5.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {



    private var user_db : UserDatabase?= null
    lateinit var binding : FragmentProfileBinding
    lateinit var password : String
    lateinit var rePassword : String
    private val sharedPref = "sharedpreferences"
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        user_db = UserDatabase.getInstance(requireContext())

        val sharedPreferences : SharedPreferences = requireActivity().getSharedPreferences(sharedPref, Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = sharedPreferences.edit()


        var username = sharedPreferences.getString("username",null);
        var id = sharedPreferences.getInt("id",0)
        var name = "";

        getUser(id)

        binding.btnUpdate.setOnClickListener {
            AlertDialog.Builder(requireActivity())
                .setTitle("Update")
                .setMessage("You sure want to Update?")
                .setCancelable(true)
                .setPositiveButton("Update"){
                        dialog, _ ->
                    val username = binding.etUsername.text.toString()
                    val email = binding.etName.text.toString()
                    val user = User(
                        id,username,email,password,rePassword
                    )
                    Thread{
                        val result = user_db?.UserDao()?.updateUser(user)
                        activity?.runOnUiThread {
                            if(result!=null){
                                editor.putString("username",username)
                                editor.apply()
                                Toast.makeText(context,"Updated",Toast.LENGTH_SHORT).show()
                                findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToMainFragment())
                                }
                        }
                    }.start()
                }
                .setNegativeButton("Cancel"){
                        dialog,_ -> dialog.dismiss()
                }
                .show()

          }

        binding.btnLogout.setOnClickListener {
            AlertDialog.Builder(requireActivity())
                .setTitle("Exit")
                .setMessage("You sure want to Exit?")
                .setCancelable(true)
                .setPositiveButton("Exit"){
                        dialog, _ ->editor.putBoolean("loginPref",false)
                    editor.apply()
                    findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToLoginFragment())

                }
                .setNegativeButton("No"){
                        dialog,_ -> dialog.dismiss()
                }
                .show()
            }

    }

    private fun getUser(id : Int) {
       Thread{
           val result = user_db?.UserDao()?.getUser(id)
           activity?.runOnUiThread {
               if(result!=null){
                   binding.etUsername.setText(result.username)
                   binding.etName.setText(result.email)
                   password = result.password
                   rePassword = result.repassword
               }else{
                   binding.etUsername.setText("")
                   binding.etName.setText("")
               }
           }
       }.start()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding = FragmentProfileBinding.inflate(inflater,container,false)
        return binding.root
    }


}