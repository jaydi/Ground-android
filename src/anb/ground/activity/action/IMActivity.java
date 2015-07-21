package anb.ground.activity.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import anb.ground.R;
import anb.ground.activity.main.TrackedActivity;
import anb.ground.adapter.IMessageAdapter;
import anb.ground.models.IMessage;
import anb.ground.models.LocalUser;
import anb.ground.models.Match;
import anb.ground.models.MatchInfo;
import anb.ground.models.TeamHint;
import anb.ground.protocols.MatchInfoResponse;
import anb.ground.protocols.MessageListResponse;
import anb.ground.server.GroundClient;
import anb.ground.server.GroundIMClient;
import anb.ground.server.ResponseHandler;
import anb.ground.utils.IMessageComparator;
import anb.ground.utils.ProtocolCodec;
import anb.ground.utils.Validator;
import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

public class IMActivity extends TrackedActivity {
	private TeamHint myTeamHint;
	private TeamHint teamHint;
	private long matchId;
	private MatchInfo matchInfo;
	private long cur;

	private ListView messages;
	private List<IMessage> messageList = new ArrayList<IMessage>();
	private IMessageAdapter messageAdapter;
	
	private int tempId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_im);

		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setLogo(getResources().getDrawable(R.drawable.menu_back));

		teamHint = getIntent().getParcelableExtra(TeamHint.EXTRA_TEAM_HINT);
		matchId = getIntent().getLongExtra(Match.EXTRA_MATCH_ID, 0);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);

		switch (item.getItemId()) {
		case android.R.id.home:
			this.onBackPressed();
			break;
		}

		return true;
	}

	@Override
	public void onResume() {
		super.onResume();

		GroundClient.getMatchInfo(new ResponseHandler<MatchInfoResponse>() {

			@Override
			protected void onReceiveOK(MatchInfoResponse response) {
				matchInfo = response.getMatchInfo();
				setMyTeamHint();
				setAdapter();
				initialize();
			}

		}, matchId, teamHint.getId());
	}

	@SuppressLint("HandlerLeak")
	private void initialize() {
		GroundIMClient.init(new Handler() {

			@Override
			public void handleMessage(Message msg) {
				IMessage message = ProtocolCodec.decode((String) msg.obj, IMessage.class);
				messageList.add(message);
				messageAdapter.notifyDataSetChanged();
				scrollDown(null);
			}

		});
	}

	private void setAdapter() {
		messages = (ListView) findViewById(R.id.list_im_messages);
		messageAdapter = new IMessageAdapter(this, messageList);
		messages.setAdapter(messageAdapter);

		loadHistory();
		setAutoScrollDown();
	}

	private void setAutoScrollDown() {
		// for resizing layout when keyboard is up
		final LinearLayout imBox = (LinearLayout) findViewById(R.id.linear_im_container);
		imBox.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

			@Override
			public void onGlobalLayout() {
				Rect r = new Rect();
				imBox.getWindowVisibleDisplayFrame(r);

				LayoutParams params = imBox.getLayoutParams();
				params.height = (r.bottom - r.top) - getActionBar().getHeight();
				imBox.setLayoutParams(params);
			}

		});
	}

	private void loadHistory() {
		GroundClient.messageList(new ResponseHandler<MessageListResponse>() {

			@Override
			protected void onReceiveOK(MessageListResponse response) {
				messageList.clear();
				messageList.addAll(response.getMessageList());
				Collections.sort(messageList, new IMessageComparator());
				messageAdapter.notifyDataSetChanged();
				scrollDown(null);

				if (messageList.size() > 0)
					cur = messageList.get(messageList.size() - 1).getId();
			}

		}, matchId, teamHint.getId(), cur);
	}

	private void setMyTeamHint() {
		if (LocalUser.getInstance().managing(matchInfo.getHomeTeamId()))
			myTeamHint = new TeamHint(matchInfo.getHomeTeamId(), matchInfo.getHomeTeamName(), matchInfo.getHomeImageUrl(), true);
		else
			myTeamHint = new TeamHint(matchInfo.getAwayTeamId(), matchInfo.getAwayTeamName(), matchInfo.getAwayImageUrl(), true);
	}

	public void scrollDown(View view) {
		messages.post(new Runnable() {

			@Override
			public void run() {
				messages.setSelection(messageAdapter.getCount() - 1);
			}

		});
	}

	public void sendMessage(View view) {
		EditText editMessage = (EditText) findViewById(R.id.edit_im_message);
		String msg = editMessage.getEditableText().toString();

		if (myTeamHint == null)
			return;
		if (Validator.isEmpty(msg))
			return;

		IMessage message = new IMessage(tempId++, matchId, myTeamHint, msg);
		GroundIMClient.sendMessage(message);
		
		messageList.add(message);
		messageAdapter.notifyDataSetChanged();
		scrollDown(null);
		editMessage.setText("");
	}

	@Override
	public void onPause() {
		super.onPause();
		GroundIMClient.close();
	}

}
