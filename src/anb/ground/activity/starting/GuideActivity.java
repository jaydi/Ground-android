package anb.ground.activity.starting;

import anb.ground.R;
import anb.ground.activity.main.TrackedFragmentActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.View;

public class GuideActivity extends TrackedFragmentActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);
		
		ViewPager pager = (ViewPager) findViewById(R.id.view_pager_guide_container);
		FragmentManager fm = getSupportFragmentManager();
		PagerAdapter adapter = new PagerAdapter(fm);
		
		pager.setAdapter(adapter);
		pager.setCurrentItem(0);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.guide, menu);
		return true;
	}
	
	public void close(View view) {
		Intent intent = new Intent(this, StartingActivity.class);
		startActivity(intent);
		finish();
	}

	public class PagerAdapter extends FragmentPagerAdapter {
		
		public PagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int arg0) {
			GuideFragment fragment = new GuideFragment();
			fragment.setPosition(arg0 + 1);
			
			return fragment;
		}

		@Override
		public int getCount() {
			return 4;
		}

	}
}
