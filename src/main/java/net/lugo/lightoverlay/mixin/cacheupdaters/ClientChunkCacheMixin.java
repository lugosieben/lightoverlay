package net.lugo.lightoverlay.mixin.cacheupdaters;

import net.lugo.lightoverlay.util.OverlayCache;
import net.minecraft.client.world.ClientChunkManager;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.world.LightType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientChunkManager.class)
public class ClientChunkCacheMixin {
    @Inject(method = "updateLoadDistance", at = @At("HEAD"))
    private void onUpdateLoadDistance(int distance, CallbackInfo ci) {
        OverlayCache.clearAll();
    }

    @Inject(method = "onLightUpdate", at = @At("HEAD"))
    private void onLightUpdate(LightType type, ChunkSectionPos pos, CallbackInfo ci) {
        OverlayCache.clear(pos);
    }
}
