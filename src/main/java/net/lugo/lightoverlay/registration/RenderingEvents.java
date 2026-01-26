package net.lugo.lightoverlay.registration;

import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.lugo.lightoverlay.OverlayManager;
import net.minecraft.util.profiling.Profiler;
import net.minecraft.util.profiling.ProfilerFiller;

public class RenderingEvents {
    public static void register() {
        WorldRenderEvents.END_MAIN.register(context -> {
            final ProfilerFiller profilerFiller = Profiler.get();
            profilerFiller.push("lightoverlay");
            profilerFiller.push("render");
            OverlayManager.render(context);
            profilerFiller.pop();
            profilerFiller.push("draw");
            OverlayManager.draw();
            profilerFiller.pop();
            profilerFiller.pop();
        });
    }
}
