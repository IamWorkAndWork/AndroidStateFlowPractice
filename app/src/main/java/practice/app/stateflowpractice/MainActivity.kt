package practice.app.stateflowpractice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map
import practice.app.stateflowpractice.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mainLoginButton.setOnClickListener {

            val username = binding.mainUsernameEditText.text.toString()
            val password = binding.mainPasswordEditText.text.toString()

            viewModel.login(username, password)

        }

//        //###################
//        //alternative use asLiveData()
//        //###################
//        viewModel.onLoginUiState()
//            .filter {
//                it is LoginUiState.Success
//            }
//            .map {
//                it
//            }
//            .asLiveData().observe(this@MainActivity, { state: LoginUiState ->
//            renderOnLoginUiState(state)
//        })

        lifecycleScope.launchWhenStarted {

            viewModel.onLoginUiState()
                    .collect { state: LoginUiState ->
                        renderOnLoginUiState(state)
                    }

        }

    }

    private fun renderOnLoginUiState(state: LoginUiState) {

        when (state) {
            is LoginUiState.Loading -> {

                binding.mainProgressBar.isVisible = true

            }
            is LoginUiState.InvalidInput -> {

                Snackbar.make(
                        binding.root,
                        state.message,
                        Snackbar.LENGTH_LONG
                ).show()
                binding.mainProgressBar.isVisible = false

            }
            is LoginUiState.Error -> {

                val message = state.error.localizedMessage
                Snackbar.make(
                        binding.root,
                        message,
                        Snackbar.LENGTH_LONG
                ).show()
                binding.mainProgressBar.isVisible = false

            }
            is LoginUiState.Success -> {

                Snackbar.make(
                        binding.root,
                        "Successfully logged in",
                        Snackbar.LENGTH_LONG
                ).show()
                binding.mainProgressBar.isVisible = false

            }
            else -> Unit
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}