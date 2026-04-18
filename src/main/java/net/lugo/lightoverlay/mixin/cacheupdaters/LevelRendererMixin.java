package net.lugo.lightoverlay.mixin.cacheupdaters;

import net.lugo.lightoverlay.OverlayHandler;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {
    @Inject(method = "blockChanged", at = @At("HEAD"))
    private void onBlockChanged(BlockGetter level, BlockPos pos, BlockState old, BlockState current, int updateFlags, CallbackInfo ci) {
        OverlayHandler.refresh(pos);
    }
}
