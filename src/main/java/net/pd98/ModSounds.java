package net.pd98;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;

public class ModSounds {
    public static final Identifier FAIL_ID = Identifier.fromNamespaceAndPath(LetsGetMending.MOD_ID, "fail");
    public static final Identifier SUCCESS_ID = Identifier.fromNamespaceAndPath(LetsGetMending.MOD_ID, "success");

    public static final SoundEvent FAIL_TRADE = Registry.register(
            BuiltInRegistries.SOUND_EVENT,
            FAIL_ID,
            SoundEvent.createVariableRangeEvent(FAIL_ID)
    );
    public static final SoundEvent SUCCESS_TRADE = Registry.register(
            BuiltInRegistries.SOUND_EVENT,
            SUCCESS_ID,
            SoundEvent.createVariableRangeEvent(SUCCESS_ID)
    );

    public static void init() {}

}
