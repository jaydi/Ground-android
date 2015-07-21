package anb.ground.activity.main.user;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import anb.ground.R;
import anb.ground.activity.action.team.match.MatchShowActivity;
import anb.ground.activity.main.FrameActivity;
import anb.ground.activity.main.TrackedFragment;
import anb.ground.activity.main.team.TeamMainActivity;
import anb.ground.adapter.FeedAdapter;
import anb.ground.adapter.FeedAdapter.FeedAckChecker;
import anb.ground.app.GlobalApplication;
import anb.ground.models.Feed;
import anb.ground.models.LocalUser;
import anb.ground.models.Match;
import anb.ground.models.TeamHint;
import anb.ground.models.TeamInfo;
import anb.ground.protocols.FeedListResponse;
import anb.ground.protocols.TeamInfoResponse;
import anb.ground.server.GroundClient;
import anb.ground.server.ResponseHandler;
import anb.ground.utils.GA;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class NewsFeedFragment extends TrackedFragment implements FeedAckChecker {
	private PullToRefreshListView feeds;
	private FeedAdapter feedAdapter;
	private List<Feed> feedList = new ArrayList<Feed>();

	private long cur = 0;
	private int userId;
	private long lastAck;
	private List<Long> ackList = new ArrayList<Long>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_news_feed, container, false);
		userId = LocalUser.getInstance().getUser().getId();
		lastAck = readLastAck();

		setContents(view);
		refreshFeeds();

		return view;
	}

	private void setContents(View view) {
		feeds = (PullToRefreshListView) view.findViewById(R.id.list_news_feed_feed);
		feedAdapter = new FeedAdapter(getActivity(), feedList, this);
		feeds.setAdapter(feedAdapter);

		feeds.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> view, View parent, int position, long id) {
				showFeed(feedList.get(--position));
			}

		});

		feeds.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				refreshFeeds();
			}

		});

		feeds.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				loadFeeds();
			}

		});
	}

	private void loadFeeds() {
		GroundClient.feedList(new ResponseHandler<FeedListResponse>() {

			@Override
			protected void onReceiveOK(FeedListResponse response) {
				if (response.getFeedList() == null)
					return;

				feedList.addAll(response.getFeedList());
				feedAdapter.notifyDataSetChanged();
				feeds.onRefreshComplete();

				if (feedList.size() > 0)
					cur = feedList.get(feedList.size() - 1).getId();
				else
					guide();
			}

		}, cur);
	}

	private void guide() {
		feedList.add(Feed.guideFeed());
		feedAdapter.notifyDataSetChanged();
	}

	private void refreshFeeds() {
		cur = 0;
		feedList.clear();
		loadFeeds();
	}

	private void showFeed(Feed feed) {
		switch (feed.getType()) {
		case Feed.TYPE_GUIDE:
			openMenu();
			break;
		case Feed.TYPE_JOIN_TEAM:
			openTeamPage(feed, 3);
			break;
		case Feed.TYPE_ACCEPT_MEMBER:
			openTeamPage(feed, 3);
			break;
		case Feed.TYPE_REQUEST_MATCH:
			openMatchPage(feed);
			break;
		case Feed.TYPE_INVITE_TEAM:
			openMatchPage(feed);
			break;
		case Feed.TYPE_MATCHING_COMPLETED:
			openMatchPage(feed);
		case Feed.TYPE_SET_SCORE:
			openMatchPage(feed);
			break;
		case Feed.TYPE_ACCEPT_SCORE:
			openMatchPage(feed);
			break;
		case Feed.TYPE_SCORE_COMPLETED:
			openMatchPage(feed);
			break;
		case Feed.TYPE_DO_SURVEY:
			openMatchPage(feed);
			break;
		default:
			break;
		}

		saveAck(feed.getId());
		feedAdapter.notifyDataSetChanged();
		
		// GA logging -------
		trackEvent(GA.CATEGORY_FEED, GA.ACTION_CLICK, String.valueOf(feed.getType()));
		// ------------------
	}

	private void openMenu() {
		((FrameActivity) getActivity()).openMenu();
	}

	private void openTeamPage(Feed feed, final int position) {
		GroundClient.getTeamInfo(new ResponseHandler<TeamInfoResponse>() {

			@Override
			protected void onReceiveOK(TeamInfoResponse response) {
				TeamInfo teamInfo = response.getTeamInfo();
				Intent intent = new Intent(getActivity(), TeamMainActivity.class);
				intent.putExtra(TeamHint.EXTRA_TEAM_HINT, new TeamHint(teamInfo.getId(), teamInfo.getName(), teamInfo.getImageUrl(), LocalUser.getInstance()
						.managing(teamInfo.getId())));
				intent.putExtra(TeamMainActivity.EXTRA_TEAM_ACTIVITY_POSITION, position);
				startActivity(intent);
			}

		}, feed.getMessage().getTeamId());
	}

	private void openMatchPage(Feed feed) {
		Intent intent = new Intent(getActivity(), MatchShowActivity.class);
		intent.putExtra(TeamHint.EXTRA_TEAM_HINT, new TeamHint(feed.getMessage().getTeamId(), feed.getMessage().getTeamName(), null, LocalUser.getInstance()
				.managing(feed.getMessage().getTeamId())));
		intent.putExtra(Match.EXTRA_MATCH_ID, feed.getMessage().getMatchId());
		startActivity(intent);
	}
	
	// news feed ACK
	
	public void onDestroy() {
		super.onDestroy();
		writeLastAck();
	}

	private long readLastAck() {
		DataInputStream is = null;
		try {
			is = new DataInputStream(GlobalApplication.getInstance().getApplicationContext().openFileInput(userId + "feedack"));
			return is.readLong();
		} catch (FileNotFoundException e) {
			writeLastAck();
			return 0;
		} catch (IOException e) {
			return 0;
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void writeLastAck() {
		DataOutputStream os = null;
		try {
			os = new DataOutputStream(GlobalApplication.getInstance().getApplicationContext()
					.openFileOutput(userId + "feedack", Context.MODE_PRIVATE));
			os.writeLong(getNewLastAck());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (os != null)
					os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private long getNewLastAck() {
		long ack = lastAck;

		for (long newAck : ackList)
			if (ack < newAck)
				ack = newAck;

		return ack;
	}

	public void saveAck(long id) {
		if (lastAck < id && !ackList.contains(id))
			ackList.add(id);
	}

	@Override
	public boolean checkReadAck(long id) {
		return lastAck >= id || ackList.contains(id);
	}
}
