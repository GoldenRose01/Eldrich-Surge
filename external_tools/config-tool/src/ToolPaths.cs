namespace EldritchConfigTool;

// Connections between the tool and its containing checkout are kept here.
internal static class ToolPaths
{
    public static string FindProjectRoot()
    {
        var directory = new DirectoryInfo(AppContext.BaseDirectory);
        while (directory != null)
        {
            if (File.Exists(Path.Combine(directory.FullName, "gradlew.bat"))
                && Directory.Exists(Path.Combine(directory.FullName, "src")))
                return directory.FullName;
            directory = directory.Parent;
        }
        throw new DirectoryNotFoundException("Keep the configurator inside the Eldrich Surge checkout.");
    }

    public static string ConfigFile(string root) =>
        Path.Combine(root, "run", "config", "eldrich-surge", "enchantment_caps.json");

    public static string IconDirectory(string root) =>
        Path.Combine(root, "external_tools", "config-tool", "mc_itemlist");

    public static IEnumerable<string> MinecraftJars(string root)
    {
        string userProfile = Environment.GetFolderPath(Environment.SpecialFolder.UserProfile);
        string[] caches =
        {
            Path.Combine(root, ".gradle", "loom-cache", "minecraftMaven", "net", "minecraft"),
            Path.Combine(userProfile, ".gradle", "caches", "fabric-loom", "minecraftMaven", "net", "minecraft")
        };
        foreach (string cache in caches)
        {
            if (!Directory.Exists(cache)) continue;
            foreach (string jar in Directory.EnumerateFiles(cache, "minecraft-client*.jar", SearchOption.AllDirectories)
                .OrderBy(path => path, StringComparer.OrdinalIgnoreCase))
                yield return jar;
        }
    }
}
