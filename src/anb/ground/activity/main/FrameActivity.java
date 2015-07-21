package anb.ground.activity.main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import anb.ground.R;
import anb.ground.activity.action.TeamSearchActivity;
import anb.ground.activity.action.user.EditProfileActivity;
import anb.ground.activity.action.user.RegisterTeamActivity;
import anb.ground.activity.action.user.SettingsActivity;
import anb.ground.activity.main.team.TeamMainActivity;
import anb.ground.activity.main.user.UserMainActivity;
import anb.ground.adapter.DrawerMenuListAdapter;
import anb.ground.app.GlobalApplication;
import anb.ground.models.LocalUser;
import anb.ground.models.TeamHint;
import anb.ground.models.UserInfo;
import anb.ground.protocols.TeamListResponse;
import anb.ground.protocols.UserInfoResponse;
import anb.ground.server.GroundClient;
import anb.ground.server.ResponseHandler;
import anb.ground.utils.KakaoLink;
import anb.ground.utils.MenuComparator;
import anb.ground.utils.ToastUtils;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public abstract class FrameActivity extends TrackedFragmentActivity {
	public static final int MENU_ID_USER = Integer.MAX_VALUE;
	public static final int MENU_ID_NEWS_FEED = Integer.MAX_VALUE - 1;
	public static final int MENU_ID_NEW_TEAM = 0;
	public static final int MENU_ID_SEARCH_TEAM = -1;
	public static final int MENU_ID_NEW_USER = -2;
	public static final int MENU_ID_SETTINGS = -3;
	public static final int MENU_ID_AD = -4;

	private DrawerLayout mDrawerLayout;
	private ListView mDrawers;
	private DrawerMenuListAdapter adapter;
	protected ActionBarDrawerToggle mDrawerToggle;
	private List<TeamHint> menuItemList = new ArrayList<TeamHint>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawers = (ListView) findViewById(R.id.left_drawer);
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

		getActionBar().setHomeButtonEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.menu_overflow, R.string.drawer_open, R.string.drawer_close) {
			public void onDrawerClosed(View view) {
			}

			public void onDrawerOpened(View view) {
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		adapter = new DrawerMenuListAdapter(this, menuItemList);
		mDrawers.setAdapter(adapter);
		mDrawers.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (id == MENU_ID_SETTINGS) {
					Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
					startActivity(intent);
				} else if (id == MENU_ID_NEW_USER) {
					inviteUser();
				} else if (id == MENU_ID_SEARCH_TEAM) {
					Intent intent = new Intent(getApplicationContext(), TeamSearchActivity.class);
					startActivity(intent);
				} else if (id == MENU_ID_NEW_TEAM) {
					Intent intent = new Intent(getApplicationContext(), RegisterTeamActivity.class);
					startActivity(intent);
				} else if (id == MENU_ID_NEWS_FEED) {
					newsFeed();
				} else if (id == MENU_ID_USER) {
					editProfile();
				} else {
					Intent intent = new Intent(getApplicationContext(), TeamMainActivity.class);
					intent.putExtra(TeamHint.EXTRA_TEAM_HINT, menuItemList.get(position - 1));
					startActivity(intent);
				}
			}

		});
	}

	@Override
	public void onResume() {
		super.onResume();
		setBasicMenus(mDrawers);
		setMenuList(mDrawers);
	}

	private void setBasicMenus(ListView mDrawers) {
		addBasicMenuItems();
		adapter.notifyDataSetChanged();
	}

	private void addBasicMenuItems() {
		menuItemList.clear();
		menuItemList.add(new TeamHint(MENU_ID_USER, LocalUser.getInstance().getUser().getName(), LocalUser.getInstance().getUser().getImageUrl()));
		menuItemList.add(new TeamHint(MENU_ID_NEWS_FEED, getString(R.string.news_feed), ""));
		menuItemList.add(new TeamHint(MENU_ID_NEW_TEAM, getString(R.string.make_team), ""));
		menuItemList.add(new TeamHint(MENU_ID_SEARCH_TEAM, getString(R.string.search_team), ""));
		menuItemList.add(new TeamHint(MENU_ID_NEW_USER, getString(R.string.new_user), ""));
		menuItemList.add(new TeamHint(MENU_ID_SETTINGS, getString(R.string.settings), ""));
		menuItemList.add(new TeamHint(MENU_ID_AD, "", ""));
	}

	private void setMenuList(final ListView mDrawers) {
		GroundClient.getTeamList(new ResponseHandler<TeamListResponse>() {
			@Override
			public void onReceiveOK(TeamListResponse response) {
				if (response.getTeamList() == null)
					return;

				setMenuItems(response.getTeamList());
			}
		});
	}

	protected void setMenuItems(List<TeamHint> teamList) {
		addBasicMenuItems();
		menuItemList.addAll(teamList);
		Collections.sort(menuItemList, new MenuComparator());
		adapter.notifyDataSetChanged();

		saveManagingTeamList(teamList);
	}

	private void saveManagingTeamList(List<TeamHint> teamList) {
		List<Integer> managingTeamIdList = new ArrayList<Integer>();
		for (TeamHint team : teamList)
			if (team.isManaged())
				managingTeamIdList.add(team.getId());

		LocalUser.getInstance().setManagingTeamIdList(managingTeamIdList);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item))
			return true;
		else
			return super.onOptionsItemSelected(item);
	}

	public void editProfile() {
		GroundClient.getUserInfo(new ResponseHandler<UserInfoResponse>() {

			@Override
			protected void onReceiveOK(UserInfoResponse response) {
				Intent intent = new Intent(getApplicationContext(), EditProfileActivity.class);
				intent.putExtra(UserInfo.EXTRA_USER_INFO, response.getUserInfo());
				startActivity(intent);
			}

		}, LocalUser.getInstance().getUser().getId());
	}

	public void newsFeed() {
		Intent intent = new Intent(getApplicationContext(), UserMainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // clear history
		startActivity(intent);
	}

	public void inviteUser() {
		sendInvitationKakao();
	}

	private void sendInvitationKakao() {
		ArrayList<Map<String, String>> metaInfoArray = new ArrayList<Map<String, String>>();

		// If application is support Android platform.
		Map<String, String> metaInfoAndroid = new Hashtable<String, String>(1);
		metaInfoAndroid.put("os", "android");
		metaInfoAndroid.put("devicetype", "phone");
		metaInfoAndroid.put("installurl", "market://details?id=anb.ground&referrer=utm_source%3Din_app_user%26utm_medium%3Dkakao");
		metaInfoAndroid.put("executeurl", "anbGround://startGround");

		// If application is support ios platform.
		Map<String, String> metaInfoIOS = new Hashtable<String, String>(1);
		metaInfoIOS.put("os", "ios");
		metaInfoIOS.put("devicetype", "phone");
		metaInfoIOS.put("installurl", "iOS app install url");
		metaInfoIOS.put("executeurl", "anbGround://startGround");

		// add to array
		metaInfoArray.add(metaInfoAndroid);
		metaInfoArray.add(metaInfoIOS);

		KakaoLink kakaoLink = KakaoLink.getLink(this);

		if (!kakaoLink.isAvailableIntent()) {
			ToastUtils.show("not installed kakao talk");
			return;
		}

		kakaoLink.openKakaoAppLink(this, "http://link.kakao.com/?test-android-app", String.format(getString(R.string.team_invitaion), "Ground"),
				getPackageName(), GlobalApplication.getInstance().getPackageInfo().versionName, "Ground", "UTF-8", metaInfoArray);
	}

	public void openMenu() {
		mDrawerLayout.openDrawer(Gravity.LEFT);
	}
}
