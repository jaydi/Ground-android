package anb.ground.adapter;

import java.util.List;

import anb.ground.R;
import anb.ground.models.TeamInfo;
import anb.ground.utils.ImageUtils;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TeamListAdapter extends BaseAdapter {
	private Context context;
	private List<TeamInfo> teamList;

	public TeamListAdapter(Context context, List<TeamInfo> teamList) {
		super();
		this.context = context;
		this.teamList = teamList;
	}

	@Override
	public int getCount() {
		return teamList.size();
	}

	@Override
	public Object getItem(int position) {
		return teamList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return teamList.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TeamInfo team = teamList.get(position);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (team.getId() == 0)
			return inflater.inflate(R.layout.list_item_loading_big_layout, parent, false);
		
		View view = inflater.inflate(R.layout.list_item_searched_team_layout, parent, false);

		TextView textTeamName = (TextView) view.findViewById(R.id.text_searched_team_name);
		ImageView imageEmblem = (ImageView) view.findViewById(R.id.image_searched_team_emblem);
		
		textTeamName.setText(team.getName());
		ImageUtils.getThumnailImageCircular(imageEmblem, team.getImageUrl());
		
		return view;
	}

}
