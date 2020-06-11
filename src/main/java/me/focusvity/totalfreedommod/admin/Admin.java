package me.focusvity.totalfreedommod.admin;

import cn.nukkit.Player;
import cn.nukkit.utils.ConfigSection;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import me.focusvity.totalfreedommod.rank.Rank;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class Admin
{

    @Getter
    @Setter
    private final List<String> ips = Lists.newArrayList();
    @Getter
    private final String configKey;
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private Rank rank = Rank.SUPER_ADMIN;
    @Getter
    @Setter
    private boolean active = true;
    @Getter
    @Setter
    private boolean commandSpyEnabled = true;

    public Admin(Player player)
    {
        configKey = player.getName().toLowerCase();
        name = player.getName();
        ips.add(player.getAddress());
    }

    public Admin(String configKey)
    {
        this.configKey = configKey;
    }

    @Override
    public String toString()
    {
        final StringBuilder s = new StringBuilder();

        s.append("Admin: ").append(name).append("\n")
                .append("IPs: ").append(StringUtils.join(ips, ", ")).append("\n")
                .append("Rank: ").append(rank.getName()).append("\n")
                .append("Active: ").append(active).append("\n")
                .append("Command Spy: ").append(commandSpyEnabled);

        return s.toString();
    }

    public void loadFrom(ConfigSection c)
    {
        name = c.getString("username", configKey);
        ips.addAll(c.getStringList("ips"));
        rank = Rank.findRank(c.getString("rank"));
        active = c.getBoolean("active", true);
        commandSpyEnabled = c.getBoolean("cmdspy", true);
    }

    public void saveTo(ConfigSection c)
    {
        c.set("username", name);
        c.set("ips", Lists.newArrayList(ips));
        c.set("rank", rank.toString());
        c.set("active", active);
        c.set("cmdspy", commandSpyEnabled);
    }

    public void addIp(String ip)
    {
        if (!ips.contains(ip))
        {
            ips.add(ip);
        }
    }

    public void addIps(List<String> ips)
    {
        for (String ip : ips)
        {
            addIp(ip);
        }
    }

    public void removeIp(String ip)
    {
        ips.remove(ip);
    }

    public void clearIPs()
    {
        ips.clear();
    }

    public boolean isValid()
    {
        return configKey != null
                && name != null
                && rank != null
                && !ips.isEmpty();
    }
}
