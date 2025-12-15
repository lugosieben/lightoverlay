package net.lugo.lightoverlay.mixin.cacheupdaters;

import net.lugo.lightoverlay.util.OverlayCache;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class WorldRendererMixin {
    @Inject(method = "blockChanged", at = @At("HEAD"))
    private void onUpdateBlock(BlockGetter world, BlockPos pos, BlockState oldState, BlockState newState, int flags, CallbackInfo ci) {
        OverlayCache.clearFromBlockPos(pos);
    }
}
