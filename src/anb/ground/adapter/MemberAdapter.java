package anb.ground.adapter;

import java.util.List;

import anb.ground.R;
import anb.ground.models.JoinUser;
import anb.ground.utils.ImageUtils;
import anb.ground.utils.Validator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MemberAdapter extends BaseAdapter {
	private static final double COL_NUM = 4;

	private Context context;
	private List<JoinUser> memberList;

	private int rowCount = 0;

	public MemberAdapter(Context context, List<JoinUser> memberList) {
		this.context = context;
		this.memberList = memberList;
		rowCount = (int) Math.ceil((double) memberList.size() / COL_NUM);
	}

	@Override
	public int getCount() {
		return rowCount;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.list_item_team_member_grid_layout, parent, false);

		setRowView(view, position);

		return view;
	}

	private void setRowView(View view, int position) {
		for (int j = 0; j < 4; j++) {
			JoinUser member;
			try {
				member = memberList.get(4 * position + j);
				setView(view, member, j);
			} catch (IndexOutOfBoundsException e) {
				break;
			}
		}
	}

	private void setView(View view, JoinUser member, int j) {
		ImageView picture = (ImageView) view.findViewWithTag("image_team_member_grid_picture_" + j);
		TextView name = (TextView) view.findViewWithTag("text_team_member_grid_name_" + j);

		name.setText(member.getName());
		if (Validator.isEmpty(member.getImageUrl()))
			picture.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_default));
		else
			ImageUtils.getIconImage(picture, member.getImageUrl());
	}

}
