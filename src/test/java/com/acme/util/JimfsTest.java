package com.acme.util;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;

import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.util.List;

/**
 * Trying out th e Google Jimfs (Java in-memory file system)
 */
@TestMethodOrder(OrderAnnotation.class)
@Slf4j
class JimfsTest {

    private static FileSystem fileSystem;

    @BeforeAll
    static void beforeAll() {
        LOG.info("Testing Jimfs (Java in-memory file system)");

        fileSystem = Jimfs.newFileSystem(Configuration.forCurrentPlatform());
        LOG.info("Created Jimfs FileSystem: {}", fileSystem);
        LOG.info("Jimfs FileSystem Provider: {}", fileSystem.provider());
        logSeparatorLine();
    }

    @AfterEach
    void afterEach() {
        logSeparatorLine();
    }

    @Test
    @Order(1)
    void shouldListRootDirectory() throws IOException {
        var root = fileSystem.getPath("/");
        var files = Files.list(root).collect(toList());

        LOG.info("Files in /:");
        files.forEach(file -> LOG.info("{}", file));

        assertThat(files).isNotEmpty();
    }

    @Test
    @Order(2)
    void shouldCreateNewDirectory() throws IOException {
        var data = fileSystem.getPath("/data");
        var createdPath = Files.createDirectory(data);

        LOG.info("Created directory: {}", createdPath);

        assertThat(createdPath).exists();
    }

    @Test
    @Order(3)
    void shouldCreateNewFile() throws IOException {
        var data = fileSystem.getPath("/data");
        var readme = data.resolve("README.md");

        LOG.info("Writing content to new file {}", readme);

        var pathWrittenTo = Files.write(readme, List.of(
            "My Project",
            "----------",
            "Welcome! This is my project."
        ));

        assertThat(pathWrittenTo).isEqualTo(readme);
    }

    @Test
    @Order(4)
    void shouldReadExistingFile() throws IOException {
        var readme = fileSystem.getPath("/data", "README.md");
        var lines = Files.lines(readme).collect(toList());

        LOG.info("Contents of {}:", readme);
        lines.forEach(line -> LOG.info("{}", line));

        assertThat(lines).containsExactly(
            "My Project",
            "----------",
            "Welcome! This is my project."
        );
    }

    private static void logSeparatorLine() {
        LOG.info("--------------------------------------------------------------------------------");
    }
}
