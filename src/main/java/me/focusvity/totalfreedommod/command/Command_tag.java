package me.focusvity.totalfreedommod.command;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import me.focusvity.totalfreedommod.PlayerData;
import me.focusvity.totalfreedommod.admin.AdminList;
import me.focusvity.totalfreedommod.rank.Rank;
import me.focusvity.totalfreedommod.util.FUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

@CommandParameters(description = "Set your own fancy tag", usage = "/<command> <tag>", rank = Rank.OP, source = SourceType.PLAYER)
public class Command_tag
{

    public static final List<String> FORBIDDEN_WORDS = Arrays.asList(
            "admin", "owner", "moderator", "developer", "console", "dev", "staff", "mod", "sra", "tca", "sta", "sa");

    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args)
    {
        if (args.length < 1)
        {
            return false;
        }

        if (args.length == 1)
        {
            if (args[0].equalsIgnoreCase("off"))
            {
                PlayerData.getData(sender).setTag(null);
                sender.sendMessage(TextFormat.GRAY + "Your tag has been removed!");
                return true;
            }
            return false;
        }
        else if (args.length >= 2)
        {
            if (args[0].equalsIgnoreCase("set"))
            {
                String input = StringUtils.join(args, " ", 1, args.length);
                String rawTag = TextFormat.clean(input);

                if (rawTag.length() > 20)
                {
                    sender.sendMessage(TextFormat.RED + "That tag is too long. (20 characters maximum)");
                    return true;
                }

                if (!AdminList.isAdmin(sender))
                {
                    for (String word : FORBIDDEN_WORDS)
                    {
                        if (rawTag.contains(word))
                        {
                            sender.sendMessage(TextFormat.RED + "That tag contains a forbidden word!");
                            return true;
                        }
                    }
                }

                PlayerData.getData(sender).setTag(FUtil.colorize(input));
                sender.sendMessage(TextFormat.GRAY + "Tag set to " + FUtil.colorize(input));
                return true;
            }
        }

        return false;
    }
}
