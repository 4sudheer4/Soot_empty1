import java.awt.Desktop;
import java.io.*;
import java.net.URI;

class GFG {
    public static void main(String[] args)
            throws Exception
    {
        Desktop desk = Desktop.getDesktop();

        // now we enter our URL that we want to open in our
        // default browser
        desk.browse(new URI("http://www.google.com"));
    }
}
