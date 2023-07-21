package com.kuroinusaba.kuroinuslotplugin_test.Commands;

import com.kuroinusaba.kuroinuslotplugin_test.KuroinuSlotPlugin_Test;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.server.TabCompleteEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.kuroinusaba.kuroinuslotplugin_test.KuroinuSlotPlugin_Test.plugin;
import static com.kuroinusaba.kuroinuslotplugin_test.KuroinuSlotPlugin_Test.prefix;

public class kuroinuslot implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        if (args.length == 0) {
            player.sendMessage(prefix + "§c引数が足りません。");
            player.sendMessage(prefix + "§c/kuroinuslot helpを参照してください。");
        } else if (args[0].equals("help")) {
            player.sendMessage(prefix + "§e§l ======== §d§lKuroinuSlotPlugin §e§l========");
            player.sendMessage(prefix + "§e§l/kuroinuslot help §f§l: §a§lこのヘルプを表示します。");
            player.sendMessage(prefix + "§e§l/kuroinuslot create §f§l: §a§lスロットを作成します。");
            player.sendMessage(prefix + "§e§l/kuroinuslot edit §f§l: §a§lスロットを編集します。");
            player.sendMessage(prefix + "§e§l/kuroinuslot start §f§l: §a§lスロットを開始します。");
            player.sendMessage(prefix + "§e§l/kuroinuslot stop §f§l: §a§lスロットを停止します。");
            player.sendMessage(prefix + "§e§l/kuroinuslot reload §f§l: §a§lコンフィグを再読み込みします。");
            player.sendMessage(prefix + "§e§l/kuroinuslot info §f§l: §a§lプラグインの情報を表示します。");
            player.sendMessage(prefix + "§e§l/kuroinuslot version §f§l: §a§lプラグインのバージョンを表示します。");
            player.sendMessage(prefix + "§e§l/kuroinuslot debug §f§l: §a§lデバッグモードを切り替えます。");
            player.sendMessage(prefix + "§e§l/kuroinuslot test §f§l: §a§lテスト用のコマンドです。");
            player.sendMessage(prefix + "§e§l =================================");
        } else if (args[0].equals("create")) {
            if (args.length == 1) {
                player.sendMessage(prefix + "§c引数が足りません。");
                player.sendMessage(prefix + "§c/kuroinuslot create helpを参照してください。");
            } else if (args.length == 2) {
                if (args[1].equals("help")) {
                    player.sendMessage(prefix + "§e§l ======== §d§lKuroinuSlotPlugin §e§l========");
                    player.sendMessage(prefix + "§e§l/kuroinuslot create help §f§l: §a§lこのヘルプを表示します。");
                    player.sendMessage(prefix + "§e§l/kuroinuslot create <スロット名> §f§l: §a§lスロットを作成します。");
                    player.sendMessage(prefix + "§e§l =================================");
                } else {
                    File newSlot = new File("plugins/KuroinuSlotPlugin_Test/slots/" + args[1] + ".yml");
                    YamlConfiguration newSlotYml = new YamlConfiguration();
                    File dataFolder = new File("plugins/KuroinuSlotPlugin_Test/slots");
                    if (!dataFolder.exists()) {
                        dataFolder.mkdir();
                        try {
                            newSlot.createNewFile();
                            newSlotYml.set("name", args[1]);
                            newSlotYml.set("coin.type", "money");
                            newSlotYml.set("coin.amount", 100);
                            newSlotYml.set("coin.item", null);
                            newSlotYml.set("slot." + 1 + ".reward." + 1 + ".item", "DIAMOND");
                            newSlotYml.set("slot." + 1 + ".percentage", 10);
                            newSlotYml.set("slot." + 2 + ".reward." + 1 + ".item", "DIAMOND");
                            newSlotYml.set("slot." + 2 + ".reward." + 1 + ".amount", 2);
                            newSlotYml.set("slot." + 2 + ".reward." + 1 + ".name", "§e§lダイヤモンド");
                            newSlotYml.set("slot." + 2 + ".reward." + 1 + ".lore", "§7ダイヤモンドです。");
                            newSlotYml.set("slot." + 2 + ".percentage", 20);
                            newSlotYml.save(newSlot);
                            player.sendMessage(prefix + "§e§lスロットを作成しました。");
                        } catch (Exception e) {
                            player.sendMessage(prefix + "§cスロットの作成に失敗しました。");
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                player.sendMessage(prefix + "§c引数が間違っています。");
                player.sendMessage(prefix + "§c/kuroinuslot helpを参照してください。");
            }
            return false;
        } else if (args[0].equals("edit")) {
            if (args[1].equals("help")) {
                player.sendMessage(prefix + "§e§l ======== §d§lKuroinuSlotPlugin §e§l========");
                player.sendMessage(prefix + "§e§l/kuroinuslot edit help §f§l: §a§lこのヘルプを表示します。");
                player.sendMessage(prefix + "§e§l/kuroinuslot edit <スロット名> §f§l: §a§lスロットを編集します。");
                player.sendMessage(prefix + "§e§l =================================");
            } else {
                File editSlot = new File("plugins/KuroinuSlotPlugin_Test/slots/" + args[1] + ".yml");
                YamlConfiguration editSlotYml = YamlConfiguration.loadConfiguration(editSlot);
                if (args[2].equals("coin")) {
                    if (args[3].equals("type")) {
                        editSlotYml.set("coin.type", args[4]);
                        try {
                            editSlotYml.save(editSlot);
                            player.sendMessage(prefix + "§e§lコインの種類を変更しました。");
                        } catch (Exception e) {
                            player.sendMessage(prefix + "§cコインの種類の変更に失敗しました。");
                            e.printStackTrace();
                        }
                    } else if (args[3].equals("amount")) {
                        // int型に変換する
                        editSlotYml.set("coin.amount", Integer.parseInt(args[4]));
                        try {
                            editSlotYml.save(editSlot);
                            player.sendMessage(prefix + "§e§lコインの量を変更しました。");
                        } catch (Exception e) {
                            player.sendMessage(prefix + "§cコインの量の変更に失敗しました。");
                            e.printStackTrace();
                        }
                    } else if (args[3].equals("item")) {
                        editSlotYml.set("coin.item", args[4]);
                        try {
                            editSlotYml.save(editSlot);
                            player.sendMessage(prefix + "§e§lコインのアイテムを変更しました。");
                        } catch (Exception e) {
                            player.sendMessage(prefix + "§cコインのアイテムの変更に失敗しました。");
                            e.printStackTrace();
                        }
                    } else if (args[3].equals("help")) {
                        player.sendMessage(prefix + "§e§l ======== §d§lKuroinuSlotPlugin §e§l========");
                        player.sendMessage(prefix + "§e§l/kuroinuslot edit <スロット名> coin help §f§l: §a§lこのヘルプを表示します。");
                        player.sendMessage(prefix + "§e§l/kuroinuslot edit <スロット名> coin type <種類> §f§l: §a§lコインの種類を変更します。");
                        player.sendMessage(prefix + "§e§l/kuroinuslot edit <スロット名> coin amount <量> §f§l: §a§lコインの量を変更します。");
                        player.sendMessage(prefix + "§e§l/kuroinuslot edit <スロット名> coin item <アイテム> §f§l: §a§lコインのアイテムを変更します。");
                        player.sendMessage(prefix + "§e§l =================================");
                    } else {
                        player.sendMessage(prefix + "§c引数が間違っています。");
                        player.sendMessage(prefix + "§c/kuroinuslot edit helpを参照してください。");
                    }
                } else if (args[2].equals("slot")) {
                    if (args[4].equals("reward")) {
                        if (args[6].equals("money")) {
                            editSlotYml.set("slot." + args[3] + ".reward." + args[5] + ".money", Integer.parseInt(args[7]));
                            try {
                                editSlotYml.save(editSlot);
                                player.sendMessage(prefix + "§e§lスロットの報酬を変更しました。");
                            } catch (Exception e) {
                                player.sendMessage(prefix + "§cスロットの報酬の変更に失敗しました。");
                                e.printStackTrace();
                            }
                        } else if (args[6].equals("item")) {
                            editSlotYml.set("slot." + args[3] + ".reward." + args[5] + ".item", args[7]);
                            editSlotYml.set("slot." + args[3] + ".reward." + args[5] + ".amount", Integer.parseInt(args[8]));
                            try {
                                editSlotYml.save(editSlot);
                                player.sendMessage(prefix + "§e§lスロットの報酬を変更しました。");
                            } catch (Exception e) {
                                player.sendMessage(prefix + "§cスロットの報酬の変更に失敗しました。");
                                e.printStackTrace();
                            }
                        } else {
                            player.sendMessage(prefix + "§c引数が間違っています。");
                            player.sendMessage(prefix + "§c/kuroinuslot edit helpを参照してください。");
                        }
                    } else if (args[4].equals("percentage")) {
                        if (args.length == 6) {
                            // int型に変換する
                            editSlotYml.set("slot." + args[3] + ".percentage", Integer.parseInt(args[5]));
                            try {
                                editSlotYml.save(editSlot);
                                player.sendMessage(prefix + "§e§lスロットの確率を変更しました。");
                            } catch (Exception e) {
                                player.sendMessage(prefix + "§cスロットの確率の変更に失敗しました。");
                                e.printStackTrace();
                            }
                        } else {
                            player.sendMessage(prefix + "§c引数が間違っています。");
                            player.sendMessage(prefix + "§c/kuroinuslot edit helpを参照してください。");
                        }
                    } else if (args[4].equals("help")) {
                        player.sendMessage(prefix + "§e§l ======== §d§lKuroinuSlotPlugin §e§l========");
                        player.sendMessage(prefix + "§e§l/kuroinuslot edit <スロット名> slot help §f§l: §a§lこのヘルプを表示します。");
                        player.sendMessage(prefix + "§e§l/kuroinuslot edit <スロット名> slot reward <報酬名> money <金額> §f§l: §a§lスロットの報酬を変更します。");
                        player.sendMessage(prefix + "§e§l/kuroinuslot edit <スロット名> slot reward <報酬名> item <アイテム> <量> §f§l: §a§lスロットの報酬を変更します。");
                        player.sendMessage(prefix + "§e§l/kuroinuslot edit <スロット名> slot percentage <確率> §f§l: §a§lスロットの確率を変更します。");
                        player.sendMessage(prefix + "§e§l =================================");
                    } else {
                        player.sendMessage(prefix + "§c引数が間違っています。");
                        player.sendMessage(prefix + "§c/kuroinuslot edit helpを参照してください。");
                    }
                }
            }
        } else if (args[0].equals("info")) {
            player.sendMessage(prefix + "§e§l ======== §d§lKuroinuSlotPlugin §e§l========");
            player.sendMessage(prefix + "§e§l説明: §a§lスロットマシンを作成するプラグインです。");
            player.sendMessage(prefix + "§e§l作者: §a§ljpbtk");
            player.sendMessage(prefix + "§e§lバージョン: §a§l" + plugin.getDescription().getVersion());
            player.sendMessage(prefix + "§e§l =================================");
        } else if (args[0].equals("reload")) {
            if (args.length == 1) {
                plugin.reloadConfig();
                player.sendMessage(prefix + "§e§lコンフィグをリロードしました。");
            } else {
                File editSlot = new File("plugins/KuroinuSlotPlugin_Test/slots/" + args[1] + ".yml");
                YamlConfiguration editSlotYml = YamlConfiguration.loadConfiguration(editSlot);
                // ファイルが存在するか確認する
                if (editSlot.exists()) {
                    try {
                        editSlotYml.save(editSlot);
                        player.sendMessage(prefix + "§e§lスロットファイルをリロードしました。");
                    } catch (Exception e) {
                        player.sendMessage(prefix + "§cスロットファイルのリロードに失敗しました。");
                        e.printStackTrace();
                    }
                } else {
                    player.sendMessage(prefix + "§cスロットファイルが存在しません。");
                }
            }
        } else if (args[0].equals("version")) {
            player.sendMessage(prefix + "§e§l ======== §d§lKuroinuSlotPlugin §e§l========");
            player.sendMessage(prefix + "§e§lバージョン: §a§l" + plugin.getDescription().getVersion());
            player.sendMessage(prefix + "§e§l =================================");
        } else if (args[0].equals("place")) {
            if (args.length == 1) {
                player.sendMessage(prefix + "§c引数が間違っています。");
                player.sendMessage(prefix + "§c/kuroinuslot place helpを参照してください。");
            } else if (args[1].equals("help")) {
                player.sendMessage(prefix + "§e§l ======== §d§lKuroinuSlotPlugin §e§l========");
                player.sendMessage(prefix + "§e§l/kuroinuslot place help §f§l: §a§lこのヘルプを表示します。");
                player.sendMessage(prefix + "§e§l/kuroinuslot place <スロット名> <スロットの名前> §f§l: §a§lスロットを設置します。");
                player.sendMessage(prefix + "§e§l =================================");
            } else {
                if (args.length == 3) {
                    File placeSlot = new File("plugins/KuroinuSlotPlugin_Test/slots/" + args[1] + ".yml");
                    YamlConfiguration placeSlotYml = YamlConfiguration.loadConfiguration(placeSlot);
                    // ファイルが存在するか確認する
                    if (placeSlot.exists()) {
                        try {
                            File data = new File("plugins/KuroinuSlotPlugin_Test/data/" + player.getUniqueId() + ".yml");
                            YamlConfiguration dataYml = YamlConfiguration.loadConfiguration(data);
                            // ファイルが存在するか確認する
                            if (!data.exists()) {
                                dataYml.createSection("slot");
                                dataYml.createSection("pslot");
                                dataYml.createSection("slotname");
                                dataYml.set("slot", 1);
                                dataYml.set("pslot", args[1]);
                                dataYml.set("slotname", args[2]);
                                dataYml.save(data);
                            } else {
                                dataYml.set("slot", 1);
                                dataYml.set("pslot", args[1]);
                                dataYml.set("slotname", args[2]);
                                dataYml.save(data);
                            }
                            File placedSlot = new File("plugins/KuroinuSlotPlugin_Test/placedslot/" + args[2] + ".yml");
                            YamlConfiguration placedSlotYml = YamlConfiguration.loadConfiguration(placedSlot);
                            placedSlotYml.createSection("slotname");
                            placedSlotYml.set("slotname", args[1]);
                            placedSlotYml.createSection("on");
                            placedSlotYml.set("on", true);
                            placedSlotYml.save(placedSlot);
                            player.sendMessage(prefix + "§e§lスロットを設置する場所をクリックしてください。");
                        } catch (Exception e) {
                            player.sendMessage(prefix + "§cスロットの設置に失敗しました。");
                            e.printStackTrace();
                        }
                    } else {
                        player.sendMessage(prefix + "§cスロットファイルが存在しません。");
                    }
                } else {
                    player.sendMessage(prefix + "§c引数が間違っています。");
                    player.sendMessage(prefix + "§c/kuroinuslot place helpを参照してください。");
                }
            }
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (command.getName().equals("kuroinuslot")) {
            if (args.length == 1) {
                List<String> tab = new ArrayList<>();
                tab.add("help");
                tab.add("create");
                tab.add("edit");
                tab.add("place");
                tab.add("start");
                tab.add("stop");
                tab.add("reload");
                tab.add("version");
                tab.add("debug");
                tab.add("test");
                return tab;
            } else if (args.length == 2) {
                if (args[0].equals("create")) {
                    List<String> tab = new ArrayList<>();
                    tab.add("help");
                    tab.add("<スロット名>");
                    return tab;
                } else if (args[0].equals("edit")) {
                    List<String> tab = new ArrayList<>();
                    tab.add("help");
                    File dataFolder = new File("plugins/KuroinuSlotPlugin_Test/slots");
                    if (dataFolder.exists()) {
                        File[] files = dataFolder.listFiles();
                        for (File file : files) {
                            tab.add(file.getName().replace(".yml", ""));
                        }
                    }
                    return tab;
                } else if (args[0].equals("reload")) {
                    File dataFolder = new File("plugins/KuroinuSlotPlugin_Test/slots");
                    YamlConfiguration config = YamlConfiguration.loadConfiguration(dataFolder);
                    List<String> tab = new ArrayList<>();
                    if (dataFolder.exists()) {
                        File[] files = dataFolder.listFiles();
                        for (File file : files) {
                            tab.add(file.getName().replace(".yml", ""));
                        }
                    }
                    return tab;
                } else if (args[0].equals("place")) {
                    List<String> tab = new ArrayList<>();
                    tab.add("help");
                    File dataFolder = new File("plugins/KuroinuSlotPlugin_Test/slots");
                    if (dataFolder.exists()) {
                        File[] files = dataFolder.listFiles();
                        for (File file : files) {
                            tab.add(file.getName().replace(".yml", ""));
                        }
                    }
                    return tab;
                }
            } else if (args.length == 3) {
                if (args[0].equals("edit")) {
                    List<String> tab = new ArrayList<>();
                    tab.add("coin");
                    tab.add("slot");
                    return tab;
                }
            } else if (args.length == 4) {
                if (args[0].equals("edit")) {
                    if (args[2].equals("coin")) {
                        List<String> tab = new ArrayList<>();
                        tab.add("help");
                        tab.add("type");
                        tab.add("amount");
                        tab.add("item");
                        return tab;
                    } else if (args[2].equals("slot")) {
                        List<String> tab = new ArrayList<>();
                        // yml内のslotの項目数を取得する
                        File editSlot = new File("plugins/KuroinuSlotPlugin_Test/slots/" + args[1] + ".yml");
                        YamlConfiguration editSlotYml = YamlConfiguration.loadConfiguration(editSlot);
                        int slotNum = editSlotYml.getConfigurationSection("slot").getKeys(false).size();
                        for (int i = 1; i <= slotNum; i++) {
                            tab.add(String.valueOf(i));
                        }
                        return tab;
                    }
                }
            } else if (args.length == 5) {
                if (args[0].equals("edit")) {
                    if (args[2].equals("coin")) {
                        if (args[3].equals("type")) {
                            List<String> tab = new ArrayList<>();
                            tab.add("money");
                            tab.add("item");
                            return tab;
                        } else if (args[3].equals("amount")) {
                            List<String> tab = new ArrayList<>();
                            for (int i = 1; i <= 64; i++) {
                                tab.add(String.valueOf(i));
                            }
                            return tab;
                        } else if (args[3].equals("item")) {
                            List<String> tab = new ArrayList<>();
                            for (Material material : Material.values()) {
                                tab.add(material.toString());
                            }
                            return tab;
                        }
                    } else if (args[2].equals("slot")) {
                        List<String> tab = new ArrayList<>();
                        tab.add("help");
                        tab.add("reward");
                        tab.add("percentage");
                        return tab;
                    }
                }
            } else if (args.length == 6) {
                if (args[0].equals("edit")) {
                    if (args[2].equals("slot")) {
                        if (args[4].equals("reward")) {
                            List<String> tab = new ArrayList<>();
                            File editSlot = new File("plugins/KuroinuSlotPlugin_Test/slots/" + args[1] + ".yml");
                            YamlConfiguration editSlotYml = YamlConfiguration.loadConfiguration(editSlot);
                            int slotNum = editSlotYml.getConfigurationSection("slot." + args[3] + ".reward").getKeys(false).size();
                            for (int i = 1; i <= slotNum; i++) {
                                tab.add(String.valueOf(i));
                            }
                            return tab;
                        } else if (args[4].equals("percentage")) {
                            List<String> tab = new ArrayList<>();
                            for (int i = 1; i <= 100; i++) {
                                tab.add(String.valueOf(i));
                            }
                            return tab;
                        }
                    }
                }
            } else if (args.length == 7) {
                if (args[0].equals("edit")) {
                    if (args[2].equals("slot")) {
                        if (args[4].equals("reward")) {
                            List<String> tab = new ArrayList<>();
                            tab.add("money");
                            tab.add("item");
                            return tab;
                        }
                    }
                }
            } else if (args.length == 8) {
                if (args[0].equals("edit")) {
                    if (args[2].equals("slot")) {
                        if (args[4].equals("reward")) {
                            if (args[6].equals("money")) {
                                List<String> tab = new ArrayList<>();
                                for (int i = 1; i <= 64; i++) {
                                    tab.add(String.valueOf(i));
                                }
                                return tab;
                            } else if (args[6].equals("item")) {
                                List<String> tab = new ArrayList<>();
                                for (Material material : Material.values()) {
                                    tab.add(material.toString());
                                }
                                return tab;
                            }
                        }
                    }
                }
            } else if (args.length == 9) {
                if (args[0].equals("edit")) {
                    if (args[2].equals("slot")) {
                        if (args[4].equals("reward")) {
                            if (args[6].equals("item")) {
                                List<String> tab = new ArrayList<>();
                                for (int i = 1; i <= 64; i++) {
                                    tab.add(String.valueOf(i));
                                }
                                return tab;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }
}

