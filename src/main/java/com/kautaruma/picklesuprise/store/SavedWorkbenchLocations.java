package com.kautaruma.picklesuprise.store;

import java.util.HashMap;
import java.util.Map;

import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.codecs.array.ArrayCodec;
import com.hypixel.hytale.codec.codecs.map.MapCodec;
import com.hypixel.hytale.math.vector.Vector3i;

public class SavedWorkbenchLocations {

    public Map<String, Vector3i[]> WorkbenchLocations;

    public SavedWorkbenchLocations() {
        
    }

    public static final BuilderCodec<SavedWorkbenchLocations> CODEC;

    static {
        CODEC = BuilderCodec
            .builder(SavedWorkbenchLocations.class, SavedWorkbenchLocations::new)
            .append(
                new KeyedCodec<>("WorkbenchLocations", new MapCodec<>(new ArrayCodec<>(Vector3i.CODEC, Vector3i[]::new), HashMap::new)),
                (obj, map) -> obj.WorkbenchLocations = map,
                obj -> obj.WorkbenchLocations
            )
            .documentation("Locations of every workbench.")
            .add()
            .build();
    }
}

