package anb.ground.activity.action.team.manage;

import java.util.ArrayList;
import java.util.List;

import anb.ground.R;
import anb.ground.activity.main.TrackedActivity;
import anb.ground.adapter.TeamManagerSelectionAdapter;
import anb.ground.adapter.TeamManagerSelectionAdapter.TeamMemberListListener;
import anb.ground.models.TeamHint;
import anb.ground.models.TeamInfo;
import anb.ground.models.User;
import anb.ground.protocols.NewManagerResponse;
import anb.ground.server.GroundClient;
import anb.ground.server.ResponseHandler;
import anb.ground.utils.ToastUtils;
import anb.ground.utils.UserListUtils;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

public class NewManagerActivity extends TrackedActivity implements TeamMemberListListener {
	private TeamHint teamHint;
	
	private List<User> memberList;
	private List<User> managerList;

	private ListView managers;
	private ListView normals;

	private TeamManagerSelectionAdapter managerAdapter;
	private TeamManagerSelectionAdapter normalAdapter;
	
	List<Integer> oldManagerIdList = new ArrayList<Integer>();
	List<Integer> newManagerIdList = new ArrayList<Integer>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_manager);

		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setLogo(getResources().getDrawable(R.drawable.menu_back));
		
		teamHint = getIntent().getParcelableExtra(TeamHint.EXTRA_TEAM_HINT);
		
		memberList = getIntent().getParcelableArrayListExtra(TeamInfo.EXTRA_TEAM_MEMBERS);
		managerList = UserListUtils.getManagerList(memberList, false);
		
		saveOldManagerIdList();

		normals = (ListView) findViewById(R.id.list_new_manager_normal);
		managers = (ListView) findViewById(R.id.list_new_manager_manager);
		
		managerAdapter = new TeamManagerSelectionAdapter(this, managerList, this);
		normalAdapter = new TeamManagerSelectionAdapter(this, memberList, this);

		managers.setAdapter(managerAdapter);
		normals.setAdapter(normalAdapter);
	}

	private void saveOldManagerIdList() {
		for (User manager : managerList)
			oldManagerIdList.add(manager.getId());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.new_manager, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			super.onBackPressed();
			break;
		case R.id.action_new_manger:
			newManager();
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onSelectMember(User member) {
		if (member.isManager())
			remove(member);
		else
			add(member);
	}

	private void add(User member) {
		member.setManager(true);
		managerList.add(member);
		refresh();
	}

	private void remove(User member) {
		member.setManager(false);
		managerList.remove(member);
		refresh();
	}

	private void refresh() {
		managerAdapter.notifyDataSetChanged();
		managers.post(new Runnable() {

			@Override
			public void run() {
				managers.setSelection(managerAdapter.getCount() - 1);
			}
			
		});
		normalAdapter.notifyDataSetChanged();
	}

	private void newManager() {
		if (managerList.size() == 0) {
			ToastUtils.show(R.string.alert_no_manager);
			return;
		}
		
		arrangeOldAndNew();
		
		GroundClient.newManager(new ResponseHandler<NewManagerResponse>() {
			
			@Override
			public void onReceiveOK(NewManagerResponse response) {
				finish();
			}
			
		}, newManagerIdList, oldManagerIdList, teamHint.getId());
	}

	private void arrangeOldAndNew() {
		for (User manager : managerList)
			newManagerIdList.add(manager.getId());
		
		for (int i = 0; i < oldManagerIdList.size(); i++) {
			Integer j = oldManagerIdList.get(i);
			if (newManagerIdList.contains(j)) {
				oldManagerIdList.remove(j);
				newManagerIdList.remove(j);
				i--;
			}
		}
	}
}
