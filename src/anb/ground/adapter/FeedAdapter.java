package anb.ground.adapter;

import java.util.List;

import anb.ground.R;
import anb.ground.models.Feed;
import anb.ground.utils.FeedWriter;
import anb.ground.utils.UIScaleUtils;
import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FeedAdapter extends BaseAdapter {
	private Context context;
	private List<Feed> feedList;
	private FeedAckChecker ackChecker;
	
	public interface FeedAckChecker {
		public boolean checkReadAck(long id);
	}

	public FeedAdapter(Context context, List<Feed> feedList, FeedAckChecker ackChecker) {
		super();
		this.context = context;
		this.feedList = feedList;
		this.ackChecker = ackChecker;
	}

	@Override
	public int getCount() {
		return feedList.size();
	}

	@Override
	public Object getItem(int position) {
		return feedList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return feedList.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Feed feed = feedList.get(position);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.list_item_feed_layout, parent, false);

		setMessage(view, feed, ackChecker.checkReadAck(feed.getId()));

		return view;
	}

	private void setMessage(View view, Feed feed, boolean checkReadAck) {
		TextView textTeamName = (TextView) view.findViewById(R.id.text_feed_list_item_team);
		TextView textMessage = (TextView) view.findViewById(R.id.text_feed_list_item_message);
		TextView textTime = (TextView) view.findViewById(R.id.text_feed_list_item_time);
		ImageView imageIcon = (ImageView) view.findViewById(R.id.image_feed_list_item_icon);

		if (!checkReadAck) {
			textTeamName.setBackground(context.getResources().getDrawable(R.drawable.background_feed_team_not_read));
			textTeamName.setPadding(UIScaleUtils.getPixels(40), 0, UIScaleUtils.getPixels(20), 0);
			textTeamName.setTextColor(context.getResources().getColor(R.color.green));
			view.setBackground(context.getResources().getDrawable(R.drawable.background_feed_not_read_green));
			view.setPadding(UIScaleUtils.getPixels(16), 0, 0, UIScaleUtils.getPixels(16));
			textMessage.setTextColor(context.getResources().getColor(R.color.white));
		} else {
			textTeamName.setBackground(context.getResources().getDrawable(R.drawable.background_feed_team_read));
			textTeamName.setPadding(UIScaleUtils.getPixels(40), 0, UIScaleUtils.getPixels(20), 0);
		}

		textTeamName.setText(feed.getMessage().getTeamName());
		textMessage.setText(FeedWriter.write(feed.getType(), feed.getMessage()));
		textTime.setText(DateUtils.getRelativeTimeSpanString(feed.getCreatedAt()));
		setIcon(imageIcon, checkReadAck, FeedWriter.getIconType(feed.getType()));
	}

	private void setIcon(ImageView imageIcon, boolean checkReadAck, int icon) {
		switch (icon) {
		case Feed.ICON_ALARM:
			if (checkReadAck)
				imageIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_feed_alarm_black));
			else
				imageIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_feed_alarm_white));
			break;
		case Feed.ICON_NOTICE:
			if (checkReadAck)
				imageIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_feed_notice_black));
			else
				imageIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_feed_notice_white));
			break;
		case Feed.ICON_CHECKED:
			if (checkReadAck)
				imageIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_feed_checked_black));
			else
				imageIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_feed_checked_white));
			break;
		}
	}
}
