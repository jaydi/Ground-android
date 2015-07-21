package anb.ground.adapter;

import java.util.List;

import anb.ground.R;
import anb.ground.models.Comment;
import anb.ground.models.LocalUser;
import anb.ground.utils.ImageUtils;
import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CommentAdapter extends BaseAdapter {
	private Context context;
	private CommentRemover remover;
	private List<Comment> commentList;

	public interface CommentRemover {
		public void removeComment(long commentId);
	}

	public CommentAdapter(Context context, List<Comment> commentList) {
		super();
		this.context = context;
		this.remover = (CommentRemover) context;
		this.commentList = commentList;
	}

	@Override
	public int getCount() {
		return commentList.size();
	}

	@Override
	public Object getItem(int position) {
		return commentList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return commentList.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Comment comment = commentList.get(position);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.list_item_comment_layout, parent, false);

		ImageView imageProfile = (ImageView) view.findViewById(R.id.image_comment_writer_profile);
		TextView textName = (TextView) view.findViewById(R.id.text_comment_writer_name);
		TextView textContent = (TextView) view.findViewById(R.id.text_comment_content);
		TextView textTime = (TextView) view.findViewById(R.id.text_comment_time);
		TextView textRemove = (TextView) view.findViewById(R.id.text_comment_remove);

		ImageUtils.getIconImage(imageProfile, comment.getUserImageUrl());
		textName.setText(comment.getUserName());
		textContent.setText(comment.getMessage());
		textTime.setText(DateUtils.getRelativeTimeSpanString(comment.getCreatedAt()));
		if (comment.getUserId() == LocalUser.getInstance().getUser().getId()) {
			textRemove.setVisibility(View.VISIBLE);
			textRemove.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					remover.removeComment(comment.getId());
				}

			});
		}

		return view;
	}
}
