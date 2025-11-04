package net.lugo.lightoverlay.mixin.cacheupdaters;

import net.lugo.lightoverlay.util.OverlayCache;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
    @Inject(method = "updateBlock", at = @At("HEAD"))
    private void onUpdateBlock(BlockView world, BlockPos pos, BlockState oldState, BlockState newState, int flags, CallbackInfo ci) {
        OverlayCache.clearFromBlockPos(pos);
    }
}
