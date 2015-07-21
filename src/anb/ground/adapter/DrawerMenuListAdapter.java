package anb.ground.adapter;

import java.util.List;

import anb.ground.R;
import anb.ground.activity.main.FrameActivity;
import anb.ground.app.GlobalApplication;
import anb.ground.models.TeamHint;
import anb.ground.utils.ImageUtils;
import anb.ground.utils.StringProvider;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DrawerMenuListAdapter extends BaseAdapter {
	private Context context;
	private List<TeamHint> menuItemList;

	public DrawerMenuListAdapter(Context context, List<TeamHint> menuItemList) {
		this.context = context;
		this.menuItemList = menuItemList;
	}

	@Override
	public int getCount() {
		if (menuItemList.size() > 1)
			return menuItemList.size() + 2;
		else
			return 0;
	}

	@Override
	public Object getItem(int position) {
		if (position < 2)
			return menuItemList.get(position);
		else if (position == 2)
			return null;
		else if (position < menuItemList.size() - 3)
			return menuItemList.get(position - 1);
		else if (position == menuItemList.size() - 3)
			return null;
		else
			return menuItemList.get(position - 2);
	}

	@Override
	public long getItemId(int position) {
		if (position < 2)
			return menuItemList.get(position).getId();
		else if (position == 2)
			return 0;
		else if (position < menuItemList.size() - 3)
			return menuItemList.get(position - 1).getId();
		else if (position == menuItemList.size() - 3)
			return 0;
		else
			return menuItemList.get(position - 2).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view;

		if (position < 2)
			view = getItemView(inflater, parent, menuItemList.get(position));
		else if (position == 2)
			view = getSectionHeaderView(inflater, parent, StringProvider.getString(R.string.my_teams));
		else if (position < menuItemList.size() - 3)
			view = getItemView(inflater, parent, menuItemList.get(position - 1));
		else if (position == menuItemList.size() - 3)
			view = getSectionHeaderView(inflater, parent, StringProvider.getString(R.string.my_actions));
		else
			view = getItemView(inflater, parent, menuItemList.get(position - 2));

		setFonts(view);

		return view;
	}

	private View getSectionHeaderView(LayoutInflater inflater, ViewGroup parent, String text) {
		View view = inflater.inflate(R.layout.list_header_section_layout, parent, false);
		
		TextView textView = (TextView) view.findViewById(R.id.text_section_header_text);
		textView.setText(text);
		
		return view;
	}

	private View getItemView(LayoutInflater inflater, ViewGroup parent, TeamHint menuItem) {
		if (menuItem.getId() == FrameActivity.MENU_ID_AD)
			return getAdView(inflater, menuItem, parent);
		else
			return getActionView(inflater, menuItem, parent);
	}

	private View getActionView(LayoutInflater inflater, TeamHint menuItem, ViewGroup parent) {
		View view = inflater.inflate(R.layout.menu_item_action_layout, parent, false);
		TextView textView = (TextView) view.findViewById(R.id.text_menu_action_item);
		ImageView imageView = (ImageView) view.findViewById(R.id.image_menu_action_item);

		textView.setText(menuItem.getName());
		switch (menuItem.getId()) {
		case FrameActivity.MENU_ID_NEWS_FEED:
			imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_news_feed));
			break;
		case FrameActivity.MENU_ID_NEW_TEAM:
			imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_new_team));
			break;
		case FrameActivity.MENU_ID_SEARCH_TEAM:
			imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_team_search));
			break;
		case FrameActivity.MENU_ID_NEW_USER:
			imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_new_user));
			break;
		case FrameActivity.MENU_ID_SETTINGS:
			imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_settings));
			break;
		default:
			ImageUtils.getThumnailImageCircular(imageView, menuItem.getImageUrl());
			break;
		}

		return view;
	}

	private View getAdView(LayoutInflater inflater, TeamHint menuItem, ViewGroup parent) {
		View view = inflater.inflate(R.layout.menu_item_ad_layout, parent, false);
		return view;
	}

	private void setFonts(View view) {
		final ViewGroup mContainer = (ViewGroup) view.getRootView();
		GlobalApplication.setAppFont(mContainer);
	}

}
