package me.stevemmmmm.thepitremake.enchants;

import me.stevemmmmm.thepitremake.core.Main;
import me.stevemmmmm.thepitremake.managers.enchants.CustomEnchant;
import me.stevemmmmm.thepitremake.managers.enchants.EnchantGroup;
import me.stevemmmmm.thepitremake.managers.enchants.EnchantProperty;
import me.stevemmmmm.thepitremake.managers.enchants.LoreBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/*
 * Copyright (c) 2020. Created by Stevemmmmm.
 */

public class Hearts extends CustomEnchant {

    private final HashMap<UUID, Integer> playerHasHearts = new HashMap<>();
    private final EnchantProperty<Float> heartsAmount = new EnchantProperty<>(0.5f, 1f, 2f);
    private boolean effectGiven;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        playerHasHearts.put(event.getPlayer().getUniqueId(), Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.INSTANCE, () -> {
            

            if (itemHasEnchant(player.getInventory().getLeggings(), this) && effectGiven != true) {
                player.setMaxHealth(player.getMaxHealth() + heartsAmount.getValueAtLevel(CustomEnchant.getEnchantLevel(player.getInventory().getLeggings(), this)));
                effectGiven = true;
            } else {
                player.setMaxHealth(24);
                effectGiven = false;
            }
        }, 0L, 1L));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Bukkit.getServer().getScheduler().cancelTask(playerHasHearts.get(event.getPlayer().getUniqueId()));
        playerHasHearts.remove(event.getPlayer().getUniqueId());
    }

  
    @Override
    public void applyEnchant(int level, Object... args) {
       
    }

    @Override
    public String getName() {
        return "Hearts";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Hearts";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        return new LoreBuilder()
                .declareVariable("0.25❤", "0.5❤", "1❤")
                .write(ChatColor.GRAY + "Increase your max health by").next()
                .writeVariable(ChatColor.RED, 0, level).build();
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
