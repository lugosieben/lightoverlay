package net.lugo.lightoverlay.mixin;

import net.lugo.lightoverlay.OverlayManager;
import net.minecraft.client.render.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
    @Inject(method = "render", at = @At("RETURN"))
    private void afterRender(CallbackInfo ci) {
        OverlayManager.renderEnd();
    }
}
