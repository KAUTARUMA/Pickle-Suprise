package com.kautaruma.picklesuprise.systems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.EntityEventSystem;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.ecs.PlaceBlockEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.kautaruma.picklesuprise.PickleSuprise;

public class WorkbenchPlaceSystem extends EntityEventSystem<EntityStore, PlaceBlockEvent> {
    public static Map<String, List<Vector3i>> workbenchLocations = new HashMap<String, List<Vector3i>>();

    public WorkbenchPlaceSystem() {
        super(PlaceBlockEvent.class);
        
    }

    @Nullable
    @Override
    public Query<EntityStore> getQuery() {
        return PlayerRef.getComponentType();
    }

    @Override
    public void handle(
        final int index, 
        @Nonnull final ArchetypeChunk<EntityStore> archetypeChunk, 
        @Nonnull final Store<EntityStore> store, 
        @Nonnull final CommandBuffer<EntityStore> commandBuffer,
        @Nonnull final PlaceBlockEvent event) {
        Ref<EntityStore> playerRef = archetypeChunk.getReferenceTo(index);
        Player player = store.getComponent(playerRef, Player.getComponentType());

        if (player == null || player.getWorld() == null || event.getItemInHand() == null) {
            return;
        }

        String blockID = event.getItemInHand().getItemId();
        
        if (!blockID.equals("Bench_WorkBench")) {
            return;
        }
        
        workbenchLocations.computeIfAbsent(
            player.getWorld().getName(),
            w -> new ArrayList<>()
        );
                
        workbenchLocations.get(player.getWorld().getName()).add(event.getTargetBlock());

        PickleSuprise.saveConfig();
    }
}
