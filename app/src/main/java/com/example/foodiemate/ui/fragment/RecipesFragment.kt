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
import com.example.foodiemate.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView

class RecipesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recipes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etIngredients = view.findViewById<EditText>(R.id.etIngredients)
        val btnGenerate = view.findViewById<MaterialButton>(R.id.btnGenerateRecipe)
        val cardResult = view.findViewById<MaterialCardView>(R.id.cardResult)
        val llEmptyState = view.findViewById<LinearLayout>(R.id.llEmptyState)
        val tvTitle = view.findViewById<TextView>(R.id.tvRecipeTitle)
        val tvContent = view.findViewById<TextView>(R.id.tvRecipeContent)

        btnGenerate.setOnClickListener {
            val ingredients = etIngredients.text.toString().trim()
            if (ingredients.isEmpty()) {
                Toast.makeText(requireContext(), "请输入至少一种食材", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Mocking AI Generation for UI demonstration
            llEmptyState.visibility = View.GONE
            cardResult.visibility = View.VISIBLE
            
            tvTitle.text = "AI 为您推荐：$ingredients 创意料理"
            tvContent.text = "【食材准备】\n$ingredients、盐、油、生抽\n\n" +
                    "【烹饪步骤】\n" +
                    "1. 将食材洗净切好备用。\n" +
                    "2. 锅中热油，放入食材翻炒均匀。\n" +
                    "3. 加入调味料，大火收汁即可出锅。"
            
            Toast.makeText(requireContext(), "生成成功！", Toast.LENGTH_SHORT).show()
        }
    }
}
