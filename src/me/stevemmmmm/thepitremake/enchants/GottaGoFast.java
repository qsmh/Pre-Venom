package me.stevemmmmm.thepitremake.enchants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.stevemmmmm.thepitremake.core.Main;
import me.stevemmmmm.thepitremake.managers.enchants.CustomEnchant;
import me.stevemmmmm.thepitremake.managers.enchants.EnchantGroup;
import me.stevemmmmm.thepitremake.managers.enchants.EnchantProperty;
import me.stevemmmmm.thepitremake.managers.enchants.LoreBuilder;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;

/*
 * Copyright (c) 2020. Created by Stevemmmmm.
 */

public class GottaGoFast extends CustomEnchant {

    private final HashMap<UUID, Integer> playerHasGtgf = new HashMap<>();
    private final EnchantProperty<Float> speedAmplifier = new EnchantProperty<>(0.04f, 0.1f, 0.2f);
    private boolean effectGiven;
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        playerHasGtgf.put(event.getPlayer().getUniqueId(), Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.INSTANCE, () -> {
        	

            if (itemHasEnchant(player.getInventory().getLeggings(), this) && effectGiven != true) {
                player.setWalkSpeed((player.getWalkSpeed() + speedAmplifier.getValueAtLevel(CustomEnchant.getEnchantLevel(player.getInventory().getLeggings(), this))) / 5);
                Location location = player.getLocation();

                PacketPlayOutWorldParticles particles = new PacketPlayOutWorldParticles(EnumParticle.SMOKE_LARGE, true, (float) location.getX(), (float) location.getY(), (float) location.getZ(), 255, 255, 255, 255, 255, 255);
                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(particles);
                effectGiven = true;
            } else {
                player.setWalkSpeed((1f) / 5);
                effectGiven = false;
            }
        }, 0L, 1L));
    }


    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Bukkit.getServer().getScheduler().cancelTask(playerHasGtgf.get(event.getPlayer().getUniqueId()));
        playerHasGtgf.remove(event.getPlayer().getUniqueId());
        effectGiven = false;
    }

  
    @Override
    public void applyEnchant(int level, Object... args) {
       
    }

    @Override
    public String getName() {
        return "Gotta go fast";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Gtgf";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        return new LoreBuilder()
                .declareVariable("4% faster", "10% faster", "20% faster")
                .write("Move ").writeVariable(ChatColor.YELLOW, 0, level).write(" at all times").build();

    }

    @Override
    public boolean isDisabledOnPassiveWorld() {
        return false;
    }

    @Override
    public EnchantGroup getEnchantGroup() {
        return EnchantGroup.B;
    }

    @Override
    public boolean isRareEnchant() {
        return false;
    }

    @Override
    public Material[] getEnchantItemTypes() {
        return new Material[] { Material.LEATHER_LEGGINGS };
    }
}
