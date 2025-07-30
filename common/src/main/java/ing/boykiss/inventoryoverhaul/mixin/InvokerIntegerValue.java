package ing.boykiss.inventoryoverhaul.mixin;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.function.BiConsumer;

@Mixin(GameRules.IntegerValue.class)
public interface InvokerIntegerValue {
    @Invoker("create")
    static GameRules.Type<GameRules.IntegerValue> invokeCreate(
            int i, int j, int k, BiConsumer<MinecraftServer, GameRules.IntegerValue> biConsumer
    ) {
        throw new AssertionError();
    }
}
