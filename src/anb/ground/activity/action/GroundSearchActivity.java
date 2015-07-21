package anb.ground.activity.action;

import java.util.ArrayList;
import java.util.List;

import anb.ground.R;
import anb.ground.activity.main.TrackedFragmentActivity;
import anb.ground.adapter.GroundAdapter;
import anb.ground.dialogs.GroundInfoDialog;
import anb.ground.models.Ground;
import anb.ground.protocols.SearchGroundResponse;
import anb.ground.server.GroundClient;
import anb.ground.server.ResponseHandler;
import anb.ground.utils.Validator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

public class GroundSearchActivity extends TrackedFragmentActivity implements GroundInfoDialog.GroundInfoDialogListener {
	public static final String EXTRA_GROUND = "anb.ground.extra.ground";

	private List<Ground> reservedGroundList = new ArrayList<Ground>();
	private List<Ground> searchedGroundList = new ArrayList<Ground>();

	private ListView reservedGrounds;
	private ListView searchedGrounds;

	private GroundAdapter reservedGroundAdapter;
	private GroundAdapter searchedGroundAdapter;

	private Ground ground;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_ground);

		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setLogo(getResources().getDrawable(R.drawable.menu_back));

		reservedGrounds = (ListView) findViewById(R.id.list_reserved_ground);
		searchedGrounds = (ListView) findViewById(R.id.list_searched_ground);

		reservedGroundAdapter = new GroundAdapter(this, reservedGroundList);
		searchedGroundAdapter = new GroundAdapter(this, searchedGroundList);

		reservedGrounds.setAdapter(reservedGroundAdapter);
		searchedGrounds.setAdapter(searchedGroundAdapter);

		setOnItemClickListener();

		EditText textSearchKeyword = (EditText) findViewById(R.id.edit_text_ground_search);
		setSearchKeywordListener(textSearchKeyword);

		if (reservedGroundList.size() > 0) {
			searchedGrounds.setVisibility(View.GONE);
			reservedGrounds.setVisibility(View.VISIBLE);
		}
	}

	private void setSearchKeywordListener(EditText textSearchKeyword) {
		textSearchKeyword.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (filterLength(s)) {
					reservedGrounds.setVisibility(View.GONE);
					searchedGrounds.setVisibility(View.VISIBLE);
					GroundClient.searchGround(new ResponseHandler<SearchGroundResponse>() {

						@Override
						public void onReceiveOK(SearchGroundResponse response) {
							searchedGroundList.clear();
							searchedGroundList.addAll(response.getGroundList());
							searchedGroundList.add(getNewGroundOption());
							searchedGroundAdapter.notifyDataSetChanged();
						}

					}, s.toString());
				} else {
					if (reservedGroundList.size() > 0)
						reservedGrounds.setVisibility(View.VISIBLE);
					searchedGrounds.setVisibility(View.GONE);
				}
			}

			private boolean filterLength(CharSequence s) {
				return s.length() >= Validator.LENGTH_ENOUGH_KOR;
			}

		});
	}

	private Ground getNewGroundOption() {
		Ground ground = new Ground();
		ground.setId(0);
		ground.setName(getResources().getString(R.string.new_ground));
		ground.setAddress(getResources().getString(R.string.new_ground_description));

		return ground;
	}

	private void setOnItemClickListener() {
		reservedGrounds.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ground = reservedGroundList.get(position);
				showGroundInfoDialog(ground);
			}

		});

		searchedGrounds.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (id == 0)
					getNewGround();
				else {
					ground = searchedGroundList.get(position);
					showGroundInfoDialog(ground);
				}
			}

		});
	}

	private void showGroundInfoDialog(Ground ground) {
		InputMethodManager iManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
		iManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);

		GroundInfoDialog dialog = new GroundInfoDialog();
		dialog.setGround(ground);
		dialog.show(getSupportFragmentManager(), "groundInfo");
	}

	@Override
	public void onDialogPositiveClick(DialogFragment dialog) {
		dialog.getDialog().cancel();

		Intent returnIntent = new Intent();
		returnIntent.putExtra(EXTRA_GROUND, ground);
		setResult(RESULT_OK, returnIntent);
		finish();
	}

	@Override
	public void onDialogNegativeClick(DialogFragment dialog) {
		dialog.getDialog().cancel();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.ground_search, menu);
		return true;
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

	public void getNewGround() {
		Intent intent = new Intent(this, MapPointActivity.class);
		intent.putExtra("requestCode", MapPointActivity.REQUEST_NEW_GROUND);
		startActivityForResult(intent, MapPointActivity.REQUEST_NEW_GROUND);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK)
			if (requestCode == MapPointActivity.REQUEST_NEW_GROUND) {
				Intent returnIntent = new Intent();
				returnIntent.putExtra(EXTRA_GROUND, data.getParcelableExtra(MapPointActivity.EXTRA_NEW_GROUND));
				setResult(RESULT_OK, returnIntent);
				finish();
			}
	}

}
