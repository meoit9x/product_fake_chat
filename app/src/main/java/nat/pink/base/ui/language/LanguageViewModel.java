package nat.pink.base.ui.language;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

import nat.pink.base.R;
import nat.pink.base.base.BaseViewModel;
import nat.pink.base.model.ObjectLanguage;

public class LanguageViewModel extends BaseViewModel {

    MutableLiveData<ArrayList<ObjectLanguage>> languages = new MutableLiveData<>();

    public void initData(Context context) {
        ArrayList<ObjectLanguage> objectLanguages = new ArrayList<>();
        ObjectLanguage language = new ObjectLanguage(context.getString(R.string.flag_name_english), "en", R.drawable.flag_en);
        objectLanguages.add(language);
        language = new ObjectLanguage(context.getString(R.string.flag_name_japanese), "ja", R.drawable.flag_jp);
        objectLanguages.add(language);
        language = new ObjectLanguage(context.getString(R.string.flag_name_chiness), "za", R.drawable.flag_cn);
        objectLanguages.add(language);
        language = new ObjectLanguage(context.getString(R.string.flag_name_indonesian), "in", R.drawable.flag_in);
        objectLanguages.add(language);
        language = new ObjectLanguage(context.getString(R.string.flag_name_rusian), "ru", R.drawable.flag_ru);
        objectLanguages.add(language);
        language = new ObjectLanguage(context.getString(R.string.flag_name_spanish), "es", R.drawable.flag_sp);
        objectLanguages.add(language);
        language = new ObjectLanguage(context.getString(R.string.flag_name_french), "fr", R.drawable.flag_fr);
        objectLanguages.add(language);
        language = new ObjectLanguage(context.getString(R.string.flag_name_portugese), "pt", R.drawable.flag_pt);
        objectLanguages.add(language);
        language = new ObjectLanguage(context.getString(R.string.flag_name_korean), "ko", R.drawable.flag_kr);
        objectLanguages.add(language);
        language = new ObjectLanguage(context.getString(R.string.flag_name_german), "de", R.drawable.flag_gr);
        objectLanguages.add(language);
        language = new ObjectLanguage(context.getString(R.string.flag_name_vietnamese), "vi", R.drawable.flag_vi);
        objectLanguages.add(language);
        languages.postValue(objectLanguages);
    }

}
