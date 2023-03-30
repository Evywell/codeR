namespace Sandbox.Tools;

public static class DumpExtension {
    public static void Dump(this object subject, string name = "") {
        using (var writter = new System.IO.StringWriter())
        {
            ObjectDumper.Dumper.Dump(subject, name, writter);

            Console.Write(writter.ToString());
        }
    }
}