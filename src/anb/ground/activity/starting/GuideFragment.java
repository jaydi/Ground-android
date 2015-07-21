package anb.ground.activity.starting;

import anb.ground.R;
import anb.ground.activity.main.TrackedFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class GuideFragment extends TrackedFragment {
	private int position;
	
	public void setPosition(int position) {
		this.position = position;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle data) {
		View view = inflater.inflate(R.layout.fragment_guide, container, false);
		ImageView image = (ImageView) view.findViewById(R.id.image_guide_guide);

		switch (position) {
		case 1:
			image.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.user_guide1));
			break;
		case 2:
			image.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.user_guide2));
			break;
		case 3:
			image.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.user_guide3));
			break;
		case 4:
			image.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.user_guide4));
			view.findViewById(R.id.image_guide_close).setVisibility(View.VISIBLE);
			break;
		}

		return view;
	}
}
