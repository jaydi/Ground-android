package anb.ground.adapter;

import java.util.List;

import anb.ground.R;
import anb.ground.activity.action.team.board.PostShowActivity;
import anb.ground.models.Feed;
import anb.ground.models.FeedMessage;
import anb.ground.models.Post;
import anb.ground.models.TeamHint;
import anb.ground.utils.FeedWriter;
import anb.ground.utils.ImageUtils;
import anb.ground.utils.StringProvider;
import anb.ground.utils.Validator;
import android.content.Context;
import android.content.Intent;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PostAdapter extends BaseAdapter {
	private Context context;
	private List<Post> postList;

	public PostAdapter(Context context, List<Post> postList) {
		super();
		this.context = context;
		this.postList = postList;
	}

	@Override
	public int getCount() {
		return postList.size();
	}

	@Override
	public Object getItem(int position) {
		return postList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return postList.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Post post = postList.get(position);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view;

		if (post.getType() == Post.TYPE_ARTICLE) {
			view = inflater.inflate(R.layout.list_item_post_layout, parent, false);
			setPostView(post, view);
		} else if (post.getType() == Post.TYPE_FEED) {
			view = inflater.inflate(R.layout.list_item_board_feed_layout, parent, false);
			setFeedView(post, view);
		} else
			view = inflater.inflate(R.layout.list_item_ad_layout, parent, false);

		return view;
	}

	private void setFeedView(Post post, View view) {
		FeedMessage message = post.getFeedMessage();
		int type = message.getType();

		ImageView imageIcon = (ImageView) view.findViewById(R.id.image_board_feed_item_icon);
		TextView textMessage = (TextView) view.findViewById(R.id.text_board_feed_item_message);
		TextView textTime = (TextView) view.findViewById(R.id.text_board_feed_item_time);

		setIcon(imageIcon, FeedWriter.getIconType(type));
		textMessage.setText(FeedWriter.write(type, message));
		textTime.setText(DateUtils.getRelativeTimeSpanString(post.getCreatedAt()));
	}

	private void setIcon(ImageView imageIcon, int icon) {
		switch (icon) {
		case Feed.ICON_ALARM:
			imageIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_feed_alarm_green));
			break;
		case Feed.ICON_NOTICE:
			imageIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_feed_notice_green));
			break;
		case Feed.ICON_CHECKED:
			imageIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_feed_checked_green));
			break;
		}
	}

	private void setPostView(final Post post, View view) {
		TextView textViewUserName = (TextView) view.findViewById(R.id.text_post_item_user_name);
		TextView textViewTime = (TextView) view.findViewById(R.id.text_post_item_time);
		TextView textViewContent = (TextView) view.findViewById(R.id.text_post_item_content_message);
		TextView textViewComment = (TextView) view.findViewById(R.id.text_post_item_comment_count);
		ImageView imageViewUser = (ImageView) view.findViewById(R.id.image_post_item_user_picture);
		ImageView imageViewImage = (ImageView) view.findViewById(R.id.image_post_item_content_picture);
		ImageView imageComment = (ImageView) view.findViewById(R.id.image_post_item_comment);

		textViewUserName.setText(post.getUserName());
		textViewTime.setText(DateUtils.getRelativeTimeSpanString(post.getCreatedAt()));
		textViewContent.setText(post.getMessage());
		textViewComment.setText(" " + StringProvider.getString(R.string.comment) + " " + post.getCommentCount());
		ImageUtils.getIconImage(imageViewUser, post.getUserImageUrl());
		if (!Validator.isEmpty(post.getImagePath())) {
			imageViewImage.setVisibility(View.VISIBLE);
			ImageUtils.getPreviewImage(imageViewImage, post.getImagePath());
		}

		OnClickListener listener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				TeamHint teamHint = new TeamHint();
				teamHint.setId(post.getTeamId());

				Intent intent = new Intent(context, PostShowActivity.class);
				intent.putExtra(TeamHint.EXTRA_TEAM_HINT, teamHint);
				intent.putExtra(Post.EXTRA_POST, post);
				context.startActivity(intent);
			}

		};
		textViewComment.setOnClickListener(listener);
		imageComment.setOnClickListener(listener);
	}

}
