package nat.pink.base.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import java.util.List;

import nat.pink.base.R;
import nat.pink.base.custom.view.ExtTextView;
import nat.pink.base.model.ObjectFaq;

public class AdapterFaq extends BaseExpandableListAdapter {
    private List<ObjectFaq> faqList;
    public LayoutInflater inflater;
    public Activity activity;

    public AdapterFaq(Activity act) {
        activity = act;
        inflater = act.getLayoutInflater();
    }

    @Override
    public int getGroupCount() {
        return faqList != null ? faqList.size() : 0;
    }

    @Override
    public int getChildrenCount(int i) {
        return faqList.get(i).getContent() != null ? faqList.get(i).getContent().size() : 0;
    }

    @Override
    public Object getGroup(int i) {
        return faqList.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return faqList.get(i).getContent().get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = inflater.inflate(R.layout.listrow_group, null);
        }
        ObjectFaq faq = (ObjectFaq) getGroup(groupPosition);
        ExtTextView textGroup = view.findViewById(R.id.listTitle);
        textGroup.setText(faq.getTitle());
        textGroup.setCompoundDrawablesWithIntrinsicBounds(0, 0, isExpanded ? R.drawable.ic_arrow_up : R.drawable.ic_arrow_down, 0);
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isExpanded, View view, ViewGroup viewGroup) {
        final String item = (String) getChild(groupPosition, childPosition);
        if (view == null) {
            view = inflater.inflate(R.layout.listrow_item, null);
        }
        ExtTextView textChild = view.findViewById(R.id.title);
        textChild.setText(item);
//        view.setOnClickListener(view1 -> Toast.makeText(activity, subcatalog.getTitle(),
//                Toast.LENGTH_SHORT).show());
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
