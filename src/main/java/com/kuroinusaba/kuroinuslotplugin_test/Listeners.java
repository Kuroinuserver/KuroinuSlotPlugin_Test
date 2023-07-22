package com.kuroinusaba.kuroinuslotplugin_test;

import net.milkbowl.vault.economy.Economy;
import com.destroystokyo.paper.event.entity.EntityAddToWorldEvent;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityPlaceEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.*;

import static com.kuroinusaba.kuroinuslotplugin_test.KuroinuSlotPlugin_Test.*;

public class Listeners implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        File data = new File("plugins/KuroinuSlotPlugin_Test/data/" + player.getUniqueId() + ".yml");
        YamlConfiguration dataconfig = YamlConfiguration.loadConfiguration(data);
        if (!data.exists()) {
            dataconfig.set("slot", 0);
        }
        try {
            dataconfig.save(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        File data = new File("plugins/KuroinuSlotPlugin_Test/data/" + player.getUniqueId() + ".yml");
        YamlConfiguration dataconfig = YamlConfiguration.loadConfiguration(data);
        if (!data.exists()) {
            dataconfig.set("slot", 0);
        }
        try {
            dataconfig.save(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @EventHandler
    public void onPlayerBlockPlace(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        plugin.getLogger().info(e.getBlock().getType().toString());
        File data = new File("plugins/KuroinuSlotPlugin_Test/data/" + player.getUniqueId() + ".yml");
        YamlConfiguration dataconfig = YamlConfiguration.loadConfiguration(data);
        if (!data.exists()) {
            return;
        }
        try {
            dataconfig.save(data);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        if (e.getBlock().getType().toString().equals("LEVER")) {
            if (dataconfig.getInt("slot") == 4) {
                player.sendMessage(prefix + "§a§lスロットを設置しました。");
                player.sendMessage(prefix + "§a§l次に看板を設置してください。");
                File placedslot = new File("plugins/KuroinuSlotPlugin_Test/placedslot/" + dataconfig.getString("slotname") + ".yml");
                YamlConfiguration placedslotconfig = YamlConfiguration.loadConfiguration(placedslot);
                placedslotconfig.set("lever", e.getBlock().getLocation());
                try {
                    placedslotconfig.save(placedslot);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
                try {
                    dataconfig.set("slot", 5);
                    dataconfig.save(data);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        } else if (e.getBlock().getType().toString().equals("OAK_SIGN") || e.getBlock().getType().toString().equals("OAK_WALL_SIGN")) {
            if (dataconfig.getInt("slot") == 5) {
                player.sendMessage(prefix + "§a§l看板を設置しました。");
                player.sendMessage(prefix + "§e§lスロットが完成しました！");
                File placedslot = new File("plugins/KuroinuSlotPlugin_Test/placedslot/" + dataconfig.getString("slotname") + ".yml");
                YamlConfiguration placedslotconfig = YamlConfiguration.loadConfiguration(placedslot);
                placedslotconfig.set("sign", e.getBlock().getLocation());
                try {
                    placedslotconfig.save(placedslot);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }

                try {
                    dataconfig.set("slot", 0);
                    dataconfig.set("pslot", "");
                    dataconfig.set("slotname", "");
                    dataconfig.save(data);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    @EventHandler
    public void onHangingPlace(HangingPlaceEvent e) {
        if (!(e.getEntity().getType().toString().equals("ITEM_FRAME"))) {
            return;
        }
        Player player = e.getPlayer();
        String frame = String.valueOf(e.getEntity());
        plugin.getLogger().info(frame);
        File data = new File("plugins/KuroinuSlotPlugin_Test/data/" + player.getUniqueId() + ".yml");
        YamlConfiguration dataconfig = YamlConfiguration.loadConfiguration(data);
        if (!data.exists()) {
            return;
        }
        try {
            dataconfig.save(data);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        if (dataconfig.getInt("slot") == 0) {
            player.sendMessage(prefix + "§c§lスロットを作成してください。");
            player.sendMessage(prefix + "§c§lスロットを作成するには、/slot create <スロット名> <スロットの名前>を実行してください。");
            return;
        }
        if (dataconfig.getInt("slot") <= 3 && dataconfig.getInt("slot") >= 1) {
            player.sendMessage(prefix + "§a§l" + dataconfig.getInt("slot") + "§r§a番目の額縁を設置しました。");
            if (dataconfig.getInt("slot") == 3) {
                player.sendMessage(prefix + "§a§l次にレバーを設置してください。");
            }
            File placedslot = new File("plugins/KuroinuSlotPlugin_Test/placedslot/" + dataconfig.getString("slotname") + ".yml");
            YamlConfiguration placedslotconfig = YamlConfiguration.loadConfiguration(placedslot);
            placedslotconfig.set("itemframe" + dataconfig.getInt("slot"), e.getEntity().getLocation());
            try {
                placedslotconfig.save(placedslot);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            try {
                dataconfig.set("slot", dataconfig.getInt("slot") + 1);
                dataconfig.save(data);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (e.getClickedBlock().getType().toString().equals("LEVER")) {
                File slot = new File("plugins/KuroinuSlotPlugin_Test/placedslot");
                for (File file : slot.listFiles()) {
                    YamlConfiguration slotconfig = YamlConfiguration.loadConfiguration(file);
                    if (slotconfig.get("lever") == null) {
                        continue;
                    }
                    if (slotconfig.get("lever").equals(e.getClickedBlock().getLocation())) {
                        if (slotconfig.get("sign") == null) {
                            player.sendMessage(prefix + "§c§lスロットが不正です。");
                            return;
                        }
                        if (slotconfig.get("itemframe1") == null) {
                            player.sendMessage(prefix + "§c§lスロットが不正です。");
                            return;
                        }
                        if (slotconfig.get("itemframe2") == null) {
                            player.sendMessage(prefix + "§c§lスロットが不正です。");
                            return;
                        }
                        if (slotconfig.get("itemframe3") == null) {
                            player.sendMessage(prefix + "§c§lスロットが不正です。");
                            return;
                        }
                        if (slotconfig.get("on") == null) {
                            slotconfig.set("on", false);
                            try {
                                slotconfig.save(file);
                            } catch (Exception ex) {
                                throw new RuntimeException(ex);
                            }
                        } else {
                            if (!(slotconfig.getBoolean("on") == true)) {
                                player.sendMessage(prefix + "§c§lスロットが起動していません。");
                                return;
                            }
                        }
                        File slotdata = new File("plugins/KuroinuSlotPlugin_Test/slots/" + slotconfig.getString("slotname") + ".yml");
                        YamlConfiguration slotdataconfig = YamlConfiguration.loadConfiguration(slotdata);
                        if (slotdataconfig.get("coin.type") == null) {
                            player.sendMessage(prefix + "§c§lスロットが不正です。");
                            return;
                        }
                        if (slotdataconfig.get("coin.amount") == null) {
                            player.sendMessage(prefix + "§c§lスロットが不正です。");
                            return;
                        }
                        if (slotdataconfig.get("coin.type").equals("item")) {
                            if (slotdataconfig.get("coin.item") == null) {
                                player.sendMessage(prefix + "§c§lスロットが不正です。");
                                return;
                            }
                        }
                        if (slotdataconfig.get("coin.type").equals("money")) {
                            // プレイヤーのお金が足りているか確認
                            if (econ.getBalance(player) < slotdataconfig.getInt("coin.amount")) {
                                player.sendMessage(prefix + "§c§lお金が足りません。");
                                return;
                            }
                            EconomyResponse withdraw = econ.withdrawPlayer(player, slotdataconfig.getInt("coin.amount"));
                            if (withdraw.transactionSuccess()) {
                                player.sendMessage(prefix + "§e§l" + withdraw.amount + "§r§e円支払いました。");
                                // スロットの処理
                                YamlConfiguration slotdata2 = YamlConfiguration.loadConfiguration(slotdata);
                                File slotconfig2 = new File("plugins/KuroinuSlotPlugin_Test/placedslot/" + slotconfig.getString("slotname") + ".yml");
                                YamlConfiguration slotconfig3 = YamlConfiguration.loadConfiguration(slotconfig2);
                                roll(player, slotdata2, slotconfig3);
                            } else {
                                player.sendMessage(prefix + "§c§lお金を支払えませんでした。");
                                return;
                            }
                        } else if (slotdataconfig.get("coin.type").equals("item")) {
                            ItemStack item = new ItemStack(Material.getMaterial(slotdataconfig.getString("coin.item")));
                            ItemMeta meta = item.getItemMeta();
                            meta.setDisplayName(slotdataconfig.getString("coin.itemname"));
                            meta.setLore(Arrays.asList(slotdataconfig.getString("coin.itemlore")));
                            item.setItemMeta(meta);
                            if (player.getInventory().containsAtLeast(item, slotdataconfig.getInt("coin.amount"))) {
                                player.getInventory().removeItem(item);
                                player.sendMessage(prefix + "§e§l" + slotdataconfig.getInt("coin.amount") + "§r§e個の" + slotdataconfig.getString("coin.itemname") + "§r§eを支払いました。");
                            } else {
                                player.sendMessage(prefix + "§c§lアイテムを支払えませんでした。");
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    public void roll(Player player, YamlConfiguration slotdata, YamlConfiguration placedslotconfig) {
        int symbolnum = slotdata.getConfigurationSection("symbol").getKeys(false).size();
        for (int i = 1; i <= symbolnum; i++) {
        }
        int tick = slotdata.getInt("tick");
        int delay = slotdata.getInt("delay");
        Location loc1 = placedslotconfig.getLocation("itemframe1");
        Location loc2 = placedslotconfig.getLocation("itemframe2");
        Location loc3 = placedslotconfig.getLocation("itemframe3");
        for (int i = 1; i <= tick; i++) {
            int finalI = i;
            int finalDelay = delay;
            Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    int frameroll1 = new Random().nextInt(symbolnum) + 1;
                    int frameroll2 = new Random().nextInt(symbolnum) + 1;
                    int frameroll3 = new Random().nextInt(symbolnum) + 1;
                    if (loc1.getNearbyEntities(0.5, 0.5, 0.5).size() == 0) {
                        if (player.hasPermission("admin")) {
                            player.sendMessage(prefix + "§c§l1§r§c番目のアイテムフレームが見つかりませんでした。");
                        }
                        
                    } else {
                        List<Entity> entities = (List<Entity>) loc1.getNearbyEntities(0.5, 0.5, 0.5);
                        for (Entity entity : entities) {
                            if (entity instanceof ItemFrame) {
                                ItemFrame itemframe = (ItemFrame) entity;
                                itemframe.setItem(new ItemStack(Material.getMaterial(slotdata.getString("symbol." + frameroll1 + ".item"))));
                                if (slotdata.getBoolean("symbol." + frameroll1 + ".glow")) {
                                    ItemStack flame1 = new ItemStack(Material.getMaterial(slotdata.getString("symbol." + frameroll1 + ".item")));
                                    flame1.getItemMeta().setDisplayName(slotdata.getString("symbol." + frameroll1 + ".name"));
                                    ItemMeta meta1 = flame1.getItemMeta();
                                    if (slotdata.getBoolean("symbol." + frameroll1 + ".glow")) {
                                        meta1.addEnchant(Enchantment.DURABILITY, 1, true);
                                        meta1.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                                    }
                                    itemframe.setItem(flame1);
                                }
                            }
                        }
                        List<Entity> entities2 = (List<Entity>) loc2.getNearbyEntities(0.5, 0.5, 0.5);
                        for (Entity entity : entities2) {
                            if (entity instanceof ItemFrame) {
                                ItemFrame itemframe = (ItemFrame) entity;
                                itemframe.setItem(new ItemStack(Material.getMaterial(slotdata.getString("symbol." + frameroll2 + ".item"))));
                                if (slotdata.getBoolean("symbol." + frameroll2 + ".glow")) {
                                    ItemStack flame2 = new ItemStack(Material.getMaterial(slotdata.getString("symbol." + frameroll2 + ".item")));
                                    flame2.getItemMeta().setDisplayName(slotdata.getString("symbol." + frameroll2 + ".name"));
                                    ItemMeta meta2 = flame2.getItemMeta();
                                    if (slotdata.getBoolean("symbol." + frameroll2 + ".glow")) {
                                        meta2.addEnchant(Enchantment.DURABILITY, 1, true);
                                        meta2.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                                    }
                                    itemframe.setItem(flame2);
                                }
                            }
                        }
                        List<Entity> entities3 = (List<Entity>) loc3.getNearbyEntities(0.5, 0.5, 0.5);
                        for (Entity entity : entities3) {
                            if (entity instanceof ItemFrame) {
                                ItemFrame itemframe = (ItemFrame) entity;
                                itemframe.setItem(new ItemStack(Material.getMaterial(slotdata.getString("symbol." + frameroll3 + ".item"))));
                                if (slotdata.getBoolean("symbol." + frameroll3 + ".glow")) {
                                    ItemStack flame3 = new ItemStack(Material.getMaterial(slotdata.getString("symbol." + frameroll3 + ".item")));
                                    flame3.getItemMeta().setDisplayName(slotdata.getString("symbol." + frameroll3 + ".name"));
                                    ItemMeta meta3 = flame3.getItemMeta();
                                    if (slotdata.getBoolean("symbol." + frameroll3 + ".glow")) {
                                        meta3.addEnchant(Enchantment.DURABILITY, 1, true);
                                        meta3.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                                    }
                                    itemframe.setItem(flame3);
                                }
                            }
                        }
                    }
                }
            }, delay * i);
        }
    }
}
