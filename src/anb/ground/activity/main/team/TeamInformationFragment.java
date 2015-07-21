package anb.ground.activity.main.team;

import java.util.ArrayList;
import java.util.List;

import anb.ground.R;
import anb.ground.activity.main.TrackedFragment;
import anb.ground.adapter.TeamMemberAdapter;
import anb.ground.models.TeamHint;
import anb.ground.models.TeamInfo;
import anb.ground.models.User;
import anb.ground.protocols.MemberListResponse;
import anb.ground.protocols.TeamInfoResponse;
import anb.ground.server.GroundClient;
import anb.ground.server.ResponseHandler;
import anb.ground.utils.ImageUtils;
import anb.ground.utils.UIScaleUtils;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class TeamInformationFragment extends TrackedFragment {
	private TeamHint teamHint;
	private TeamInfo teamInfo;

	private ListView members;

	private List<User> memberList = new ArrayList<User>();
	private List<User> wannabeList = new ArrayList<User>();

	private TeamMemberAdapter memberAdapter;

	private View view;
	private FrameLayout headerView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle state) {
		super.onCreateView(inflater, container, state);
		teamHint = ((TeamMainActivity) getActivity()).getTeamHint();

		view = inflater.inflate(R.layout.fragment_team_information, container, false);
		headerView = (FrameLayout) inflater.inflate(R.layout.list_header_team_info_layout, null, false);

		members = (ListView) view.findViewById(R.id.list_team_information_members);
		memberAdapter = new TeamMemberAdapter(getActivity(), memberList, wannabeList, teamHint);

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		setView();
	}

	private void setView() {
		GroundClient.getTeamInfo(new ResponseHandler<TeamInfoResponse>() {

			@Override
			public void onReceiveOK(TeamInfoResponse response) {
				teamInfo = response.getTeamInfo();
				if (teamInfo != null)
					setTeamInfoHeaderView();
			}

		}, teamHint.getId());
	}

	private void setTeamInfoHeaderView() {
		ImageView imageProfile = (ImageView) headerView.findViewById(R.id.image_team_member_header_team_info_picture);
		TextView textDesc = (TextView) headerView.findViewById(R.id.text_team_member_header_team_info_info);

		ImageUtils.getThumnailImageCircular(imageProfile, teamInfo.getImageUrl());
		if (teamHint.isManaged()) {
			imageProfile.setClickable(true);
			imageProfile.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					((TeamMainActivity) getActivity()).editTeamProfile();
				}

			});
		}
		textDesc.setText("팀명 : " + teamInfo.getName() + "\n지역 : " + teamInfo.getAddress() + "\n연령 : " + teamInfo.getAvgBirth() + "\n팀원 : "
				+ teamInfo.getMembersCount() + "\n전적 : " + teamInfo.getWin() + " 승 " + teamInfo.getDraw() + " 무 " + teamInfo.getLose() + " 패");

		members.removeHeaderView(headerView);
		members.addHeaderView(headerView);
		members.setAdapter(memberAdapter);

		setMemberLists();
	}

	private void setMemberLists() {
		GroundClient.getMemberList(new ResponseHandler<MemberListResponse>() {

			@Override
			public void onReceiveOK(MemberListResponse response) {
				memberList.clear();
				memberList.addAll(response.getUserList());
				memberAdapter.notifyDataSetChanged();
				guide();
			}

		}, teamHint.getId());
		GroundClient.getWannabeList(new ResponseHandler<MemberListResponse>() {

			@Override
			public void onReceiveOK(MemberListResponse response) {
				wannabeList.clear();
				wannabeList.addAll(response.getUserList());
				memberAdapter.notifyDataSetChanged();
				guide();
			}

		}, teamHint.getId());
	}

	protected void guide() {
		if (getActivity() == null)
			return;
		LinearLayout guide = (LinearLayout) getActivity().findViewById(R.id.linear_team_information_guide);

		if (memberList.size() > 1 || wannabeList.size() > 0)
			guide.setVisibility(View.GONE);
		else {
			Display display = getActivity().getWindowManager().getDefaultDisplay();
			Point size = new Point();
			display.getSize(size);

			guide.setVisibility(View.VISIBLE);
			LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.setMargins(size.x / 2 - UIScaleUtils.getPixels(20), UIScaleUtils.getPixels(340), 0, 0);
			guide.setLayoutParams(params);
		}
	}
}
