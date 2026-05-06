package com.archmage.spells.lightning;

import com.archmage.Archmage;
import com.archmage.elements.ElementType;
import com.archmage.mastery.MasteryTier;
import com.archmage.spells.SpellBase;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.AutoSpellConfig;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.damage.DamageSources;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

/**
 * Arc Bolt — basic Lightning spell, available at Apprentice mastery.
 * Fires a lightning strike at the aimed target. Deals damage scaled by spell level.
 */
@AutoSpellConfig
public class ArcBoltSpell extends SpellBase {

    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(Archmage.MOD_ID, "arc_bolt");

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.COMMON)
            .setSchoolResource(SchoolRegistry.LIGHTNING_RESOURCE)
            .setMaxLevel(5)
            .setCooldownSeconds(8)
            .build();

    public ArcBoltSpell() {
        super(ElementType.LIGHTNING, MasteryTier.APPRENTICE);
        this.baseManaCost = 20;
        this.manaCostPerLevel = 5;
        this.baseSpellPower = 8;
        this.spellPowerPerLevel = 3;
        this.castTime = 0;
    }

    @Override
    public CastType getCastType() {
        return CastType.INSTANT;
    }

    @Override
    public DefaultConfig getDefaultConfig() {
        return defaultConfig;
    }

    @Override
    public ResourceLocation getSpellResource() {
        return spellId;
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.of(SoundEvents.LIGHTNING_BOLT_THUNDER);
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        var result = Utils.raycastForEntity(level, entity, 24, true, 0.5f);
        Vec3 pos = result.getLocation();

        if (result.getType() == HitResult.Type.ENTITY) {
            var entityHit = (EntityHitResult) result;
            pos = entityHit.getEntity().position();
            DamageSources.applyDamage(entityHit.getEntity(), getSpellPower(spellLevel, entity), getDamageSource(entity));
        }

        LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(level);
        bolt.setVisualOnly(true);
        bolt.setDamage(0);
        bolt.setPos(pos);
        level.addFreshEntity(bolt);

        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }
}
