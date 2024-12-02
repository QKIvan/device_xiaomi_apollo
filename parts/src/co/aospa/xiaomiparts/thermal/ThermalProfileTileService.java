/*
 * Copyright (C) 2024 Paranoid Android
 *
 * SPDX-License-Identifier: Apache-2.0
 */

 package co.aospa.xiaomiparts.thermal;

import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

import co.aospa.xiaomiparts.utils.FileUtils;

public class ThermalProfileTileService extends TileService {

    private static final String THERMAL_PROFILE_PATH = "/sys/class/thermal/thermal_message/sconfig";
    private static final int THERMAL_PROFILE_DEFAULT = 0;
    private static final int THERMAL_PROFILE_MGAME = 19;

    private void updateUI(int profile) {
        Tile tile = getQsTile();
        tile.setLabel(getString(com.android.settingslib.collapsingtoolbar.R.string.thermalprofile_title));

        String subtitle;
        if (profile == THERMAL_PROFILE_DEFAULT) {
            subtitle = getString(com.android.settingslib.collapsingtoolbar.R.string.thermalprofile_default);
        } else if (profile == THERMAL_PROFILE_MGAME) {
            subtitle = getString(com.android.settingslib.collapsingtoolbar.R.string.thermalprofile_game);
        } else {
            subtitle = getString(com.android.settingslib.collapsingtoolbar.R.string.thermalprofile_unknown);
        }
        tile.setSubtitle(subtitle);
        tile.setState(Tile.STATE_ACTIVE);
        tile.updateTile();
    }

    @Override
    public void onStartListening() {
        super.onStartListening();
        int currentProfile = FileUtils.readLineInt(THERMAL_PROFILE_PATH);
        updateUI(currentProfile);
    }

    @Override
    public void onStopListening() {
        super.onStopListening();
    }

    @Override
    public void onClick() {
        super.onClick();
        int currentThermalProfile = FileUtils.readLineInt(THERMAL_PROFILE_PATH);
        int newThermalProfile = (currentThermalProfile == THERMAL_PROFILE_DEFAULT)
                                ? THERMAL_PROFILE_MGAME
                                : THERMAL_PROFILE_DEFAULT;
        FileUtils.writeLine(THERMAL_PROFILE_PATH, Integer.toString(newThermalProfile));
        updateUI(newThermalProfile);
    }
}
