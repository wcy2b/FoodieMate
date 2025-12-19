package com.example.foodiemate.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.foodiemate.R
import com.example.foodiemate.data.api.AiRetrofitClient
import com.example.foodiemate.data.api.ChatMessage
import com.example.foodiemate.data.api.ChatRequest
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import io.noties.markwon.Markwon
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class RecipesFragment : Fragment() {

    private val apiKey = "Bearer sk-zesbrofdzjofqijaghtnzwwpwvoyasiopkcgwquogcmfxacy"
    private lateinit var markwon: Markwon
    private var loadingJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recipes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        markwon = Markwon.create(requireContext())

        val etIngredients = view.findViewById<EditText>(R.id.etIngredients)
        val btnGenerate = view.findViewById<MaterialButton>(R.id.btnGenerateRecipe)
        val cardResult = view.findViewById<MaterialCardView>(R.id.cardResult)
        val llEmptyState = view.findViewById<LinearLayout>(R.id.llEmptyState)
        val tvTitle = view.findViewById<TextView>(R.id.tvRecipeTitle)
        val tvContent = view.findViewById<TextView>(R.id.tvRecipeContent)
        val llLoading = view.findViewById<LinearLayout>(R.id.llLoading)
        val tvLoadingStatus = view.findViewById<TextView>(R.id.tvLoadingStatus)

        val loadingMessages = listOf(
            "AI 厨师正在备菜...",
            "正在挑选新鲜食材...",
            "火候调整中，请稍候...",
            "大厨正在构思创意料理...",
            "摆盘中，美味即将呈现...",
            "正在为您润色菜谱细节..."
        )

        btnGenerate.setOnClickListener {
            val ingredients = etIngredients.text.toString().trim()
            if (ingredients.isEmpty()) {
                Toast.makeText(requireContext(), "请输入至少一种食材", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // UI feedback
            llLoading.visibility = View.VISIBLE
            llEmptyState.visibility = View.GONE
            cardResult.visibility = View.GONE
            btnGenerate.isEnabled = false

            // Start loading status rotation
            loadingJob = lifecycleScope.launch {
                var index = 0
                while (isActive) {
                    tvLoadingStatus.text = loadingMessages[index % loadingMessages.size]
                    index++
                    delay(2000)
                }
            }

            // Call DeepSeek API
            lifecycleScope.launch {
                try {
                    val request = ChatRequest(
                        model = "deepseek-ai/DeepSeek-V3",
                        messages = listOf(
                            ChatMessage(role = "system", content = "你是一个专业的厨师。请根据用户提供的食材，生成一个包含【食材准备】和【烹饪步骤】的详细菜谱。请使用 Markdown 格式输出，使其包含标题、粗体和列表。"),
                            ChatMessage(role = "user", content = "食材：$ingredients")
                        )
                    )

                    val response = AiRetrofitClient.instance.getChatCompletions(apiKey, request)
                    
                    if (response.choices.isNotEmpty()) {
                        val aiContent = response.choices[0].message.content
                        tvTitle.text = "AI 为您推荐：$ingredients 创意料理"
                        
                        // Render with Markwon
                        markwon.setMarkdown(tvContent, aiContent)
                        
                        cardResult.visibility = View.VISIBLE
                        llLoading.visibility = View.GONE
                        Toast.makeText(requireContext(), "生成成功！", Toast.LENGTH_SHORT).show()
                    } else {
                        throw Exception("API 返回空结果")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    llLoading.visibility = View.GONE
                    llEmptyState.visibility = View.VISIBLE
                    Toast.makeText(requireContext(), "生成失败: ${e.message}", Toast.LENGTH_LONG).show()
                } finally {
                    loadingJob?.cancel()
                    btnGenerate.isEnabled = true
                }
            }
        }
    }
}
