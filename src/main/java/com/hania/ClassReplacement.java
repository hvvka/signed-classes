package com.hania;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:226154@student.pwr.edu.pl">Hanna Grodzicka</a>
 */
public class ClassReplacement {

    private static final Logger LOG = LoggerFactory.getLogger(ClassReplacement.class);

    private ClassReplacement() {
        throw new IllegalStateException("Utility class!");
    }

    static void replace(String jarPath, String className, String tmpClassPath) {
        Map<String, String> env = new HashMap<>();
        env.put("create", "true");
        LOG.debug("jarPath: {}", jarPath);
        URI uri = URI.create(jarPath);
        LOG.debug("URI: {}", uri);
        LOG.debug("className: {}", className);
        LOG.debug("tmpClassPath: {}", tmpClassPath);
        try (FileSystem fileSystem = FileSystems.newFileSystem(uri, env)) {
            Path externalClass = Paths.get(tmpClassPath);
            Path pathInJar = fileSystem.getPath(className);
            LOG.debug("externalClass: {}", externalClass);
            LOG.debug("pathInJar: {}", pathInJar);
            Files.copy(externalClass, pathInJar, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            LOG.error("", e);
        }
    }
}
