package me.focusvity.totalfreedommod.command;

import me.focusvity.totalfreedommod.rank.Rank;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CommandParameters
{

    String name();

    String description();

    String usage() default "/<command>";

    String aliases() default ""; // alias1, alias2, alias3 etc

    Rank rank();

    SourceType source();
}
