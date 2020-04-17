package com.vapor05.dataloom.util;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;

/**
 *
 * @author NicholasBocchini
 */
public class MavenClassLoader extends URLClassLoader {
    
     private HashMap<String, HashMap<String, String>> loaded;
     private String root;
     
     public MavenClassLoader(ClassLoader parent)
     {
        super(new URL[0], parent);
        
        this.loaded = new HashMap<String, HashMap<String, String>>();
        this.root = System.getProperty("loom.url.root");
        
        if (root == null) root = System.getProperty("user.home") + "/.m2/repository";
     }
     
     
}
