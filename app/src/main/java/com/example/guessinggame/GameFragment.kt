package com.example.guessinggame

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.findNavController
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import androidx.compose.material3.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Button
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.material3.TextField
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp


class GameFragment : Fragment() {

    lateinit var viewModel: GameViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(this).get(GameViewModel::class.java)

        viewModel.gameOver.observe(viewLifecycleOwner, Observer{ newValue ->
            if(newValue) {
                val action = GameFragmentDirections
                    .actionGameFragmentToResultFragment(viewModel.wonLostMessage())
                view?.findNavController()?.navigate(action)
            }
        })
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme {
                    Surface {
                        GameFragmentContent(viewModel)
                    }
                }
            }
        }
    }



}



@Composable
fun FinishGameButton(clicked: () -> Unit)
{
    Button(onClick = clicked) {
        Text("Конец Игры")
    }
}

@Composable
fun EnterGuess(guess: String, changed: (String) -> Unit) {
    TextField(
        value = guess,
        label = { Text("Угадай букву") },
        onValueChange = changed
    )
}

@Composable
fun GuessButton(clicked: () -> Unit) {
    Button(onClick = clicked) {
        Text("Угадать!")
    }
}

@Composable
fun IncorrectGuessesText(viewModel: GameViewModel) {
    val incorrectGuesses = viewModel.incorrectGuesses.observeAsState()
    incorrectGuesses.value?.let{
        Text(stringResource(R.string.incorrect_guesses, it))
    }
}

@Composable
fun LivesLeftText(viewModel: GameViewModel) {
    val livesLeft = viewModel.livesLeft.observeAsState()
    livesLeft.value?.let {
        Text(stringResource(R.string.lives_left, it))
    }
}

@Composable
fun SecretWordDisplay(viewModel: GameViewModel) {
    val display = viewModel.secretWordDisplay.observeAsState()
    display.value?.let {
        Text(
            text = it,
            letterSpacing = 0.1.em,
            fontSize = 36.sp
        )
    }
}

@Composable
fun GameFragmentContent(viewModel: GameViewModel) {
    val guess = remember {
        mutableStateOf("")
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center) {
            SecretWordDisplay(viewModel = viewModel)
        }
        LivesLeftText(viewModel = viewModel)
        IncorrectGuessesText(viewModel =  viewModel)
        EnterGuess(guess = guess.value) {
            guess.value = it
        }
    }

    Column(modifier = Modifier.padding(135.dp).fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally) {
        GuessButton {
            viewModel.makeGuess(guess.value.uppercase())
            guess.value = ""
        }
        FinishGameButton {
            viewModel.finishGame()
        }
    }
}