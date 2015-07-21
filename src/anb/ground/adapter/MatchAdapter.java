package anb.ground.adapter;

import java.util.List;

import anb.ground.app.GlobalApplication;
import anb.ground.models.Match;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class MatchAdapter extends BaseAdapter {
	protected Context context;
	protected List<Match> matchList;
	protected int teamId;

	public MatchAdapter(Context context, List<Match> matchList, int teamId) {
		this.context = context;
		this.matchList = matchList;
		this.teamId = teamId;
	}

	@Override
	public int getCount() {
		return matchList.size();
	}

	@Override
	public Object getItem(int position) {
		return matchList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return matchList.get(position).getId();
	}

	@Override
	public abstract View getView(int position, View convertView, ViewGroup parent);

	protected void setFonts(View view) {
		final ViewGroup mContainer = (ViewGroup) view.getRootView();
		GlobalApplication.setAppFont(mContainer);
	}

}
