package cz.martinbarton.decid;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{

	private static final long DRAWER_CLOSE_DELAY_MS = 250;
	private static final String NAV_ITEM_ID = "navItemId";

	private final Handler mDrawerActionHandler = new Handler();
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	private int mNavItemId;

	private int counter = 1;
	private TextView badge;


	@Override
	protected void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		// load saved navigation state if present
		if(null == savedInstanceState)
		{
			mNavItemId = R.id.drawer_item_1;
		}
		else
		{
			mNavItemId = savedInstanceState.getInt(NAV_ITEM_ID);
		}

		// listen for navigation events
		NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
		navigationView.setNavigationItemSelectedListener(this);

		// select the correct nav menu item
		navigationView.getMenu().findItem(mNavItemId).setChecked(true);

		// set up the hamburger icon to open and close the drawer
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open, R.string.close);
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		mDrawerToggle.syncState();

		navigate(mNavItemId);

		Button btn = (Button) findViewById(R.id.button);
		badge = (TextView) navigationView.getMenu().getItem(1).getActionView().findViewById(R.id.drawer_item_badge);

		btn.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				badge.setText("" + counter);
				counter++;
			}
		});
	}


	private void navigate(final int itemId)
	{
		// perform the actual navigation logic, updating the main content fragment etc
	}


	@Override
	public boolean onNavigationItemSelected(final MenuItem menuItem)
	{
		// update highlighted item in the navigation menu
		menuItem.setChecked(true);
		mNavItemId = menuItem.getItemId();

		// allow some time after closing the drawer before performing real navigation
		// so the user can see what is happening
		mDrawerLayout.closeDrawer(GravityCompat.START);
		mDrawerActionHandler.postDelayed(new Runnable()
		{
			@Override
			public void run()
			{
				navigate(menuItem.getItemId());
			}
		}, DRAWER_CLOSE_DELAY_MS);
		return true;
	}


	@Override
	public void onConfigurationChanged(final Configuration newConfig)
	{
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}


	@Override
	public boolean onOptionsItemSelected(final MenuItem item)
	{
		if(item.getItemId() == android.support.v7.appcompat.R.id.home)
		{
			return mDrawerToggle.onOptionsItemSelected(item);
		}
		return super.onOptionsItemSelected(item);
	}


	@Override
	public void onBackPressed()
	{
		if(mDrawerLayout.isDrawerOpen(GravityCompat.START))
		{
			mDrawerLayout.closeDrawer(GravityCompat.START);
		}
		else
		{
			super.onBackPressed();
		}
	}


	@Override
	protected void onSaveInstanceState(final Bundle outState)
	{
		super.onSaveInstanceState(outState);
		outState.putInt(NAV_ITEM_ID, mNavItemId);
	}
}
