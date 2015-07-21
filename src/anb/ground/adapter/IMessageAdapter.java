package anb.ground.adapter;

import java.util.List;

import anb.ground.R;
import anb.ground.models.IMessage;
import anb.ground.models.LocalUser;
import anb.ground.utils.ImageUtils;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class IMessageAdapter extends BaseAdapter {
	private Context context;
	private List<IMessage> messageList;

	public IMessageAdapter(Context context, List<IMessage> messageList) {
		super();
		this.context = context;
		this.messageList = messageList;
	}

	@Override
	public int getCount() {
		return messageList.size();
	}

	@Override
	public Object getItem(int position) {
		return messageList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return messageList.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		IMessage message = messageList.get(position);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view;

		if (LocalUser.getInstance().managing(message.getTeamId()))
			view = inflater.inflate(R.layout.list_item_imessage_mine_layout, parent, false);
		else {
			view = inflater.inflate(R.layout.list_item_imessage_layout, parent, false);

			TextView textName = (TextView) view.findViewById(R.id.text_list_item_imessage_name);
			ImageView imageProfile = (ImageView) view.findViewById(R.id.image_list_item_imessage_profile);

			textName.setText(message.getTeamName());
			ImageUtils.getThumnailImageCircular(imageProfile, message.getTeamImageUrl());
		}
		
		TextView textMessage = (TextView) view.findViewById(R.id.text_list_item_imessage_message);
		textMessage.setText(message.getMsg());

		return view;
	}

}
