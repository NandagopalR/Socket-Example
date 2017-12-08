package com.nanda.socketexample.activity;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nanda.socketexample.R;
import com.nanda.socketexample.app.AppConstants;
import com.nanda.socketexample.app.AppController;
import com.nanda.socketexample.base.BaseActivity;
import com.nanda.socketexample.helper.LocationHelper;
import com.nanda.socketexample.socket.SocketManager;
import com.nanda.socketexample.socket.SocketManagerListener;
import com.nanda.socketexample.utils.LoggerUtils;
import com.nanda.socketexample.utils.NetworkUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements SocketManagerListener, OnMapReadyCallback, LocationHelper.OnLocationCompleteListener {

    private SupportMapFragment mapFragment;

    private SocketManager socketManager;
    private LocationHelper locationHelper;
    private Location mLastLocation;
    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                int hasGetLocationPermission = ActivityCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION);
                if (hasGetLocationPermission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, AppConstants.REQUEST_CODE_ASK_PERMISSIONS);
                } else {
                    locationHelper = new LocationHelper(MainActivity.this, this);
                }
            } else {
                locationHelper = new LocationHelper(MainActivity.this, this);
            }
        } else {
            NetworkUtils.turnOnGps(this);
            locationHelper = new LocationHelper(MainActivity.this, this);
        }

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
        socketManager = AppController.getInstance().getSocketManager();
        socketManager.connectToSocket(this);
        socketManager.setListener(this);

    }

    @OnClick(R.id.fab_send)
    public void onSendClicked() {
        startActivity(ContactsActivity.getCallingIntent(this, false));
    }

    @OnClick(R.id.fab_lookup)
    public void onLookupClicked() {
        startActivity(ContactsActivity.getCallingIntent(this, true));
    }

    @Override
    public void onConnect() {
        LoggerUtils.e("Connection", "Success");
    }

    @Override
    public void onUserList(String userList) {
        LoggerUtils.e("User List", userList);
    }

    @Override
    public void onSocketFailed(String message) {
        LoggerUtils.e("Connection", message);
    }

    @Override
    public void onLoginWithSocket() {
        LoggerUtils.e("Login", "Success");
        socketManager.emitMessage(AppConstants.CONNECT_TO_USER, "nanda");
        socketManager.fetchUserList();
    }

    @Override
    public void onNewUser(Object... args) {
        LoggerUtils.e("Connection", "new User");
    }

    @Override
    public void onSocketError(int code) {
        LoggerUtils.e("Socket Error", " - " + code);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (socketManager != null) {
            socketManager.closeAndDisconnectSocket();
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            NetworkUtils.turnOffGps(this);
        }
    }

    @Override
    public void onConnected(Location location) {
        if (location != null) {
            mLastLocation = location;
            if (googleMap != null && mLastLocation != null) {
                mapFragment.getMapAsync(this);
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        this.googleMap.getUiSettings().setRotateGesturesEnabled(false);
        this.googleMap.getUiSettings().setZoomControlsEnabled(false);
        this.googleMap.getUiSettings().setMapToolbarEnabled(false);
        this.googleMap.getUiSettings().setMyLocationButtonEnabled(true);

        if (mLastLocation != null) {
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()))
                    .zoom(AppConstants.MAP_MAX_ZOOM)
                    .build();
            this.googleMap.addMarker(new MarkerOptions().position(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude())).title("Marker"));
            this.googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    @Override
    public void getLocationUpdate(Location location) {
        mLastLocation = location;

        if (googleMap != null && mLastLocation != null) {
            mapFragment.getMapAsync(this);
        }

    }

    @Override
    public void getGoogleApiClient(GoogleApiClient googleApiClient) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        locationHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onError(ConnectionResult connectionResult, Status status, String error) {
        if (connectionResult != null) {
            if (connectionResult.hasResolution()) {
                try {
                    connectionResult.startResolutionForResult(this,
                            LocationHelper.CONNECTION_FAILURE_RESOLUTION_REQUEST);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            }
        } else if (status != null) {
            try {
                status.startResolutionForResult(this, LocationHelper.REQUEST_CHECK_SETTINGS);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            finish();
        }
    }

}
