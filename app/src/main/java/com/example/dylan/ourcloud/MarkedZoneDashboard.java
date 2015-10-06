package com.example.dylan.ourcloud;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;

import java.util.List;

/**
 * Created by dylan on 10/5/15.
 */
public class MarkedZoneDashboard extends NetworkListenerActivity implements View.OnClickListener,MarkedZoneController.Callback {

    private TextView noMarkedZones;

    public void onCreate( Bundle savedInstance ) {
        super.onCreate(savedInstance);
        setContentView(R.layout.mark_zone_dash);

        Toolbar toolbar  = ( Toolbar ) findViewById( R.id.toolbar );
        ImageView toolbarBack = (ImageView) toolbar.findViewById(R.id.toolbarBackButton);
        FloatingActionButton markZoneButton = ( FloatingActionButton ) findViewById( R.id.markZoneButton );
        noMarkedZones = (TextView) findViewById( R.id.noMarkedZones );

        markZoneButton.setOnClickListener( this );
        toolbarBack.setOnClickListener( this );
    }

    @Override
    public void onStart() {
        super.onStart();
        MarkedZoneController mZoneManager = new MarkedZoneController( this );
        mZoneManager.getMarkedZones( LocalUser.getInstance( this ).getItem( LocalUserDBHelper.user_id_col ) );
        /**
         * Use markZoneManager to query for all zones this user has marked
         * If 0, show text saying "No zones marked yet, mark this one!"
         * Else, show cards for all marked zones
         * If current zone is in list of returned marked zones, do not show the FAB to mark this zone
         */
    }

    public void getMarkedZones( List<Zone> zones ) {
        /**
         * If size is 0, show no makred zone test
         * If LocalUser.currentZone() is inside of this list of zones, do not show the FAB
         */

        if ( zones.size() == 0 ) {
            noMarkedZones.setVisibility( View.VISIBLE );
        } else {
            //handle zone cards
        }

    }

    @Override
    public void onClick( View v ) {

        switch ( v.getId() ) {
            case R.id.toolbarBackButton :
                this.finish();
                break;
            case R.id.markZoneButton :
                //use markedZoneController to mark this zone
                //show dialog asking if the user would like to mark zone first
                break;
        }

    }

}
