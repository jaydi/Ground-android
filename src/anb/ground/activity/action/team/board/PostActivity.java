package anb.ground.activity.action.team.board;

import java.util.List;

import anb.ground.R;
import anb.ground.activity.main.TrackedFragmentActivity;
import anb.ground.models.Post;
import anb.ground.models.TeamHint;
import anb.ground.protocols.UploadImageResponse;
import anb.ground.protocols.WritePostResponse;
import anb.ground.server.DialogResponseHandler;
import anb.ground.server.GroundClient;
import anb.ground.server.ResponseHandler;
import anb.ground.utils.DialogUtils;
import anb.ground.utils.ImageUtils;
import anb.ground.utils.Validator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class PostActivity extends TrackedFragmentActivity {
	private final static int REQUEST_GET_IMAGE = 47587;
	
	private String imagePath;
	private TeamHint teamHint;
	private String message;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post);

		teamHint = getIntent().getParcelableExtra(TeamHint.EXTRA_TEAM_HINT);

		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setLogo(getResources().getDrawable(R.drawable.menu_back));

		// for resizing layout when keyboard is up
		final LinearLayout postBox = (LinearLayout) findViewById(R.id.linear_post_container);
		postBox.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

			@Override
			public void onGlobalLayout() {
				Rect r = new Rect();
				postBox.getWindowVisibleDisplayFrame(r);

				LayoutParams params = postBox.getLayoutParams();
				params.height = (r.bottom - r.top) - getActionBar().getHeight();
				postBox.setLayoutParams(params);
			}

		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		MenuInflater inflater = this.getMenuInflater();
		inflater.inflate(R.menu.post, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);

		switch (item.getItemId()) {
		case android.R.id.home:
			super.onBackPressed();
			break;
		case R.id.action_post:
			post();
			break;
		}

		return true;
	}

	@Override
	public void onResume() {
		super.onResume();
		if (!Validator.isEmpty(imagePath)) {
			ImageView imageView = (ImageView) findViewById(R.id.image_post_preview);
			ImageUtils.getThumnailImage(imageView, imagePath);
			imageView.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onRestoreInstanceState(Bundle state) {
		super.onRestoreInstanceState(state);
		imagePath = state.getString("imagePath");
	}

	@Override
	public void onSaveInstanceState(Bundle state) {
		super.onSaveInstanceState(state);
		state.putString("imagePath", imagePath);
	}
	
	public void capture(View view) {
		Intent imgIntent;
		imgIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		PackageManager manager = getPackageManager();
		List<ResolveInfo> activities = manager.queryIntentActivities(imgIntent, 0);
		boolean isIntentSafe = activities.size() > 0;

		if (isIntentSafe)
			startActivityForResult(imgIntent, REQUEST_GET_IMAGE);
	}
	
	public void album(View view) {
		Intent imgIntent;
		imgIntent = new Intent(Intent.ACTION_PICK, Media.EXTERNAL_CONTENT_URI);

		PackageManager manager = getPackageManager();
		List<ResolveInfo> activities = manager.queryIntentActivities(imgIntent, 0);
		boolean isIntentSafe = activities.size() > 0;

		if (isIntentSafe)
			startActivityForResult(imgIntent, REQUEST_GET_IMAGE);
	}

	private void post() {
		EditText editTextPost = (EditText) findViewById(R.id.edit_post_message);
		message = editTextPost.getEditableText().toString();

		Post post = new Post(teamHint.getId(), Post.TYPE_ARTICLE, message, imagePath);

		if (Validator.validatePost(post))
			if (Validator.isEmpty(imagePath) || imagePath.startsWith("http"))
				postText(post);
			else
				postImageAndText(post);
	}

	private void postImageAndText(final Post post) {
		ImageUtils.uploadImage(new DialogResponseHandler<UploadImageResponse>(DialogUtils.showWaitingDialog(this)) {

			@Override
			public void onReceiveOK(UploadImageResponse response) {
				post.setImagePath(response.getPath());
				postText(post);
			}

		}, imagePath);
	}

	private void postText(Post post) {
		GroundClient.writePost(new ResponseHandler<WritePostResponse>() {

			@Override
			public void onReceiveOK(WritePostResponse response) {
				Log.i("response", response.toString());
				finish();
			}

		}, post);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_GET_IMAGE && resultCode == RESULT_OK && data != null) {
			Uri image = data.getData();
			String[] projection = { Media.DATA };

			Cursor cursor = getContentResolver().query(image, projection, null, null, null);
			cursor.moveToFirst();

			int column = cursor.getColumnIndex(Media.DATA);
			imagePath = cursor.getString(column);

			cursor.close();
		}
	}
}
