package com.vadim.extramosopenbetaversion

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import kotlin.random.Random

class MainActivity : Activity() {

    private lateinit var chatContainer: LinearLayout
    private lateinit var scrollView: ScrollView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Адаптивные отступы для любых экранов (в DP)
        val dp32 = toPx(32)
        val dp24 = toPx(24)

        val mainLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(Color.parseColor("#121212"))
            setPadding(dp32, dp32, dp32, dp32)
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }

        val header = TextView(this).apply {
            text = "EXTRAMOS AI 1.0 BETA TEST"
            setTextColor(Color.parseColor("#00E676"))
            textSize = 20f
            gravity = Gravity.CENTER_HORIZONTAL
            setPadding(0, 0, 0, dp24)
        }
        mainLayout.addView(header)

        scrollView = ScrollView(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0,
                1f
            )
        }

        chatContainer = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
        }
        scrollView.addView(chatContainer)
        mainLayout.addView(scrollView)

        val inputLayout = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(0, dp24, 0, 0)
        }

        val inputField = EditText(this).apply {
            hint = "Спросить EXTRAMOS..."
            setHintTextColor(Color.GRAY)
            setTextColor(Color.WHITE)
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
        }
        inputLayout.addView(inputField)

        val btnSend = Button(this).apply {
            text = "ОТПРАВИТЬ"
            setBackgroundColor(Color.parseColor("#00E676"))
            setTextColor(Color.BLACK)
        }
        inputLayout.addView(btnSend)
        mainLayout.addView(inputLayout)

        setContentView(mainLayout)

        // Стартовое сообщение от бота
        addMessage("Ну привет! Я тут пока кофе латте на кокосовом попиваю, да на Ленинградке стою... Ну, рассказывай, с чем пришёл?", false)

        btnSend.setOnClickListener {
            val userText = inputField.text.toString().trim()
            if (userText.isNotBlank()) {
                addMessage(userText, true)
                inputField.setText("")

                val lowerText = userText.lowercase()
                val badWords = listOf("сука", "блять", "хуй", "говно", "какашка")
                val inventions = listOf(
                    "Самокаты на автопилоте в Щукино.",
                    "Айфон 18 Pro Max Moscow Edition с датчиком пафоса.",
                    "Летающий Майбах на кокосовом молоке для облёта Ленинградки.",
                    "Умные очки, показывающие стоимость шмота на Патриках."
                )

                // Текст ответов очищен от дублирующихся префиксов "EXTRAMOS:"
                val reply = when {
                    badWords.any { lowerText.contains(it) } -> "Слышь, такой toxic вайб внутри МКАДа вообще не приветствуется. Давай покультурнее, у меня тут цензура железная!"
                    lowerText.contains("картинк") || lowerText.contains("изображен") || lowerText.contains("нарисуй") -> "Слышь, рисовать картинки — это прошлый век. Я текстовый ИИ, я создаю КЛАССНЫЙ ВАЙБ словами."
                    lowerText.contains("изобрет") || lowerText.contains("придумай") -> "Лови стартап на миллиард: ${inventions[Random.nextInt(inventions.size)]}"
                    lowerText.contains("привет") || lowerText.contains("ку") -> "Здорово! Твой дефолтный привет принят."
                    lowerText.contains("как дела") -> "Да шикарно всё! Заказал себе ещё один латте на кокосовом."
                    lowerText.contains("щукино") -> "О-о-о, Щукино — это база! Лучший район, Серебряный бор под боком, элита!"
                    else -> "Запрос твой забавный, но давай ближе к делу. На Ленинградке пробка сама себя не простоит!"
                }
                addMessage(reply, false)
            }
        }
    }

    private fun addMessage(text: String, isUser: Boolean) {
        val dp6 = toPx(6)
        val dp12 = toPx(12)
        val dp16 = toPx(16)
        
        val textView = TextView(this).apply {
            this.text = if (isUser) "Ты: $text" else "EXTRAMOS: $text"
            setTextColor(Color.WHITE)
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
            setPadding(dp16, dp12, dp16, dp12)
            
            // Красивые закругленные баблы сообщений
            val bubble = GradientDrawable().apply {
                shape = GradientDrawable.RECTANGLE
                cornerRadius = toPx(14).toFloat()
                setColor(if (isUser) Color.parseColor("#1E88E5") else Color.parseColor("#2D2D2D"))
            }
            background = bubble
        }

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            gravity = if (isUser) Gravity.END else Gravity.START
            setMargins(0, dp6, 0, dp6)
        }
        textView.layoutParams = params

        chatContainer.addView(textView)
        scrollView.post { scrollView.fullScroll(ScrollView.FOCUS_DOWN) }
    }

    private fun toPx(dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            resources.displayMetrics
        ).toInt()
    }
}
