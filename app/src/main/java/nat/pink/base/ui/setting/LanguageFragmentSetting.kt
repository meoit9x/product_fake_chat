package nat.pink.base.ui.setting

import android.content.Intent
import android.os.Handler
import android.os.Looper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.gun0912.tedpermission.provider.TedPermissionProvider
import nat.pink.base.MainActivity
import nat.pink.base.R
import nat.pink.base.adapter.LanguageAdapter
import nat.pink.base.base.BaseFragment
import nat.pink.base.base.EmptyViewModel
import nat.pink.base.databinding.FragmentLanguageSettingBinding
import nat.pink.base.model.Language
import nat.pink.base.ui.splah.SplashActivity
import nat.pink.base.utils.PreferenceUtil


class LanguageFragmentSetting : BaseFragment<FragmentLanguageSettingBinding, EmptyViewModel>() {

    private var listLanguage = mutableListOf<Language>()

    private var adapterLanguage: LanguageAdapter? = null

    private lateinit var currentLanguageSelected: String
    private lateinit var english: String

    override fun initData() {
        super.initData()
        binding.llTop.txtTitle.text = getString(R.string.language)
        PreferenceUtil.getCurrentLanguage(requireContext())?.let {
            currentLanguageSelected = it
        } ?: kotlin.run {
            currentLanguageSelected ="en"
        }


        english = PreferenceUtil.getString(context, PreferenceUtil.KEY_CURRENT_LANGUAGE, "")
        listLanguage.apply {
            add(
                Language(
                    getString(R.string.txt_language_en),
                    english.equals("en"),
                    "en",
                    R.drawable.flag_en
                )
            )
            add(
                Language(
                    getString(R.string.txt_language_chi),
                    english.equals("za"),
                    "za",
                    R.drawable.flag_cn
                )
            )
            add(
                Language(
                    getString(R.string.txt_language_ja),
                    english.equals("ja"),
                    "ja",
                    R.drawable.flag_jp
                )
            )
            add(
                Language(
                    getString(R.string.txt_language_vi),
                    english.equals("vi"),
                    "vi",
                    R.drawable.flag_vi
                )
            )
            add(
                Language(
                    getString(R.string.txt_language_spanish),
                    english.equals("es"),
                    "es",
                    R.drawable.flag_sp
                )
            )
            add(
                Language(
                    getString(R.string.txt_language_portuguese),
                    english.equals("pt"),
                    "pt",
                    R.drawable.flag_ft
                )
            )
            add(
                Language(
                    getString(R.string.txt_language_russiane),
                    english.equals("ru"),
                    "ru",
                    R.drawable.flag_rs
                )
            )
            add(
                Language(
                    getString(R.string.txt_language_korean),
                    english.equals("ko"),
                    "ko",
                    R.drawable.flag_kr
                )
            )
            add(
                Language(
                    getString(R.string.txt_language_french),
                    english.equals("fr"),
                    "fr",
                    R.drawable.flag_fr
                )
            )
            add(
                Language(
                    getString(R.string.txt_language_german),
                    english.equals("de"),
                    "de",
                    R.drawable.flag_gr
                )
            )
        }
        PreferenceUtil.saveCurrentLanguage(requireContext(), currentLanguageSelected)
    }

    override fun initView() {
        super.initView()
        setupRecycleView()
    }

    override fun getViewModel(): EmptyViewModel {
        TODO("Not yet implemented")
    }

    override fun initEvent() {
        super.initEvent()
        for (i in 0 until listLanguage.size) {
            val language = listLanguage[i]
            if (language.value.equals(english)) {
                binding.rcvEnglish.scrollToPosition(i)
            }
        }
        binding.llTop.ivBack.setOnClickListener {
            backStackFragment()
        }
        binding.txtDone.setOnClickListener {
            PreferenceUtil.saveString(
                requireContext(),
                PreferenceUtil.KEY_CURRENT_LANGUAGE,
                currentLanguageSelected
            )
            PreferenceUtil.saveCurrentLanguage(requireContext(), currentLanguageSelected)
            Handler(Looper.getMainLooper()).postDelayed({
                if (isAdded) {
                    var intent =Intent(
                        requireActivity(),
                        SplashActivity::class.java
                    )
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                    requireActivity().startActivity(
                        intent
                    )
                    requireActivity().finish()
                }
            }, 500)
        }
    }

    private fun setupRecycleView() {
        adapterLanguage = LanguageAdapter(listLanguage, ::onLanguageSelected)
        binding.rcvEnglish.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = adapterLanguage
        }
        binding.llView.setOnClickListener { }
    }

    private fun onLanguageSelected(position: Int) {
        listLanguage.forEachIndexed { index, language ->
            if (language.isSelected) {
                language.isSelected = false
                adapterLanguage?.notifyItemChanged(index)
            }
        }

        listLanguage[position].isSelected = true
        currentLanguageSelected = listLanguage[position].value
        adapterLanguage?.notifyItemChanged(position)
        binding.rcvEnglish.scrollToPosition(position)
    }

    companion object {
        const val TAG: String = "LanguageFragmentSetting"
    }

}