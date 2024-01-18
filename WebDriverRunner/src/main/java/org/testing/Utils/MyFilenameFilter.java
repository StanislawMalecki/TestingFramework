package org.testing.Utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testing.core.TestRunner;

import java.io.*;

public class MyFilenameFilter implements FilenameFilter
{
    private static final Logger logger = LogManager.getLogger(TestRunner.class);

    String initials;

    // constructor to initialize object
    public MyFilenameFilter(String initials)
    {
        this.initials = initials;
    }

    // overriding the accept method of FilenameFilter
    // interface
    public boolean accept(File dir, String name)
    {
        return name.startsWith(initials);
    }
    public static String getFilename(String fileName)
    {
        File directory = new File(System.getProperty("user.dir"));
        MyFilenameFilter filter = new MyFilenameFilter(fileName);

        String[] list = directory.list(filter);

        if (list == null)
        {
            logger.error("Empty directory or directory does not exists.");
        }
        else
        {
            for (String s : list)
            {
                return s;
            }
        }
        return "";
    }
}
