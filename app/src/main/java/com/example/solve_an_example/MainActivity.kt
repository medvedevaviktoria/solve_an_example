package com.example.solve_an_example

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.solve_an_example.databinding.ActivityMainBinding
import kotlin.concurrent.fixedRateTimer
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    //переменные для подсчёта статистики
    private var correctAnswers = 0 // количество правильных ответов
    private var wrongAnswers = 0 // количество неправильных ответов
    var rightAnswer = 0 //правильный ответ примера
    var userAnswer: Int? = 0//ответ, введённый пользователем

    // Список для хранения делителей чисел
    private val savedDivisors = mutableMapOf<Int, List<Int>>()

    //переменные для вывода сообщения, если пользователь не сделал выбор
    val text = "Напишите ответ в поле ввода!" //переменная с текстом об ошибке, если пользователь не сделал выбор
    var duration = Toast.LENGTH_SHORT //длительность показа сообщения

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    //Нажатие на кнопку СТАРТ
    fun btnStartClick(view: View) {
        binding.btnCheck.isEnabled = true //кнопка ПРОВЕРКА становится доступна
        binding.btnStart.isEnabled = false //кнопка СТАРТ становится недоступна
        binding.editValue.isEnabled = true //поле ввода становится доступным
        binding.editValue.text.clear() //поле ввода очищается
        binding.root.setBackgroundColor(Color.WHITE) //фон всего экрана становится белым
        generateAnExample() //генерация примера и вывод на экран
    }

    fun btnCheckClick(view: View) {
        userAnswer = binding.editValue.text.toString().toIntOrNull()
        if (userAnswer == null)
        {
            Toast.makeText(applicationContext, text, duration).show() //сообщаем пользователю, что нужно написать ответ
            return
        }
        binding.btnCheck.isEnabled = false//кнопка ПРОВЕРКА становится недоступна
        binding.btnStart.isEnabled = true//кнопка СТАРТ становится доступна
        binding.editValue.isEnabled = false//поле ввода становится недоступным
        //проверка ответа и обновление статистики
        updateStats()
    }

    fun generateAnExample() {
        val operations = listOf("*","/","-","+") //список операций
        val operation = operations.random() //генерируем одну операцию из списка
        var firstOperand = Random.nextInt(10,100) //первый операнд
        var secondOperand = 0 //второй операнд

        //если сгенерирована операция деления, то проверяем, чтобы результат деления был целым числом
        if (operation == "/") {
            //используем сохраненные делители или вычисляем новые
            secondOperand = if (savedDivisors.containsKey(firstOperand)) {
                savedDivisors[firstOperand]!!.random()//!! - не null, то есть по заданному делимому ТОЧНО существует список делителей
            } else {
                var divisors = mutableListOf<Int>() //список всех делителей первого операнда
                for (i in 1..firstOperand ) {
                    if (firstOperand % i == 0)
                        divisors.add(i) //добавляем в список операнд, если при делении на него результат целый
                }
                savedDivisors[firstOperand] = divisors
                divisors.random()
            }
            rightAnswer = firstOperand / secondOperand // записываем правильный ответ примера в переменную rightAnswer
        }
        else {
            if (operation == "-") secondOperand = Random.nextInt(10,firstOperand)
            else secondOperand = Random.nextInt(10,100)
            rightAnswer = when (operation) {
                "*" -> firstOperand * secondOperand
                "-" -> firstOperand - secondOperand
                "+" -> firstOperand + secondOperand
                else -> 0
            }
        }
        //вывод примера на экран
        binding.txtFirstOperand.text = firstOperand.toString()
        binding.txtOperation.text = operation
        binding.txtSecondOperand.text = secondOperand.toString()
    }

    //метод обновления статистики
    fun updateStats() {
        //проверка введённого пользователем ответа
        if (userAnswer == rightAnswer) //если дан правильный ответ
        {
            binding.root.setBackgroundColor(Color.GREEN)
            correctAnswers++
        }
        else {//если дан неправильный ответ
            binding.root.setBackgroundColor(Color.RED)
            wrongAnswers++
        }
        binding.txtAllExamples.text = (wrongAnswers + correctAnswers).toString()
        binding.txtNumberRight.text = correctAnswers.toString()
        binding.txtNumberWrong.text = wrongAnswers.toString()
        binding.txtPercentageCorrectAnswers.text = "%.2f%%".format(correctAnswers.toDouble() / (wrongAnswers + correctAnswers).toDouble() * 100)
    }
}