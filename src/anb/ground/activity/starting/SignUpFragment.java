package anb.ground.activity.starting;

import anb.ground.R;
import anb.ground.activity.main.TrackedFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SignUpFragment extends TrackedFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_sign_up, null);
		return view;
	}
}
