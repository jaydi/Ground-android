package anb.ground.receiver;

import anb.ground.R;
import anb.ground.activity.main.team.TeamMainActivity;
import anb.ground.app.GlobalApplication;
import anb.ground.models.IMessage;
import anb.ground.models.Match;
import anb.ground.models.PushParams;
import anb.ground.models.TeamHint;
import anb.ground.utils.StringProvider;
import anb.ground.utils.ToastUtils;
import anb.ground.utils.Validator;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

public class GCMBroadcastReceiver extends BroadcastReceiver {
	public static final String EXTRA_PUSH_TYPE = "anb.ground.extra.pushType";
	public static final String EXTRA_PUSH_KEY = "anb.ground.extra.pushKey";

	private Context context;

	@Override
	public void onReceive(Context context, Intent intent) {
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
		this.context = context;
		String messageType = gcm.getMessageType(intent);

		if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType) && GlobalApplication.pushSettings.isPushAllowed())
			pushMessage(getPushParams(intent));

		setResultCode(Activity.RESULT_OK);
		
		Log.i("pushedExtras : ", intent.getExtras().toString());
	}

	private PushParams getPushParams(Intent intent) {
		PushParams params = new PushParams();
		params.setPushKey(intent.getStringExtra(EXTRA_PUSH_KEY));
		params.setMatchId(Long.valueOf(intent.getStringExtra(Match.EXTRA_MATCH_ID)));
		params.setTeamId(Integer.valueOf(intent.getStringExtra(TeamHint.EXTRA_TEAM_ID)));
		params.setTeamName(intent.getStringExtra(TeamHint.EXTRA_TEAM_NAME));
		params.setTeamImageUrl(intent.getStringExtra(TeamHint.EXTRA_TEAM_IMAGE_URL));
		params.setMsg(intent.getStringExtra(IMessage.EXTRA_MESSAGE));
		params.setParams(intent.getStringExtra(PushParams.EXTRA_PARAMS));
		return params;
	}

	private void pushMessage(PushParams params) {
		if(!Validator.validatePushParams(params))
			return;
		
		notify(getContentIntent(params), params.getTeamName() + " : " + params.getMsg());
		
		if (GlobalApplication.pushSettings.isPushToastAllowed())
			ToastUtils.showPushMessage(context, params);
	}

	private PendingIntent getContentIntent(PushParams params) {
		Intent newIntent = new Intent(context, params.getTargetClass());
		newIntent.putExtra(Match.EXTRA_MATCH_ID, params.getMatchId());
		newIntent.putExtra(TeamMainActivity.EXTRA_TEAM_ACTIVITY_POSITION, 1);
		newIntent.putExtra(TeamHint.EXTRA_TEAM_HINT, params.getTeamHint());
		newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		return PendingIntent.getActivity(context, 0, newIntent, PendingIntent.FLAG_UPDATE_CURRENT);
	}

	private void notify(PendingIntent contentIntent, String msg) {
		NotificationCompat.Builder builder = new NotificationCompat.Builder(context).setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle(StringProvider.getString(R.string.ground_notification)).setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
				.setContentText(msg).setContentIntent(contentIntent).setAutoCancel(true);

		if (GlobalApplication.pushSettings.isPushVibrationAllowed())
			builder.setVibrate(new long[] { 301, 301, 301, 301 });

		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(0, builder.build());
	}

}
