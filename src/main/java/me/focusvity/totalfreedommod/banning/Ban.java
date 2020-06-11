package me.focusvity.totalfreedommod.banning;

import cn.nukkit.Player;
import cn.nukkit.utils.ConfigSection;
import cn.nukkit.utils.TextFormat;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import me.focusvity.totalfreedommod.util.FUtil;

import java.text.SimpleDateFormat;
import java.util.List;

public class Ban
{

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss");

    @Getter
    private final String configKey;
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private List<String> ips = Lists.newArrayList();
    @Getter
    @Setter
    private String by;
    @Getter
    @Setter
    private String reason;
    @Getter
    @Setter
    private long expiry = -1;

    public Ban(Player player)
    {
        configKey = player.getName().toLowerCase();
        this.name = player.getName();
        this.ips.add(player.getAddress());
    }

    public Ban(String key)
    {
        this.configKey = key;
    }

    public String getKickMessage()
    {
        return TextFormat.RED + "You're currently banned from this server"
                + "\nReason: " + TextFormat.YELLOW + (reason != null ? reason : "N/A") + TextFormat.RED
                + "\nBanned by: " + TextFormat.YELLOW + by + TextFormat.RED
                + "\nYour ban will expire on " + TextFormat.YELLOW + FUtil.getUnixDate(expiry);
    }

    public boolean isExpired()
    {
        return hasExpiry() && expiry < FUtil.getUnixTime();
    }

    public boolean hasExpiry()
    {
        return expiry > 0;
    }

    public void saveTo(ConfigSection section)
    {
        section.set("username", name);
        section.set("ips", Lists.newArrayList(ips));
        section.set("reason", reason);
        section.set("by", by);
        section.set("expiry", expiry > 0 ? expiry : null);
    }

    public void loadFrom(ConfigSection section)
    {
        name = section.getString("username", null);
        ips.addAll(section.getStringList("ips"));
        reason = section.getString("reason", null);
        by = section.getString("by", null);
        expiry = section.getLong("expiry", 0);
    }
}
