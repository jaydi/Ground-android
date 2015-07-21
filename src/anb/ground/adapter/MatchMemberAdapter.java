package anb.ground.adapter;

import java.util.List;

import anb.ground.R;
import anb.ground.models.JoinUser;
import anb.ground.models.Match;
import anb.ground.utils.ImageUtils;
import anb.ground.utils.StringProvider;
import anb.ground.utils.UserListUtils;
import anb.ground.utils.Validator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MatchMemberAdapter extends BaseAdapter {
	private static final double COL_NUM = 4;

	private Context context;
	private List<JoinUser> memberList;
	private List<JoinUser> joinMemberList;
	private List<JoinUser> notJoinMemberList;
	private List<JoinUser> unknownMemberList;

	private int joinRowCount = 0;
	private int notJoinRowCount = 0;
	private int unknownRowCount = 0;

	public MatchMemberAdapter(Context context, List<JoinUser> memberList) {
		this.context = context;
		this.memberList = memberList;
	}

	private void sort() {
		joinMemberList = UserListUtils.getJoinMemberList(memberList, Match.JOIN_YES);
		notJoinMemberList = UserListUtils.getJoinMemberList(memberList, Match.JOIN_NO);
		unknownMemberList = UserListUtils.getJoinMemberList(memberList, Match.JOIN_NONE);

		joinRowCount = (int) Math.ceil((double) joinMemberList.size() / COL_NUM);
		notJoinRowCount = (int) Math.ceil((double) notJoinMemberList.size() / COL_NUM);
		unknownRowCount = (int) Math.ceil((double) unknownMemberList.size() / COL_NUM);
	}

	@Override
	public int getCount() {
		sort();
		return joinRowCount + notJoinRowCount + unknownRowCount + 3;
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
		View view;

		if (position == 0) {
			view = inflater.inflate(R.layout.list_header_section_divider_layout, parent, false);
			setSectionHeaderView(view, 0);
		} else if (position < joinRowCount + 1) {
			view = inflater.inflate(R.layout.list_item_team_member_grid_layout, parent, false);
			setRowView(view, position - 1, 0);
		} else if (position == joinRowCount + 1) {
			view = inflater.inflate(R.layout.list_header_section_divider_layout, parent, false);
			setSectionHeaderView(view, 1);
		} else if (position >= joinRowCount + 2 && position < joinRowCount + notJoinRowCount + 2) {
			view = inflater.inflate(R.layout.list_item_team_member_grid_layout, parent, false);
			setRowView(view, position - (joinRowCount + 2), 1);
		} else if (position == joinRowCount + notJoinRowCount + 2) {
			view = inflater.inflate(R.layout.list_header_section_divider_layout, parent, false);
			setSectionHeaderView(view, 2);
		} else {
			view = inflater.inflate(R.layout.list_item_team_member_grid_layout, parent, false);
			setRowView(view, position - (joinRowCount + notJoinRowCount + 3), 2);
		}

		return view;
	}

	private void setSectionHeaderView(View view, int i) {
		TextView sectionHeader = (TextView) view.findViewById(R.id.text_section_header_text);

		if (i == 0)
			sectionHeader.setText(StringProvider.getString(R.string.join) + " " + joinMemberList.size());
		else if (i == 1)
			sectionHeader.setText(StringProvider.getString(R.string.not_join) + " " + notJoinMemberList.size());
		else
			sectionHeader.setText(StringProvider.getString(R.string.not_yet) + " " + unknownMemberList.size());
	}

	private void setRowView(View view, int position, int i) {
		List<JoinUser> list;

		if (i == 0)
			list = joinMemberList;
		else if (i == 1)
			list = notJoinMemberList;
		else
			list = unknownMemberList;

		for (int j = 0; j < 4; j++) {
			JoinUser member;
			try {
				member = list.get(4 * position + j);
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
