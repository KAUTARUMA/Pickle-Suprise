package com.kautaruma.picklesuprise.systems;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.system.tick.TickingSystem;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.protocol.SoundCategory;
import com.hypixel.hytale.server.core.asset.type.soundevent.config.SoundEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.SoundUtil;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

public class RickVoiceSystem extends TickingSystem<EntityStore> {

    static final float MIN_VALUE = 3.0f;
    static final float MAX_VALUE = 6.0f;

    private final Map<Vector3i, Float> positionTimers = new HashMap<>();

    public RickVoiceSystem() {
        super();
    }

    private float randomDelay() {
        return (float) (MIN_VALUE + Math.random() * (MAX_VALUE - MIN_VALUE));
    }

    @Override
    public void tick(float dt, int systemIndex, @Nonnull Store<EntityStore> store) {
        World world = store.getExternalData().getWorld();
        List<Vector3i> positions = WorkbenchPlaceSystem.workbenchLocations.get(world.getName());

        if (positions == null || positions.isEmpty()) return;

        for (Vector3i pos : positions) {
            float timer = positionTimers.getOrDefault(pos, randomDelay());
            timer -= dt;

            if (timer > 0.0f) {
                positionTimers.put(pos, timer);
                continue;
            } else {
                positionTimers.put(pos, randomDelay());
            }

            Vector3d soundPos = new Vector3d(pos);
            
            switch (world.getBlockRotationIndex(pos.x, pos.y, pos.z)) {
                case 1:
                    soundPos.z += 1;
                    break;
                case 2:
                    soundPos.x += 1;
                    break;
            }

            boolean playCloseSound = false;

            for (PlayerRef playerRef : world.getPlayerRefs()) {
                double dist = playerRef.getTransform().getPosition().distanceSquaredTo(soundPos);

                if (dist < 10.0f) {
                    playCloseSound = true;
                    break;
                }
            }

            int index = SoundEvent.getAssetMap().getIndex(playCloseSound ? "SFX_Rick_InsideSomething" : "SFX_Rick_FarAway");

            world.execute(() -> {
                SoundUtil.playSoundEvent3d(
                    index,
                    SoundCategory.Ambient,
                    new Vector3d(soundPos),
                    store
                );
            });
        }

        positionTimers.keySet().retainAll(positions);
    }
}
