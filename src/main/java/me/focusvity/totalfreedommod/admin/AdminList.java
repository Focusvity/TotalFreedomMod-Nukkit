package me.focusvity.totalfreedommod.admin;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.Config;
import com.google.common.collect.Lists;
import lombok.Getter;
import me.focusvity.totalfreedommod.TotalFreedomMod;
import me.focusvity.totalfreedommod.rank.Rank;

import java.util.List;

public class AdminList
{

    @Getter
    public static List<String> imposters;
    @Getter
    private static List<Admin> admins;
    private static Config config;
    private static TotalFreedomMod plugin;

    public AdminList(TotalFreedomMod plugin)
    {
        this.plugin = plugin;
        imposters = Lists.newArrayList();
        imposters.clear();
        admins = Lists.newArrayList();
        admins.clear();
        config = plugin.admins;
        load();
    }

    public static void load()
    {
        admins.clear();

        for (String key : config.getKeys(false))
        {
            if (config.getSection(key) == null)
            {
                plugin.getLogger().warning("Could not load admin: " + key + ". Invalid section format!");
                continue;
            }

            Admin admin = new Admin(key);
            admin.loadFrom(config.getSection(key));

            if (!admin.isValid())
            {
                plugin.getLogger().warning("Could not load admin: " + key + ". Missing information!");
                continue;
            }

            admins.add(admin);
        }

        plugin.getLogger().info("Successfully loaded " + admins.size() + " admins!");
    }

    public static boolean isAdmin(Admin admin)
    {
        final String key = admin.getConfigKey();
        for (Admin check : admins)
        {
            return check.equals(key);
        }
        return false;
    }

    public static boolean isAdmin(Player player)
    {
        return getAdmin(player) != null
                && getAdmin(player).isActive();
    }

    public static boolean isAdmin(CommandSender sender)
    {
        if (sender instanceof Player)
        {
            return isAdmin((Player) sender);
        }
        return true;
    }

    public static Admin getAdmin(Player player)
    {
        for (Admin admin : admins)
        {
            if (player.getName().equals(admin.getName()))
            {
                return admin;
            }
        }
        return null;
    }

    public static void addAdmin(Admin admin)
    {
        if (isAdmin(admin))
        {
            return;
        }

        admins.add(admin);
        config.set(admin.getConfigKey() + ".username", admin.getName());
        admin.saveTo(config.getSection(admin.getConfigKey()));
        config.save();
    }

    // Remove an admin's entry
    public static void removeAdmin(Admin admin)
    {
        if (!isAdmin(admin))
        {
            return;
        }

        admins.remove(admin);
        config.set(admin.getConfigKey(), null);
        config.save();
    }

    // Disable an admin's entry
    public static void deactivateAdmin(Admin admin)
    {
        admins.remove(admin);
        admin.setActive(false);
        admin.saveTo(config.getSection(admin.getConfigKey()));
        admins.add(admin);
    }

    public static void updateRank(Admin admin, Rank rank)
    {
        admins.remove(admin);
        admin.setRank(rank);
        admin.saveTo(config.getSection(admin.getConfigKey()));
        admins.add(admin);
    }

    public static void addIp(Player player, String ip)
    {
        Admin admin = getAdmin(player);

        if (admin == null)
        {
            return;
        }

        if (admin.getIps().contains(ip))
        {
            return;
        }

        admins.remove(admin);
        admin.addIp(ip);
        admin.saveTo(config.getSection(admin.getConfigKey()));
        admins.add(admin);
    }

    public static boolean isImposter(Player player)
    {
        return imposters.contains(player.getName());
    }

    public void save()
    {
        for (String key : config.getKeys(false))
        {
            config.set(key, null);
        }

        for (Admin admin : admins)
        {
            config.set(admin.getConfigKey() + ".username", admin.getName());
            admin.saveTo(config.getSection(admin.getConfigKey()));
            config.save();
        }
    }
}
