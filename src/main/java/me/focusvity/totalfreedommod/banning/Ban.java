package me.focusvity.totalfreedommod.banning;

import cn.nukkit.Player;
import cn.nukkit.utils.ConfigSection;
import cn.nukkit.utils.TextFormat;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import me.focusvity.totalfreedommod.util.FUtil;

import java.util.Date;
import java.util.List;

public class Ban
{

    @Getter
    private String configKey;
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
    private Date expiry;

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
                + "\nYour ban will expire on " + TextFormat.YELLOW + FUtil.parseDateToString(expiry);
    }

    public boolean isExpired()
    {
        if (expiry == null)
        {
            return false;
        }

        return expiry.after(new Date());
    }

    public void saveTo(ConfigSection section)
    {
        section.set("username", name);
        section.set("ips", Lists.newArrayList(ips));
        section.set("reason", reason);
        section.set("by", by);
        section.set("expiry", FUtil.getUnixTime(expiry));
    }

    public void loadFrom(ConfigSection section)
    {
        name = section.getString("username", name);
        ips.addAll(section.getStringList("ips"));
        reason = section.getString("reason", reason);
        by = section.getString("by", by);
        expiry = FUtil.getUnixDate(section.getLong("expiry"));
    }
}
