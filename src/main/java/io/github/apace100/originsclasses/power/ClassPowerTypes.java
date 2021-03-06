package io.github.apace100.originsclasses.power;

import io.github.apace100.origins.power.AttributePower;
import io.github.apace100.origins.power.Power;
import io.github.apace100.origins.power.PowerType;
import io.github.apace100.origins.power.VariableIntPower;
import io.github.apace100.origins.registry.ModRegistries;
import io.github.apace100.originsclasses.OriginsClasses;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.OreBlock;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ClassPowerTypes {

    // Rogue
    public static final PowerType<Power> SNEAKY = new PowerType<>(Power::new);
    public static final PowerType<VariableIntPower> STEALTH = new PowerType<>((type, player) -> new VariableIntPower(type, player, 0, 0, 200));
    public static final PowerType<Power> STEALTH_DESCRIPTOR = new PowerType<>(Power::new);

    // Warrior
    public static final PowerType<Power> LESS_SHIELD_SLOWDOWN = new PowerType<>(Power::new);
    public static final PowerType<AttributePower> MORE_ATTACK_DAMAGE = new PowerType<>((type, player) -> {
        return new AttributePower(type, player, EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier("Warrior attack bonus", 1.0, EntityAttributeModifier.Operation.ADDITION));
    });

    // Ranger
    public static final PowerType<Power> LESS_BOW_SLOWDOWN = new PowerType<>(Power::new);
    public static final PowerType<Power> NO_PROJECTILE_DIVERGENCE = new PowerType<>(Power::new);

    // Beastmaster
    public static final PowerType<Power> TAMED_ANIMAL_BOOST = new PowerType<>(Power::new);
    public static final PowerType<Power> TAMED_POTION_DIFFUSAL = new PowerType<>(Power::new);

    // Cook
    public static final PowerType<Power> MORE_SMOKER_XP = new PowerType<>(Power::new);
    public static final PowerType<Power> BETTER_CRAFTED_FOOD = new PowerType<>(Power::new);

    // Cleric
    public static final PowerType<Power> LONGER_POTIONS = new PowerType<>(Power::new);
    public static final PowerType<Power> BETTER_ENCHANTING = new PowerType<>(Power::new);

    // Blacksmith
    public static final PowerType<Power> QUALITY_EQUIPMENT = new PowerType<>(Power::new);
    public static final PowerType<Power> EFFICIENT_REPAIRS = new PowerType<>(Power::new);

    // Farmer
    public static final PowerType<Power> MORE_CROP_DROPS = new PowerType<>(Power::new);
    public static final PowerType<Power> BETTER_BONE_MEAL = new PowerType<>(Power::new);

    // Rancher
    public static final PowerType<Power> TWIN_BREEDING = new PowerType<>(Power::new);
    public static final PowerType<Power> MORE_ANIMAL_LOOT = new PowerType<>(Power::new);

    // Merchant
    public static final PowerType<Power> TRADE_AVAILABILITY = new PowerType<>(Power::new);
    public static final PowerType<Power> RARE_WANDERING_LOOT = new PowerType<>(Power::new);

    // Miner
    public static final PowerType<MultiMinePower> ORE_VEIN_MINING = new PowerType<>((type, player) -> new MultiMinePower(type, player, (pl, bs, bp) -> {
        List<BlockPos> affected = new LinkedList<>();
        Queue<BlockPos> queue = new LinkedList<>();
        queue.add(bp);
        while(!queue.isEmpty()) {
            BlockPos pos = queue.remove();
            for(Direction d : Direction.values()) {
                BlockPos newPos = pos.offset(d);
                if(pl.world.getBlockState(newPos).isOf(bs.getBlock()) && !affected.contains(newPos)) {
                    affected.add(newPos);
                    queue.add(newPos);
                    if(affected.size() >= 31) {
                        return affected;
                    }
                }
            }
        }
        return affected;
    }, state -> state.getBlock() instanceof OreBlock));
    public static final PowerType<Power> MORE_STONE_BREAK_SPEED = new PowerType<>(Power::new);
    public static final PowerType<Power> NO_MINING_EXHAUSTION = new PowerType<>(Power::new);

    public static final PowerType<StartingEquipmentPower> EXPLORER_KIT = new PowerType<>((type, player) -> new StartingEquipmentPower(type, player).addStack(new ItemStack(Items.COMPASS)).addStack(new ItemStack(Items.CLOCK)).addStack(new ItemStack(Items.MAP, 9)));
    public static final PowerType<Power> NO_SPRINT_EXHAUSTION = new PowerType<>(Power::new);

    // Lumberjack
    public static final PowerType<MultiMinePower> TREE_FELLING = new PowerType<>((type, player) -> (MultiMinePower)new MultiMinePower(type, player, (pl, bs, bp) -> {
        List<BlockPos> affected = new LinkedList<>();
        Queue<BlockPos> queue = new LinkedList<>();
        queue.add(bp);
        boolean foundOneWithLeaves = false;
        while(!queue.isEmpty()) {
            BlockPos pos = queue.remove();
            for(int dx = -1; dx <= 1; dx++) {
                for(int dy = 0; dy <= 1; dy++) {
                    for(int dz = -1; dz <= 1; dz++) {
                        if(dx == 0 & dy == 0 && dz == 0) {
                            continue;
                        }
                        BlockPos newPos = pos.add(dx, dy, dz);
                        BlockState state = pl.world.getBlockState(newPos);
                        if(state.isOf(bs.getBlock()) && !affected.contains(newPos)) {
                            affected.add(newPos);
                            queue.add(newPos);
                        } else
                        if(state.getBlock() instanceof LeavesBlock && !state.get(LeavesBlock.PERSISTENT)) {
                            foundOneWithLeaves = true;
                        }
                    }
                }
            }
        }
        if(!foundOneWithLeaves) {
            affected.clear();
        }
        return affected;
    }, state -> state.getBlock().isIn(BlockTags.LOGS)).addCondition(p -> p.getMainHandStack().getItem() instanceof AxeItem));
    public static final PowerType<Power> MORE_PLANKS_FROM_LOGS = new PowerType<>(Power::new);

    public static void register() {
        register("sneaky", SNEAKY);
        register("stealth", STEALTH);
        register("stealth_descriptor", STEALTH_DESCRIPTOR);

        register("less_shield_slowdown", LESS_SHIELD_SLOWDOWN);
        register("more_attack_damage", MORE_ATTACK_DAMAGE);

        register("less_bow_slowdown", LESS_BOW_SLOWDOWN);
        register("no_projectile_divergence", NO_PROJECTILE_DIVERGENCE);

        register("tamed_animal_boost", TAMED_ANIMAL_BOOST);
        register("tamed_potion_diffusal", TAMED_POTION_DIFFUSAL);

        register("more_smoker_xp", MORE_SMOKER_XP);
        register("better_crafted_food", BETTER_CRAFTED_FOOD);

        register("longer_potions", LONGER_POTIONS);
        register("better_enchanting", BETTER_ENCHANTING);

        register("quality_equipment", QUALITY_EQUIPMENT);
        register("efficient_repairs", EFFICIENT_REPAIRS);

        register("more_crop_drops", MORE_CROP_DROPS);
        register("better_bone_meal", BETTER_BONE_MEAL);

        register("twin_breeding", TWIN_BREEDING);
        register("more_animal_loot", MORE_ANIMAL_LOOT);

        register("trade_availability", TRADE_AVAILABILITY);
        register("rare_wandering_loot", RARE_WANDERING_LOOT);

        register("ore_vein_mining", ORE_VEIN_MINING);
        register("more_stone_break_speed", MORE_STONE_BREAK_SPEED);
        register("no_mining_exhaustion", NO_MINING_EXHAUSTION);

        register("tree_felling", TREE_FELLING);
        register("more_planks_from_logs", MORE_PLANKS_FROM_LOGS);

        register("explorer_kit", EXPLORER_KIT);
        register("no_sprint_exhaustion", NO_SPRINT_EXHAUSTION);
    }

    private static void register(String path, PowerType<?> powerType) {
        Registry.register(ModRegistries.POWER_TYPE, new Identifier(OriginsClasses.MODID, path), powerType);
    }

}
