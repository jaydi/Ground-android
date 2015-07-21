package anb.ground.activity.action.team.board;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import anb.ground.R;
import anb.ground.activity.main.TrackedActivity;
import anb.ground.adapter.CommentAdapter;
import anb.ground.adapter.CommentAdapter.CommentRemover;
import anb.ground.models.Comment;
import anb.ground.models.LocalUser;
import anb.ground.models.Post;
import anb.ground.models.TeamHint;
import anb.ground.protocols.CommentListResponse;
import anb.ground.protocols.RemoveCommentResponse;
import anb.ground.protocols.RemovePostResponse;
import anb.ground.protocols.WriteCommentResponse;
import anb.ground.server.GroundClient;
import anb.ground.server.ResponseHandler;
import anb.ground.utils.CommentComparator;
import anb.ground.utils.DialogUtils;
import anb.ground.utils.ImageUtils;
import anb.ground.utils.StringProvider;
import anb.ground.utils.ToastUtils;
import anb.ground.utils.Validator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class PostShowActivity extends TrackedActivity implements CommentRemover {
	private TeamHint teamHint;
	private Post post;

	private ListView comments;
	private List<Comment> commentList = new ArrayList<Comment>();
	private CommentAdapter commentAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post_show);

		teamHint = getIntent().getParcelableExtra(TeamHint.EXTRA_TEAM_HINT);
		post = getIntent().getParcelableExtra(Post.EXTRA_POST);

		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setLogo(getResources().getDrawable(R.drawable.menu_back));

		comments = (ListView) findViewById(R.id.list_post_show_comments);
		setHeaderView();
		setFooterView();

		commentAdapter = new CommentAdapter(this, commentList);
		comments.setAdapter(commentAdapter);
		refreshComments();
	}

	private void setFooterView() {
		View view = getLayoutInflater().inflate(R.layout.list_footer_comment_input_layout, null, false);

		ImageView imageWriter = (ImageView) view.findViewById(R.id.image_post_show_user_pic);
		ImageUtils.getIconImage(imageWriter, LocalUser.getInstance().getUser().getImageUrl());

		comments.addFooterView(view);
	}

	private void setHeaderView() {
		View view = getLayoutInflater().inflate(R.layout.list_header_post_layout, null, false);

		ImageView writerImage = (ImageView) view.findViewById(R.id.image_post_show_profile_pic);
		TextView textName = (TextView) view.findViewById(R.id.text_post_show_profile_name);
		TextView textTime = (TextView) view.findViewById(R.id.text_post_show_profile_time);
		ImageView contentImage = (ImageView) view.findViewById(R.id.image_post_show_content_pic);
		TextView textContent = (TextView) view.findViewById(R.id.text_post_show_content);
		TextView textCommentCount = (TextView) view.findViewById(R.id.text_post_show_comment_count);
		TextView textRemove = (TextView) view.findViewById(R.id.text_post_show_remove);

		ImageUtils.getIconImage(writerImage, post.getUserImageUrl());
		textName.setText(post.getUserName());
		textTime.setText(DateUtils.getRelativeTimeSpanString(post.getCreatedAt()));
		if (!Validator.isEmpty(post.getImagePath())) {
			contentImage.setVisibility(View.VISIBLE);
			ImageUtils.getViewImage(contentImage, post.getImagePath());
		}
		textContent.setText(post.getMessage());
		textCommentCount.setText(StringProvider.getString(R.string.comment) + " " + post.getCommentCount());
		if (post.getUserId() == LocalUser.getInstance().getUser().getId())
			textRemove.setVisibility(View.VISIBLE);

		comments.addHeaderView(view);
	}

	private void refreshComments() {
		GroundClient.commentList(new ResponseHandler<CommentListResponse>() {

			@Override
			protected void onReceiveOK(CommentListResponse response) {
				commentList.clear();
				commentList.addAll(response.getCommentList());
				Collections.sort(commentList, new CommentComparator());
				commentAdapter.notifyDataSetChanged();
			}

		}, teamHint.getId(), post.getId());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.show_post, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);

		switch (item.getItemId()) {
		case android.R.id.home:
			super.onBackPressed();
			break;
		}

		return true;
	}

	public void removePost(View view) {
		DialogUtils.showConfirmDialogFromActionBar(this, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				GroundClient.removePost(new ResponseHandler<RemovePostResponse>() {

					@Override
					protected void onReceiveOK(RemovePostResponse response) {
						finish();
					}

				}, teamHint.getId(), post.getId());
			}

		}, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}

		}, R.string.confirm_remove_post);
	}

	public void showPicture(View view) {
		// TODO full screen image
	}

	public void addComment(View view) {
		final EditText editComment = (EditText) findViewById(R.id.edit_post_show_comment);
		String comment = editComment.getEditableText().toString();
		editComment.setText("");

		if (Validator.isEmpty(comment)) {
			ToastUtils.show(R.string.alert_empty_comment);
			return;
		}

		GroundClient.writeComment(new ResponseHandler<WriteCommentResponse>() {

			@Override
			protected void onReceiveOK(WriteCommentResponse response) {
				refreshComments();
				hideSoftInput(editComment);
			}

		}, teamHint.getId(), post.getId(), comment);
	}
	
	@Override
	public void removeComment(long id) {
		GroundClient.removeComment(new ResponseHandler<RemoveCommentResponse>() {

			@Override
			protected void onReceiveOK(RemoveCommentResponse response) {
				refreshComments();
			}
			
		}, post.getId(), id);
	}

	private void hideSoftInput(EditText editText) {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);

	}
}
