package anb.ground.utils;

import org.apache.http.HttpStatus;

import anb.ground.R;
import anb.ground.app.GlobalApplication;
import anb.ground.models.Match;
import anb.ground.models.PushParams;
import anb.ground.models.TeamHint;
import anb.ground.protocols.DefaultResponse;
import anb.ground.protocols.DefaultResponse.StatusCode;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ToastUtils {
	public static void show(int src) {
		show(GlobalApplication.getInstance().getResources().getString(src));
	}

	public static void show(String msg) {
		Toast.makeText(GlobalApplication.getInstance().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
	}

	public static void showServerErrorMessage(DefaultResponse response) {
		String msg;
		if (response.getHttpStatus() != HttpStatus.SC_OK) {
			StatusCode statusCode = response.getStatusCode();
			switch (statusCode) {
			case NETWORK_ERROR:
				msg = "네트워크 연결에 문제가 발생했습니다\n잠시 후 다시 시도해주세요";
				break;
			default:
				msg = response.getMsg();
				break;
			}
		} else {
			StatusCode statusCode = response.getStatusCode();
			switch (statusCode) {
			case INVALID_PARAMETERS:
				msg = "잘못된 요청입니다";
				break;
			case INVALID_SESSION_KEY:
				msg = "세션이 종료되어 로그아웃됩니다";
				break;
			case EMAIL_NOT_EXIST:
				msg = "등록되지 않은 이메일입니다";
				break;
			case DUPLICATED_EMAIL:
				msg = "이미 등록된 이메일 주소입니다";
				break;
			case WRONG_PASSWORD:
				msg = "잘못된 비밀번호입니다";
				break;
			case WRONG_EMAIL:
				msg = "이메일 주소가 맞지 않습니다";
				break;
			case ERROR:
				msg = String.format("[%s] %s", statusCode.toString(), response.getMsg());
				break;
			default:
				msg = String.format("[%s] %s", statusCode.toString(), response.getMsg());
				break;
			}
		}
		Toast.makeText(GlobalApplication.getInstance().getApplicationContext(), msg, Toast.LENGTH_LONG).show();
	}

	public static void showPushMessage(final Context context, final PushParams params) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.toast_push_message_layout, null, false);
		final Toast toast = new Toast(context);

		ImageView imageView = (ImageView) view.findViewById(R.id.image_toast_push_message_image);
		TextView textName = (TextView) view.findViewById(R.id.text_toast_push_message_name);
		TextView textMsg = (TextView) view.findViewById(R.id.text_toast_push_message_content);
		TextView buttonConfirm = (TextView) view.findViewById(R.id.button_toast_push_message_confirm);
		TextView buttonClose = (TextView) view.findViewById(R.id.button_toast_push_message_close);

		ImageUtils.getThumnailImageCircular(imageView, params.getTeamImageUrl());
		textName.setText(params.getTeamName());
		textMsg.setText(params.getMsg());
		buttonConfirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				toast.cancel();

				Intent intent = new Intent(context, params.getTargetClass());
				intent.putExtra(TeamHint.EXTRA_TEAM_HINT, params.getTeamHint());
				intent.putExtra(Match.EXTRA_MATCH_ID, params.getMatchId());
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intent);
			}

		});
		buttonClose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				toast.cancel();
			}

		});

		toast.setView(view);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}
}
