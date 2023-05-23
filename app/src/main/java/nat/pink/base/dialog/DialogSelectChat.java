package nat.pink.base.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.util.Consumer;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import nat.pink.base.MainActivity;
import nat.pink.base.R;
import nat.pink.base.adapter.AdapterFakeUserContact;
import nat.pink.base.databinding.DialogSelectChatBinding;
import nat.pink.base.model.ObjectUser;



public class DialogSelectChat extends Dialog {
    private Consumer consumer;
    private DialogSelectChatBinding binding;
    private AdapterFakeUserContact adapterSuggest,adapterContact;
    public DialogSelectChat(@NonNull Context context, int themeResId, Consumer<ObjectUser> consumer) {
        super(context, themeResId);
        this.consumer = consumer;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       this.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ColorDrawable back = new ColorDrawable(Color.TRANSPARENT);
        InsetDrawable inset = new InsetDrawable(back, 100);
        this.getWindow().setBackgroundDrawable(inset);
      //  getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.CENTER);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        binding = DialogSelectChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
       // binding.extCancel.setOnClickListener(v -> dismiss());
        initData();
        initEvent();
    }
    void initData(){
        adapterSuggest = new AdapterFakeUserContact(getContext(), data -> {

        });
        adapterSuggest.setFakeUsers(generateRecommendUser());
        LinearLayoutManager ln = new LinearLayoutManager(getContext());
        ln.setOrientation(RecyclerView.VERTICAL);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),
                ln.getOrientation());
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.item_decor));
        binding.rcvSuggest.setLayoutManager(ln);
        binding.rcvSuggest.addItemDecoration(dividerItemDecoration);
        binding.rcvSuggest.setAdapter(adapterSuggest);



        //contact adapter
        adapterContact = new AdapterFakeUserContact(getContext(), user -> {
            consumer.accept(user);
          //  this.dismiss();
        });
        adapterContact.setFakeUsers(generateContactUser());
        LinearLayoutManager ln2 = new LinearLayoutManager(getContext());
        ln.setOrientation(RecyclerView.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.item_decor));
        binding.rcvContact.setLayoutManager(ln2);
        binding.rcvContact.addItemDecoration(dividerItemDecoration);
        binding.rcvContact.setAdapter(adapterContact);
    }

    void initEvent(){
        binding.ivExit.setOnClickListener(v -> DialogSelectChat.this.dismiss());

    }


    ArrayList<ObjectUser> generateRecommendUser(){
        ArrayList<ObjectUser> fakeUsers = new ArrayList<>();
        //fakeUsers.add(new ObjectUser(1,"Create new", "", 0, Uri.parse("android.resource://"+getContext().getPackageName()+"/drawable/add_fake_user2").toString()));
        fakeUsers.add(new ObjectUser(2,"Cristiano Ronaldo", "", 1, Uri.parse("android.resource://"+getContext().getPackageName()+"/drawable/ronaldo").toString(),1));
        fakeUsers.add(new ObjectUser(3,"Leo Messi", "", 1,Uri.parse("android.resource://"+getContext().getPackageName()+"/drawable/messi2").toString(),1));
        fakeUsers.add(new ObjectUser(4,"Taylor Swift", "", 1,Uri.parse("android.resource://"+getContext().getPackageName()+"/drawable/taylor").toString(),1));
        fakeUsers.add(new ObjectUser(5,"Johnny Depp", "", 1,Uri.parse("android.resource://"+getContext().getPackageName()+"/drawable/depp").toString(),1));
        return fakeUsers;
    }
    ArrayList<ObjectUser> generateContactUser(){
        ArrayList<ObjectUser> fakeUsers = new ArrayList<>();
        fakeUsers.add(new ObjectUser(-1,"Create new", "", 0, Uri.parse("android.resource://"+getContext().getPackageName()+"/drawable/add_fake_user_bg_2").toString(),0));
        //fakeUsers.add(new ObjectUser(0,"Cristiano Ronaldo", "", 1, Uri.parse("android.resource://"+getContext().getPackageName()+"/drawable/ronaldo").toString()));
               return fakeUsers;
    }

}
