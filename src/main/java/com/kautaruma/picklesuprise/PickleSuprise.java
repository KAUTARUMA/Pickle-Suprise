package com.kautaruma.picklesuprise;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.util.Config;
import com.kautaruma.picklesuprise.store.SavedWorkbenchLocations;
import com.kautaruma.picklesuprise.systems.RickVoiceSystem;
import com.kautaruma.picklesuprise.systems.WorkbenchBreakSystem;
import com.kautaruma.picklesuprise.systems.WorkbenchPlaceSystem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class PickleSuprise extends JavaPlugin {
    public static HytaleLogger logger;
    public static Config<SavedWorkbenchLocations> config = null;

    public PickleSuprise(@NonNullDecl JavaPluginInit init) {
        super(init);
        logger = getLogger();
        config = withConfig(SavedWorkbenchLocations.CODEC);
    }

    @Override
    public void start() {
        getEntityStoreRegistry().registerSystem(new WorkbenchPlaceSystem());
        getEntityStoreRegistry().registerSystem(new WorkbenchBreakSystem());
        getEntityStoreRegistry().registerSystem(new RickVoiceSystem());

        WorkbenchPlaceSystem.workbenchLocations = new HashMap<>();
        if (config.get().WorkbenchLocations != null) {
            for (Map.Entry<String, Vector3i[]> entry : config.get().WorkbenchLocations.entrySet()) {
                WorkbenchPlaceSystem.workbenchLocations.put(
                    entry.getKey(), 
                    new ArrayList<>(Arrays.asList(entry.getValue()))
                );
            }
        }
    }

    public static void saveConfig() {
        config.get().WorkbenchLocations = new HashMap<>();

        if (WorkbenchPlaceSystem.workbenchLocations != null) {
            for (Map.Entry<String, List<Vector3i>> entry : WorkbenchPlaceSystem.workbenchLocations.entrySet()) {

                config.get().WorkbenchLocations.put(
                    entry.getKey(),
                    entry.getValue().toArray(new Vector3i[0])
                );
            }
        }

        config.save();
    }

    @Override
    public void shutdown() {}
}
