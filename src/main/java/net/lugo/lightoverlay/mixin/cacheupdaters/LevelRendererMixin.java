package net.lugo.lightoverlay.mixin.cacheupdaters;

import net.lugo.lightoverlay.OverlayHandler;
import net.minecraft.client.renderer.extract.LevelExtractor;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelExtractor.class)
public class LevelRendererMixin {
    @Inject(method = "blockChanged", at = @At("HEAD"))
    private void onBlockChanged(BlockPos pos, int updateFlags, CallbackInfo ci) {
        OverlayHandler.refresh(pos);
    }
}
