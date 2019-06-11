package main;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipClassLoader extends ClassLoader {
    private final ZipFile file;

    /*
     *Well i got this from the Internet, i can't comment it, even if I know how this works 
     *
     */
    
    public ZipClassLoader(String filename) throws IOException {
        this.file = new ZipFile(filename);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        ZipEntry entry = this.file.getEntry(name.replace('.', '/') + ".class");
        if (entry == null) {
            throw new ClassNotFoundException(name);
        }
        try {
            byte[] array = new byte[1024];
            InputStream in = this.file.getInputStream(entry);
            ByteArrayOutputStream out = new ByteArrayOutputStream(array.length);
            int length = in.read(array);
            while (length > 0) {
                out.write(array, 0, length);
                length = in.read(array);
            }
            return defineClass(name, out.toByteArray(), 0, out.size());
        }
        catch (IOException exception) {
            throw new ClassNotFoundException(name, exception);
        }
    }
    
    protected URL findResource(String name) {
        ZipEntry entry = this.file.getEntry(name);
        if (entry == null) {
            return null;
        }
        try {
            return new URL("jar:file:" + this.file.getName() + "!/" + entry.getName());
        }
        catch (MalformedURLException exception) {
            return null;
        }
    }
    
    @Override
    protected Enumeration<URL> findResources(final String name) {
        return new Enumeration<URL>() {
            private URL element = findResource(name);

            public boolean hasMoreElements() {
                return this.element != null;
            }

            public URL nextElement() {
                if (this.element != null) {
                    URL element = this.element;
                    this.element = null;
                    return element;
                }
                throw new NoSuchElementException();
            }
        };
    }
}
