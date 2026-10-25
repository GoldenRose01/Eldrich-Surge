namespace EldritchConfigTool;

internal static class Program
{
    [STAThread]
    private static void Main()
    {
        ApplicationConfiguration.Initialize();
        Application.Run(new ConfigForm());
    }
}

internal enum EnchantCategory
{
    Weapons,
    Defense,
    Archery,
    Tools,
    Misc
}

