package anb.ground.adapter;

import java.util.List;

import anb.ground.R;
import anb.ground.models.Ground;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class GroundAdapter extends BaseAdapter {
	private Context context;
	private List<Ground> groundList;
	
	public GroundAdapter(Context context, List<Ground> groundList) {
		super();
		this.context = context;
		this.groundList = groundList;
	}

	@Override
	public int getCount() {
		return groundList.size();
	}

	@Override
	public Object getItem(int position) {
		return groundList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return groundList.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Ground ground = groundList.get(position);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.list_item_searched_ground_layout, parent, false);

		TextView textName = (TextView) view.findViewById(R.id.text_searched_ground_name);
		TextView textAddress = (TextView) view.findViewById(R.id.text_searched_ground_address);
		
		textName.setText(ground.getName());
		textAddress.setText(ground.getAddress());
		
		if (ground.getId() == 0) {
			view.setBackgroundColor(context.getResources().getColor(R.color.green));
			textName.setTextColor(context.getResources().getColor(R.color.white));
		}
		
		return view;
	}

}
