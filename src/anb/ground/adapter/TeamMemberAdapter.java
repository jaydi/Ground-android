package anb.ground.adapter;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import anb.ground.R;
import anb.ground.activity.action.team.manage.NewManagerActivity;
import anb.ground.app.GlobalApplication;
import anb.ground.dialogs.MemberShowDialog;
import anb.ground.models.TeamHint;
import anb.ground.models.TeamInfo;
import anb.ground.models.User;
import anb.ground.utils.ImageUtils;
import anb.ground.utils.KakaoLink;
import anb.ground.utils.ToastUtils;
import anb.ground.utils.UserListUtils;
import anb.ground.utils.Validator;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TeamMemberAdapter extends BaseAdapter {
	private static final double COL_NUM = 4;

	private FragmentActivity context;
	private List<User> memberList;
	private List<User> wannabeList;
	private List<User> managerList;
	private List<User> normalList;
	private TeamHint teamHint;

	private int wannabeRowCount = 0;
	private int managerRowCount = 0;
	private int normalRowCount = 0;

	private boolean flag = false;

	public TeamMemberAdapter(FragmentActivity context, List<User> memberList, List<User> wannabeList, TeamHint teamHint) {
		super();
		this.context = context;
		this.memberList = memberList;
		this.wannabeList = wannabeList;
		this.teamHint = teamHint;
	}

	private void sort() {
		managerList = UserListUtils.getManagerList(memberList, teamHint.isManaged()); // adder not included for normal members
		normalList = UserListUtils.getNormalMemberList(memberList, true); // adder always included

		wannabeRowCount = (int) Math.ceil((double) wannabeList.size() / COL_NUM);
		managerRowCount = (int) Math.ceil((double) managerList.size() / COL_NUM);
		normalRowCount = (int) Math.ceil((double) normalList.size() / COL_NUM);

		flag = (wannabeRowCount > 0) ? true : false;
		// 'want to be a team member' list not shown for normal members
		if (flag)
			flag = teamHint.isManaged();
	}

	@Override
	public int getCount() {
		sort();
		return wannabeRowCount + managerRowCount + normalRowCount + ((flag) ? 3 : 2);
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
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
		sort();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view;

		if (flag) {
			if (position == 0) {
				view = inflater.inflate(R.layout.list_header_section_divider_layout, parent, false);
				setSectionHeaderView(view, 0);
			} else if (position < wannabeRowCount + 1) {
				view = inflater.inflate(R.layout.list_item_team_member_grid_layout, parent, false);
				setRowView(view, position - 1, 0, true);
			} else if (position == wannabeRowCount + 1) {
				view = inflater.inflate(R.layout.list_header_section_divider_layout, parent, false);
				setSectionHeaderView(view, 1);
			} else if (position >= wannabeRowCount + 2 && position < (wannabeRowCount + managerRowCount + 2)) {
				view = inflater.inflate(R.layout.list_item_team_member_grid_layout, parent, false);
				setRowView(view, position - (wannabeRowCount + 2), 1, false);
			} else if (position == wannabeRowCount + managerRowCount + 2) {
				view = inflater.inflate(R.layout.list_header_section_divider_layout, parent, false);
				setSectionHeaderView(view, 2);
			} else {
				view = inflater.inflate(R.layout.list_item_team_member_grid_layout, parent, false);
				setRowView(view, position - (wannabeRowCount + managerRowCount + 3), 2, false);
			}
		} else {
			if (position == 0) {
				view = inflater.inflate(R.layout.list_header_section_divider_layout, parent, false);
				setSectionHeaderView(view, 1);
			} else if (position < managerRowCount + 1) {
				view = inflater.inflate(R.layout.list_item_team_member_grid_layout, parent, false);
				setRowView(view, position - 1, 1, false);
			} else if (position == managerRowCount + 1) {
				view = inflater.inflate(R.layout.list_header_section_divider_layout, parent, false);
				setSectionHeaderView(view, 2);
			} else {
				view = inflater.inflate(R.layout.list_item_team_member_grid_layout, parent, false);
				setRowView(view, position - (managerRowCount + 2), 2, false);
			}
		}

		return view;
	}

	private void setSectionHeaderView(View view, int i) {
		TextView sectionHeader = (TextView) view.findViewById(R.id.text_section_header_text);

		if (i == 0)
			sectionHeader.setText(context.getResources().getString(R.string.wannabe));
		else if (i == 1)
			sectionHeader.setText(context.getResources().getString(R.string.manager));
		else
			sectionHeader.setText(context.getResources().getString(R.string.members));
	}

	private void setRowView(View view, int position, int i, boolean isWannabe) {
		List<User> list;
		if (i == 0)
			list = wannabeList;
		else if (i == 1)
			list = managerList;
		else
			list = normalList;

		for (int j = 0; j < 4; j++) {
			User member;
			try {
				member = list.get(4 * position + j);
				setView(view, member, j, isWannabe);
			} catch (IndexOutOfBoundsException e) {
				break;
			}
		}
	}

	private void setView(View view, final User member, int j, final boolean isWannabe) {
		ImageView picture = (ImageView) view.findViewWithTag("image_team_member_grid_picture_" + j);
		TextView name = (TextView) view.findViewWithTag("text_team_member_grid_name_" + j);

		if (member.getId() == User.MEMBER_ID_NEW_MANAGER) {
			picture.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_add_user));
			name.setText(context.getResources().getString(R.string.new_manager));
		} else if (member.getId() == User.MEMBER_ID_INVITE_KAKAO) {
			picture.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_kakao_button));
			name.setText(context.getResources().getString(R.string.send_invitation_kakao));
		} else {
			if (Validator.isEmpty(member.getImageUrl()))
				picture.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_default));
			else
				ImageUtils.getIconImage(picture, member.getImageUrl());
			name.setText(member.getName());
		}

		picture.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (member.getId() == User.MEMBER_ID_NEW_MANAGER)
					newManager();
				else if (member.getId() == User.MEMBER_ID_INVITE_KAKAO)
					sendInvitationKakao();
				else
					showUserInfo(member.getId(), isWannabe);
			}

		});
	}

	private void showUserInfo(long id, boolean isWannabe) {
		MemberShowDialog dialog = new MemberShowDialog();
		dialog.setUserId((int) id);
		dialog.setIsWannabe(isWannabe);
		dialog.show(context.getSupportFragmentManager(), "memberShow");
	}

	private void newManager() {
		Intent intent = new Intent(context, NewManagerActivity.class);
		intent.putExtra(TeamHint.EXTRA_TEAM_HINT, teamHint);
		intent.putParcelableArrayListExtra(TeamInfo.EXTRA_TEAM_MEMBERS, new ArrayList<User>(memberList));
		context.startActivity(intent);
	}

	private void sendInvitationKakao() {
		ArrayList<Map<String, String>> metaInfoArray = new ArrayList<Map<String, String>>();

		// If application is support Android platform.
		Map<String, String> metaInfoAndroid = new Hashtable<String, String>(1);
		metaInfoAndroid.put("os", "android");
		metaInfoAndroid.put("devicetype", "phone");
		metaInfoAndroid.put("installurl", "market://details?id=anb.ground&referrer=utm_source%3Din_app_team%26utm_medium%3Dkakao");
		metaInfoAndroid.put("executeurl", "anbGround://showInvitation?teamId=" + teamHint.getId());

		// If application is support ios platform.
		Map<String, String> metaInfoIOS = new Hashtable<String, String>(1);
		metaInfoIOS.put("os", "ios");
		metaInfoIOS.put("devicetype", "phone");
		metaInfoIOS.put("installurl", "iOS app install url");
		metaInfoIOS.put("executeurl", "anbGround://startGround");

		// add to array
		metaInfoArray.add(metaInfoAndroid);
		metaInfoArray.add(metaInfoIOS);

		KakaoLink kakaoLink = KakaoLink.getLink(context);

		if (!kakaoLink.isAvailableIntent()) {
			ToastUtils.show("not installed kakao talk");
			return;
		}

		kakaoLink.openKakaoAppLink(context, "http://link.kakao.com/?test-android-app",
				String.format(context.getResources().getString(R.string.team_invitaion), teamHint.getName()), context.getApplicationContext().getPackageName(),
				GlobalApplication.getInstance().getPackageInfo().versionName, "Ground", "UTF-8", metaInfoArray);
	}

}
