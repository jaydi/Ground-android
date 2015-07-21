package anb.ground.activity.main.team;

import java.util.ArrayList;
import java.util.List;

import anb.ground.R;
import anb.ground.activity.action.team.match.MatchShowActivity;
import anb.ground.activity.main.TrackedFragment;
import anb.ground.adapter.MatchAdapter;
import anb.ground.adapter.MatchAdapterSettled;
import anb.ground.adapter.MatchAdapterUnsettled;
import anb.ground.models.Match;
import anb.ground.models.TeamHint;
import anb.ground.protocols.MatchHistoryResponse;
import anb.ground.protocols.MatchListResponse;
import anb.ground.server.GroundClient;
import anb.ground.server.ResponseHandler;
import anb.ground.utils.MatchListUtils;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class TeamMatchFragment extends TrackedFragment {
	private TeamHint teamHint;
	private long cur;

	private List<Match> loadedMatchList = new ArrayList<Match>();
	private List<Match> unsettledMatchList = new ArrayList<Match>();
	private List<Match> settledMatchList = new ArrayList<Match>();

	private ListView unsettledMatches;
	private PullToRefreshListView settledMatches;

	private MatchAdapter unsettledMatchAdapter;
	private MatchAdapter settledMatchAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_team_match, container, false);

		teamHint = ((TeamMainActivity) getActivity()).getTeamHint();

		unsettledMatches = (ListView) view.findViewById(R.id.list_team_match_unsettled);
		settledMatches = (PullToRefreshListView) view.findViewById(R.id.list_team_match_settled);

		OnItemClickListener itemListener = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> view, View parent, int position, long id) {
				Intent intent = new Intent(getActivity(), MatchShowActivity.class);
				intent.putExtra(TeamHint.EXTRA_TEAM_HINT, teamHint);
				intent.putExtra(Match.EXTRA_MATCH_ID, id);
				startActivity(intent);
			}

		};
		unsettledMatches.setOnItemClickListener(itemListener);
		settledMatches.setOnItemClickListener(itemListener);

		settledMatches.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				loadHistory();
			}

		});

		unsettledMatchAdapter = new MatchAdapterUnsettled(getActivity(), unsettledMatchList, teamHint.getId());
		settledMatchAdapter = new MatchAdapterSettled(getActivity(), settledMatchList, teamHint.getId());

		unsettledMatches.setAdapter(unsettledMatchAdapter);
		settledMatches.setAdapter(settledMatchAdapter);

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		cur = Long.MAX_VALUE;
		clearData();
		loadMatches();
		loadHistory();
	}

	private void clearData() {
		loadedMatchList.clear();
		unsettledMatchList.clear();
		settledMatchList.clear();
	}

	private void loadMatches() {
		GroundClient.getMatchList(new ResponseHandler<MatchListResponse>() {

			@Override
			public void onReceiveOK(MatchListResponse response) {
				if (response.getMatchList() != null)
					loadedMatchList.addAll(response.getMatchList());

				unsettledMatchList.addAll(MatchListUtils.getUnsettledList(loadedMatchList));
				settledMatchList.addAll(MatchListUtils.getSettledList(loadedMatchList, teamHint.isManaged()));

				unsettledMatchAdapter.notifyDataSetChanged();
				settledMatchAdapter.notifyDataSetChanged();
				
				guide();
			}

		}, teamHint.getId());
	}

	private void loadHistory() {
		GroundClient.getHistoryList(new ResponseHandler<MatchHistoryResponse>() {

			@Override
			public void onReceiveOK(MatchHistoryResponse response) {
				if (response.getMatchList() != null)
					settledMatchList.addAll(response.getMatchList());
				settledMatchAdapter.notifyDataSetChanged();

				if (settledMatchList.size() > 0)
					cur = settledMatchList.get(settledMatchList.size() - 1).getId();
				
				guide();
			}

		}, teamHint.getId(), cur, false);
	}

	protected void guide() {
		View guideSettled = getActivity().findViewById(R.id.linear_fragment_team_match_guide_settled);
		if (settledMatchAdapter.getCount() > 0)
			guideSettled.setVisibility(View.GONE);
		else
			guideSettled.setVisibility(View.VISIBLE);
		

		View guideUnsettled = getActivity().findViewById(R.id.linear_fragment_team_match_guide_unsettled);
		if (unsettledMatchAdapter.getCount() > 0)
			guideUnsettled.setVisibility(View.GONE);
		else
			guideUnsettled.setVisibility(View.VISIBLE);
	}
}