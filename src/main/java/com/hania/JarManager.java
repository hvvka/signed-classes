package com.hania;

/**
 * @author <a href="mailto:226154@student.pwr.edu.pl">Hanna Grodzicka</a>
 */
public class JarManager {

    public static void main(String[] args) {
        String prefix = "jar:";
        String jarPath = "file:/Users/hg/pwr/6sem/pwjjtz/lab-culer/lab7/src/main/resources/";
        String jarName = "test.jar";

        JarLoader jarLoader = new JarLoader(jarPath);
        jarLoader.readJarFile(jarName);
        String className = jarLoader.getClassNames().get(0);

        ClassEncryptor classEncryptor = new ClassEncryptor(prefix + jarPath + jarName);
//        classEncryptor.encrypt(className, jarLoader.getClassBytes(className));
        classEncryptor.decrypt(className, jarLoader.getClassBytes(className));
    }
}
