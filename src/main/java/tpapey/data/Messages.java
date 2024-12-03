package tpapey.data;

import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.text.Normalizer;
import java.util.function.Function;

public final class Messages {
    private Messages() {}

    public static final Text NO_DEFAULT_TARGET =
            Text.literal("No default tpa target specified\nUse ")
            .styled(style -> style.withColor(Formatting.RED))
            .append(Text.literal("/tpadefault set [player]")
                    .styled(style -> style
                            .withColor(Formatting.AQUA)
                            .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tpadefault set "))
                    )
            )
            .append(Text.literal(" to set it")
                    .styled(style -> style
                            .withColor(Formatting.RED))
            );

    public static final Function<String, Text> DEFAULT_TARGET_SET =
            name -> Text.literal("Default tpa target set to ")
                    .append(Text.literal(name).styled(style -> style
                            .withColor(Formatting.AQUA))
                    );

    public static final Function<String, Text> DEFAULT_TARGET =
            name -> Text.literal("Default tpa target is ").
                    append(Text.literal(name).styled(style -> style
                            .withColor(Formatting.AQUA))
                    );

    public static final Text DEFAULT_TARGET_REMOVED =
            Text.literal("Default tpa target removed");

    public static final Function<String, Text> REQUEST_SENT =
            name -> Text.literal("Request sent to ")
                    .append(Text.literal(name).styled(style -> style
                            .withColor(Formatting.AQUA))
                    );

    public static final Text REQUEST_ACCEPTED =
            Text.literal("Request accepted")
                    .styled(style -> style
                            .withColor(Formatting.RED)
                    );

    public static final Text REQUEST_REJECTED =
            Text.literal("Request rejected")
                    .styled(style -> style
                            .withColor(Formatting.GREEN)
                    );

    public static final Text NO_PENDING_REQUESTS =
            Text.literal("No pending requests")
                    .styled(style -> style
                            .withColor(Formatting.RED)
                    );
}
