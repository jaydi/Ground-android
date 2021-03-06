package anb.ground.adapter;

import java.util.List;

import anb.ground.R;
import anb.ground.models.JoinUser;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PushMemberSelectionAdapter extends BaseAdapter {
	private Context context;
	private List<JoinUser> memberList;
	
	private PushMemberListListener listener;
	
	public interface PushMemberListListener {
		public void onSelectMember(JoinUser member);
	}

	public PushMemberSelectionAdapter(Context context, List<JoinUser> memberList, PushMemberListListener listener) {
		super();
		this.context = context;
		this.memberList = memberList;
		this.listener = listener;
	}

	@Override
	public int getCount() {
		return memberList.size();
	}

	@Override
	public Object getItem(int position) {
		return memberList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return memberList.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final JoinUser member = memberList.get(position);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.list_item_team_member_layout, parent, false);

		TextView textName = (TextView) view.findViewById(R.id.text_member_list_item_name);
		ImageView imageView = (ImageView) view.findViewById(R.id.image_member_list_item_state);
		
		textName.setText(member.getName());
		if (member.isToPush())
			imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_checkbox_checked));
		else
			imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_checkbox_not_checked));
		
		imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				listener.onSelectMember(member);
			}
			
		});

		return view;
	}

}
