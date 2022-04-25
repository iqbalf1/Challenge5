package com.binar.chapter5.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.binar.chapter5.R
import com.binar.chapter5.adapter.MainAdapter
import com.binar.chapter5.database.createDB.UserDatabase
import com.binar.chapter5.databinding.FragmentLoginBinding
import com.binar.chapter5.databinding.FragmentMainBinding
import com.binar.chapter5.view_model.SecondViewModel


class MainFragment : Fragment() {


    private val sharedPref = "sharedpreferences"
    private val secondViewModel: SecondViewModel by viewModels()
    lateinit var binding: FragmentMainBinding
    lateinit var username: String
    private val mId = MutableLiveData<Int>()


    private var user_db: UserDatabase? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        user_db = UserDatabase.getInstance(requireContext())
        mId.postValue(68726)

        val sharedPreferences: SharedPreferences =
            requireActivity().getSharedPreferences(sharedPref, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()

        username = sharedPreferences.getString("username", "null").toString()
        binding.tvUsername.setText("Welcome, $username")
        setupSecondObserver()
        getIdUsername()
        binding.ivProfile.setOnClickListener {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToProfileFragment())
        }

    }

    private fun getIdUsername() {
        val sharedPreferences: SharedPreferences =
            requireActivity().getSharedPreferences(sharedPref, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        Thread {
            var result = user_db?.UserDao()?.getId(username)
            activity?.runOnUiThread {
                if (result != null) {
                    var id = result

                    editor.putInt("id", id)
                    editor.apply()
                    Log.d("ID", id.toString())
                } else {
                    Log.d("ID", "Null Id")
                }
            }
        }.start()
    }
    private fun setupSecondObserver() {
        Log.d("Tag","Fragment activity : datanya ->")
        secondViewModel.getMovies().observe(requireActivity()){
            binding.progressBar.visibility = View.VISIBLE
            Log.d("Tag","Fragment activity : datanya -> $it")
            val adapter = MainAdapter(it)
            val layoutManager = LinearLayoutManager(requireContext(),
                LinearLayoutManager.VERTICAL,false)
            binding.rvMain.layoutManager = layoutManager
            binding.rvMain.adapter = adapter
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMainBinding.inflate(inflater,container,false)
        return binding.root
    }

}




