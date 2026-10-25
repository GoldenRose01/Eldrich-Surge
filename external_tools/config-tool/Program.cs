using System.IO.Compression;
using System.Text.Json;
using System.Text.Json.Nodes;
using System.Text.RegularExpressions;

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

