package com.example.leoni.telephoneinfos;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements OnMapReadyCallback {
    /**
     * A placeholder fragment containing a simple view.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    MapView mMapView;
    GoogleMap googleMap;
    DataManager data;

    @TargetApi(Build.VERSION_CODES.N)
    public PlaceholderFragment() {
        //data = new DataManager(getActivity());
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment newInstance(int sectionNumber) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        data = new DataManager(container.getContext());
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.section_label);

        if(getArguments().getInt(ARG_SECTION_NUMBER) == 1) {

                /*List<String> values = new ArrayList<>();
                values.add(data.getSerial());
                values.add(data.getCountryCode());
                values.add(data.getOperatorNumber());
                values.add(Integer.toString(data.getDataNetworkType()));

                ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(container.getContext(),R.layout.list_item,R.id.list_item_textView,values);

                ListView listview = (ListView) rootView.findViewById(R.id.listView);
                listview.setAdapter(listAdapter);*/

            TextView l1 = (TextView) rootView.findViewById(R.id.label1);

            l1.setText("Serial number:  " + data.getSerial());
            TextView l2 = (TextView) rootView.findViewById(R.id.label2);
            l2.setText("CountryCode:  " + data.getCountryCode());
            TextView l3 = (TextView) rootView.findViewById(R.id.label3);
            l3.setText("Sim operator name:  " + data.getOperatorNumber());
            TextView l4 = (TextView) rootView.findViewById(R.id.label4);
            l4.setText("Network type:  " + data.getDataNetworkType());
            TextView l5 = (TextView) rootView.findViewById(R.id.label5);
            l5.setText("Network type for voice calls:  " + data.getVoiceCallType());

            //ListView
            ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(container.getContext(),R.layout.list_item,R.id.list_item_textView,data.getCellIds());

            ListView listview = (ListView) rootView.findViewById(R.id.listView);
            listview.setAdapter(listAdapter);
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    String value = (String) parent.getItemAtPosition(position);
                    final Dialog dialog = new Dialog(view.getContext());
                    dialog.setContentView(R.layout.custom_dialog);
                    dialog.setTitle("Cell Info");
                    ListView info = (ListView) dialog.findViewById(R.id.List_Content);
                    ArrayAdapter<String> adapter = new ArrayAdapter(view.getContext(), R.layout.cellinfo_view, data.getCellInfos().get(position));
                    info.setAdapter(adapter);
                    dialog.show();

                }
            });

        }else if (getArguments().getInt(ARG_SECTION_NUMBER) == 2){
            TextView l1 = (TextView) rootView.findViewById(R.id.label1);
            l1.clearComposingText();
            l1.setText("Type \n" + data.getType());
            TextView l2 = (TextView) rootView.findViewById(R.id.label2);
            l2.setText("Roaming \n" + data.getWifi());
            TextView l3 = (TextView) rootView.findViewById(R.id.label3);
            l3.setText("Active state \n" + data.getActiveState());
            TextView l4 = (TextView) rootView.findViewById(R.id.label4);
            l3.setText("Downstream bandwidth \n" + data.getBandwidth_down());
            TextView l5 = (TextView) rootView.findViewById(R.id.label5);
            l4.setText("Upstream bandwidth \n" + data.getBandwidth_up());

            TextView l6 = (TextView) rootView.findViewById(R.id.label6);
            l6.setText("Interface name: " + data.getInterfaceName());

        }else if(getArguments().getInt(ARG_SECTION_NUMBER) == 3) {

            mMapView = (MapView) rootView.findViewById(R.id.mapView);
            //savedInstanceState is null
            if (mMapView != null) {
                mMapView.onCreate(new Bundle());


                mMapView.onResume(); // needed to get the map to display immediately

                try {
                    MapsInitializer.initialize(getActivity().getApplicationContext());
                } catch (Exception e) {
                    e.printStackTrace();
                }


                mMapView.getMapAsync(this);
            }
        }

        //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap mMap) {
        googleMap = mMap;


        // For showing a move to my location button
        //googleMap.setMyLocationEnabled(true);

        // For dropping a marker at a point on the Map
        LatLng sydney = new LatLng(-34, 151);
        googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description"));

        // For zooming automatically to the location of the marker
        CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }


    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }
}
