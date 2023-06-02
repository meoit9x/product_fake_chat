package nat.pink.base.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import nat.pink.base.R
import nat.pink.base.databinding.ItemLanguageBinding
import nat.pink.base.model.Language


class LanguageAdapter(
    var listLanguage: MutableList<Language>,
    var onClickItem: (Int) -> Unit,
) : RecyclerView.Adapter<LanguageAdapter.ViewHolder>() {

    inner class ViewHolder(var binding: ItemLanguageBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int = listLanguage.size
    var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val itemBinding =
            ItemLanguageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.binding
        val item = listLanguage[position]
        binding.txtFlag.text = item.language
        Glide.with(binding.ivFlag.context).load(item.flags).into(binding.ivFlag)
        binding.rb.isChecked = item.isSelected
        binding.txtFlag.setTextColor(if (item.isSelected) Color.WHITE else context!!.getColor(R.color.color_212121))
        binding.rootView.setBackgroundResource(
            if (item.isSelected) R.drawable.bg_setting_language else
                R.drawable.bg_white)
        binding.rootView.setOnClickListener {
            onClickItem.invoke(position)
        }
    }
}
