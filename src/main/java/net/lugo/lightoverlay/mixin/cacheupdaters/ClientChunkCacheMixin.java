package net.lugo.lightoverlay.mixin.cacheupdaters;

import net.lugo.lightoverlay.OverlayHandler;
import net.minecraft.client.multiplayer.ClientChunkCache;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.LightLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientChunkCache.class)
public class ClientChunkCacheMixin {
    @Inject(method = "updateViewRadius", at = @At("HEAD"))
    private void onUpdateViewRadius(int viewRange, CallbackInfo ci) {
        OverlayHandler.clearAll();
    }

    @Inject(method = "onLightUpdate", at = @At("HEAD"))
    private void onLightUpdate(LightLayer layer, SectionPos pos, CallbackInfo ci) {
        OverlayHandler.refresh(pos);
    }
}
