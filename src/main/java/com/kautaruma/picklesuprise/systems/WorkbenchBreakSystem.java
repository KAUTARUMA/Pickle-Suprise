package com.kautaruma.picklesuprise.systems;

import java.util.List;

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
import com.hypixel.hytale.server.core.event.events.ecs.BreakBlockEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.kautaruma.picklesuprise.PickleSuprise;

public class WorkbenchBreakSystem extends EntityEventSystem<EntityStore, BreakBlockEvent> {
    public WorkbenchBreakSystem() {
        super(BreakBlockEvent.class);
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
        @Nonnull final BreakBlockEvent event) {
        Ref<EntityStore> playerRef = archetypeChunk.getReferenceTo(index);
        Player player = store.getComponent(playerRef, Player.getComponentType());

        if (player == null || player.getWorld() == null || event.getBlockType() == null) {
            return;
        }

        String blockID = event.getBlockType().getId();
        
        if (!blockID.equals("Bench_WorkBench")) {
            return;
        }
        
        List<Vector3i> list = WorkbenchPlaceSystem.workbenchLocations.get(player.getWorld().getName());

        if (list != null) {
            list.remove(event.getTargetBlock());
            
            if (list.isEmpty()) {
                WorkbenchPlaceSystem.workbenchLocations.remove(player.getWorld().getName());
            }
        }

        PickleSuprise.saveConfig();
    }
}
