package net.pd98.mixin;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.pd98.LetsGetMending;
import net.pd98.ModSounds;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Villager.class)
public abstract class VillagerMixin {

	@Unique
	private boolean tradesRerolled = false;

	@Inject(method = "updateTrades", at = @At("TAIL"))
	private void onUpdateTrades(CallbackInfo ci) {
		Villager self = (Villager)(Object)this;
		if (self.getTradingPlayer() != null && self.getVillagerData().profession().is(VillagerProfession.LIBRARIAN)) {
			playSound(self);
		} else {
			tradesRerolled = true;
		}
	}

	@Inject(method = "startTrading", at = @At("HEAD"))
	private void onStartTrading(Player player, CallbackInfo ci) {
		LetsGetMending.LOGGER.info("startTrading");
		Villager self = (Villager)(Object)this;
		if (tradesRerolled && self.getVillagerData().profession().is(VillagerProfession.LIBRARIAN)) {
			playSound(self);
			tradesRerolled = false;
		}
	}

	@Inject(method = "resendOffersToTradingPlayer", at = @At("HEAD"))
	private void onResendOffersToTradingPlayer(CallbackInfo ci) {
		LetsGetMending.LOGGER.info("resendOffersToTradingPlayer");
		Villager self = (Villager)(Object)this;
		if (tradesRerolled && self.getVillagerData().profession().is(VillagerProfession.LIBRARIAN)) {
			playSound(self);
			tradesRerolled = false;
		}
	}

	@Unique
	private void playSound(Villager villager) {
		LetsGetMending.LOGGER.info("sounding");

		Holder<Enchantment> mending = villager.level().registryAccess()
				.lookupOrThrow(Registries.ENCHANTMENT)
				.getOrThrow(Enchantments.MENDING);

		if (villager.getOffers().stream().anyMatch(offer ->
				offer.getResult().getOrDefault(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY).getLevel(mending) > 0
				&& offer.getResult().getItem().equals(Items.ENCHANTED_BOOK)
		)) {
			LetsGetMending.LOGGER.info("Mending!");
			villager.level().playSound(
					null,
					villager.blockPosition(),
					ModSounds.SUCCESS_TRADE,
					SoundSource.NEUTRAL,
					1.0f,
					1.0f
			);
		} else {
			villager.level().playSound(
					null,
					villager.blockPosition(),
					ModSounds.FAIL_TRADE,
					SoundSource.NEUTRAL,
					1.0f,
					1.0f
			);
		}
	}
}