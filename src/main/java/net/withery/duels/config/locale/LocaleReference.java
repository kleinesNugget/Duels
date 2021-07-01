package net.withery.duels.config.locale;

public enum LocaleReference {

    PREFIX("prefix", "&eDuels &8» &r"),
    NO_PERMISSION("no-permission", "&cInsufficient permission!"),
    INVALID_COMMAND("invalid-command", "&cInvalid command!"),

    ARENA_SETUP_ARENA_START("arena.setup.arena.start", "&7Enter a name for the arena in the chat. \"&eCancel&7\" to cancel."),
    ARENA_SETUP_ARENA_DONE("arena.setup.arena.done", "&7Created the arena &e%name%.", "name"),
    ARENA_SETUP_ARENA_CANCELLED("arena.setup.arena.cancelled", "&cThe arena creation was cancelled!"),
    ARENA_SETUP_ARENA_ENABLE("arena.setup.arena.enable", "&7The arena &e%arena% &7has been enabled.", "arena"),
    ARENA_SETUP_ARENA_DISABLE("arena.setup.arena.disable", "&7The arena &e%arena% &7has been disabled.", "arena"),
    ARENA_SETUP_ARENA_DELETE("arena.setup.arena.delete", "&7The arena &e%arena% &7has been deleted.", "arena"),
    ARENA_SETUP_NAME_EXISTS("arena.setup.name.exists", "&cThere is already an arena with that name!"),
    ARENA_SETUP_NAME_START("arena.setup.name.start", "&7Enter a new name in the chat. \"&eCancel&7\" to cancel."),
    ARENA_SETUP_NAME_DONE("arena.setup.name.done", "&7Changed the arena name to &e%name%.", "name"),
    ARENA_SETUP_POSITION_START("arena.setup.position.start", "&7Left click a block at corner &e%corner%. &7Type \"&eCancel&7\" to cancel.", "corner"),
    ARENA_SETUP_POSITION_DONE("arena.setup.position.done", "&7Corner &e%corner% &7has been set.", "corner"),
    ARENA_SETUP_POSITION_UNSET("arena.setup.position.unset", "&7Corner &e%corner% &7has been unset.", "corner"),
    ARENA_SETUP_SPAWN_START("arena.setup.spawn.start", "&7Stand at spawn &e%spawn% &7and type \"&eConfirm&7\". \"&eCancel&7\" to cancel.", "spawn"),
    ARENA_SETUP_SPAWN_DONE("arena.setup.spawn.done", "&7Spawn &e%spawn% &7has been set.", "spawn"),
    ARENA_SETUP_SPAWN_UNSET("arena.setup.spawn.unset", "&7Spawn &e%spawn% &7has been removed.", "spawn"),
    ARENA_SETUP_SPAWN_POSITION_NEEDED("arena.setup.spawn.position.needed", "&cYou need to set both corners in order to set a spawn!"),
    ARENA_SETUP_SPAWN_INSIDE_ARENA("arena.setup.spawn.inside-arena", "&cThe spawn needs to be inside of the arena!"),
    ARENA_SETUP_EXPIRED("arena.setup.expired", "&cThe arena setup action expired."),
    ARENA_SETUP_CANCEL("arena.setup.cancel", "&cCancelled the setup!");

    private final String reference;
    private final String defaultValue;
    private final String[] variables;

    LocaleReference(String reference, String defaultValue, String... variables) {
        this.reference = reference;
        this.defaultValue = defaultValue;
        this.variables = variables;
    }

    public String getReference() {
        return reference;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public String[] getVariables() {
        return variables;
    }

}