package anb.ground.activity.action.team.match;

import java.util.ArrayList;
import java.util.List;

import anb.ground.R;
import anb.ground.activity.main.TrackedActivity;
import anb.ground.adapter.MatchAdapterSettled;
import anb.ground.models.Match;
import anb.ground.models.TeamHint;
import anb.ground.protocols.MatchHistoryResponse;
import anb.ground.server.GroundClient;
import anb.ground.server.ResponseHandler;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class ShowRecordsActivity extends TrackedActivity {
	private TeamHint teamHint;
	private long cur = 0;

	private List<Match> recordList = new ArrayList<Match>();

	private PullToRefreshListView records;
	private MatchAdapterSettled recordAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_records);
		
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setLogo(getResources().getDrawable(R.drawable.menu_back));
		
		teamHint = getIntent().getParcelableExtra(TeamHint.EXTRA_TEAM_HINT);

		recordAdapter = new MatchAdapterSettled(this, recordList, teamHint.getId());
		records = (PullToRefreshListView) findViewById(R.id.list_show_records_history);
		records.setAdapter(recordAdapter);
		records.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				loadRecords();
			}
			
		});
		
		loadRecords();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.show_records, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		
		switch (item.getItemId()) {
		case android.R.id.home :
			super.onBackPressed();
			break;
		}
		
		return true;
	}

	private void loadRecords() {
		GroundClient.getHistoryList(new ResponseHandler<MatchHistoryResponse>() {

			@Override
			public void onReceiveOK(MatchHistoryResponse response) {
				recordList.addAll(response.getMatchList());
				recordAdapter.notifyDataSetChanged();

				if (recordList.size() > 0)
					cur = recordList.get(recordList.size() - 1).getId();
			}

		}, teamHint.getId(), cur, false);
	}

}
