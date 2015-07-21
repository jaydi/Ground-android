package anb.ground.activity.main.team;

import java.util.ArrayList;
import java.util.List;

import anb.ground.R;
import anb.ground.activity.main.TrackedFragment;
import anb.ground.adapter.PostAdapter;
import anb.ground.models.Post;
import anb.ground.models.TeamHint;
import anb.ground.protocols.PostListResponse;
import anb.ground.server.GroundClient;
import anb.ground.server.ResponseHandler;
import anb.ground.utils.AdManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class TeamBoardFragment extends TrackedFragment {
	private PullToRefreshListView posts;
	private TeamHint teamHint;
	private long cur;
	private List<Post> postList = new ArrayList<Post>();
	private PostAdapter teamPostAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_team_board, container, false);

		teamHint = ((TeamMainActivity) getActivity()).getTeamHint();

		posts = (PullToRefreshListView) view.findViewById(R.id.list_fragment_team_board_posts);

		posts.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				refresh();
			}

		});
		posts.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				loadPosts();
			}

		});

		teamPostAdapter = new PostAdapter(getActivity(), postList);
		posts.setAdapter(teamPostAdapter);

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		refresh();
	}

	private void refresh() {
		cur = 0;
		postList.clear();
		loadPosts();
	}

	private void loadPosts() {
		GroundClient.getPostList(new ResponseHandler<PostListResponse>() {

			@Override
			public void onReceiveOK(PostListResponse response) {
				List<Post> loadedPostList = response.getPostList();

				if (loadedPostList != null && loadedPostList.size() > 0) {
					// adds ad & sort
					AdManager.addAdAsPost(loadedPostList);
					cur = loadedPostList.get(loadedPostList.size() - 1).getId();

					// adds newly loaded data to current list
					postList.addAll(loadedPostList);
					// refreshes list view
					teamPostAdapter.notifyDataSetChanged();
				}
				
				guide();
				posts.onRefreshComplete();
			}

		}, teamHint.getId(), cur);
	}

	private void guide() {
		View guide = getActivity().findViewById(R.id.linear_fragment_team_board_guide);
		if (teamPostAdapter.getCount() > 0)
			guide.setVisibility(View.GONE);
		else
			guide.setVisibility(View.VISIBLE);
	}
}