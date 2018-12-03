package me.focusvity.totalfreedommod.banning;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.Config;
import com.google.common.collect.Lists;
import lombok.Getter;
import me.focusvity.totalfreedommod.TotalFreedomMod;

import java.util.Date;
import java.util.List;

public class BanManager
{

    @Getter
    private static List<Ban> bans;
    private static Config config;
    private static TotalFreedomMod plugin;

    public BanManager(TotalFreedomMod plugin)
    {
        this.plugin = plugin;
        bans = Lists.newArrayList();
        bans.clear();
        config = plugin.bans;
        load();
    }

    public static void load()
    {
        bans.clear();

        for (String key : config.getKeys(false))
        {
            Ban ban = new Ban(key);
            ban.loadFrom(config.getSection(key));

            bans.add(ban);
        }

        plugin.getLogger().info("Successfully loaded " + bans.size() + " bans!");
    }

    public static void save()
    {
        for (String key : config.getKeys(false))
        {
            config.set(key, null);
        }

        for (Ban ban : bans)
        {
            config.set(ban.getConfigKey() + ".username", ban.getName());
            ban.saveTo(config.getSection(ban.getConfigKey()));
            config.save();
        }
    }

    public static void addBan(CommandSender sender, Player player, String reason, Date date)
    {
        if (isBanned(player))
        {
            return;
        }

        Ban ban = new Ban(player);
        ban.setBy(sender.getName());
        ban.setReason(reason);
        ban.setExpiry(date);
        addBan(ban);
    }

    public static void addBan(CommandSender sender, String name, String ip, String reason, Date date)
    {
        if (getBan(name, ip) != null)
        {
            return;
        }

        Ban ban = new Ban(name);
        ban.setIps(Lists.newArrayList(ip));
        ban.setBy(sender.getName());
        ban.setReason(reason);
        ban.setExpiry(date);
        addBan(ban);
    }

    public static void removeBan(Ban ban)
    {
        if (!isBanned(ban))
        {
            return;
        }

        bans.remove(ban);
    }

    public static void removeExpiredBan()
    {
        for (Ban ban : bans)
        {
            if (ban.isExpired())
            {
                bans.remove(ban);
            }
        }
    }

    public static void addBan(Ban ban)
    {
        if (isBanned(ban))
        {
            return;
        }

        bans.add(ban);
        ban.saveTo(config.getSection(ban.getConfigKey()));
    }

    public static Ban getBan(Player player)
    {
        for (Ban ban : bans)
        {
            if (ban.getName() != null)
            {
                if (ban.getName().equalsIgnoreCase(player.getName()))
                {
                    return ban;
                }
            }

            if (ban.getIps() != null)
            {
                if (ban.getIps().contains(player.getAddress()))
                {
                    return ban;
                }
            }
        }
        return null;
    }

    public static Ban getBan(String name, String ip)
    {
        for (Ban ban : bans)
        {
            if (ban.getName() != null)
            {
                if (ban.getName().equalsIgnoreCase(name))
                {
                    return ban;
                }
            }

            if (ban.getIps() != null)
            {
                if (ban.getIps().contains(ip))
                {
                    return ban;
                }
            }
        }
        return null;
    }

    public static Ban getBan(String string, boolean ip)
    {
        for (Ban ban : bans)
        {
            if (ip)
            {
                if (ban.getIps().contains(string))
                {
                    return ban;
                }
            }
            else
            {
                if (ban.getName().equalsIgnoreCase(string))
                {
                    return ban;
                }
            }
        }
        return null;
    }

    public static boolean isBanned(Ban ban)
    {
        removeExpiredBan();
        for (Ban b : bans)
        {
            if (b.getName().equals(ban.getName()) || b.getIps().contains(ban.getIps().get(0)))
            {
                return true;
            }
        }
        return false;
    }

    public static boolean isBanned(Player player)
    {
        removeExpiredBan();
        return getBan(player) != null;
    }
}
