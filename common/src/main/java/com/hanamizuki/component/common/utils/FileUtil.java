package com.hanamizuki.component.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


public class FileUtil {
    private static final Logger log = LoggerFactory.getLogger(PropertiesUtil.class);

    /**
     * 原子写入文件（覆盖）
     */
    public static boolean writeAtomic(String path,
                                      String content,
                                      Set<PosixFilePermission> permissions){
        Path tempFile = null;
        try {
            Path target = Paths.get(path);
            Path parent = target.getParent();

            if (parent != null && Files.notExists(parent)) {
                Files.createDirectories(parent);
            }

            assert parent != null;
            tempFile = Files.createTempFile(
                    parent,
                    target.getFileName().toString(),
                    ".tmp"
            );

            Files.write(
                    tempFile,
                    content.getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.TRUNCATE_EXISTING
            );

            if (permissions != null) {
                try {
                    Files.setPosixFilePermissions(tempFile, permissions);
                } catch (UnsupportedOperationException ignored) {
                }
            }

            Files.move(
                    tempFile,
                    target,
                    StandardCopyOption.ATOMIC_MOVE,
                    StandardCopyOption.REPLACE_EXISTING
            );

            return true;

        } catch (Exception e) {

            log.error("Could not write atomic file {}, error: {}", path, e.toString());

        } finally {

            if (tempFile != null && Files.exists(tempFile)) {
                try {
                    Files.delete(tempFile);
                } catch (IOException ignored) {
                }
            }
        }

        return false;
    }

    /**
     * 原子写入文件（二进制版）
     */
    public static boolean writeAtomic(String path,
                                      byte[] content,
                                      Set<PosixFilePermission> permissions) {
        Path tempFile = null;
        try {
            Path target = Paths.get(path);
            Path parent = target.getParent();

            if (parent != null && Files.notExists(parent)) {
                Files.createDirectories(parent);
            }

            assert parent != null;
            tempFile = Files.createTempFile(
                    parent,
                    target.getFileName().toString(),
                    ".tmp"
            );

            Files.write(tempFile, content);

            if (permissions != null) {
                try {
                    Files.setPosixFilePermissions(tempFile, permissions);
                } catch (UnsupportedOperationException ignored) {

                }
            }

            Files.move(
                    tempFile,
                    target,
                    StandardCopyOption.ATOMIC_MOVE,
                    StandardCopyOption.REPLACE_EXISTING
            );

            return true;

        } catch (Exception e) {
            log.error("Failed to write atomic binary file {}, error: {}", path, e.getMessage());
        } finally {

            if (tempFile != null && Files.exists(tempFile)) {
                try {
                    Files.delete(tempFile);
                } catch (IOException ignored) {
                }
            }
        }
        return false;
    }


    public static boolean appendAtomic(String path,
                                        String content,
                                        Set<PosixFilePermission> permissions){

        Path tempFile = null;
        try {
            Path target = Paths.get(path);
            Path parent = target.getParent();

            if (parent != null && Files.notExists(parent)) {
                Files.createDirectories(parent);
            }

            if (parent == null) {
                parent = Paths.get(".").toAbsolutePath().normalize();
            }

            tempFile = Files.createTempFile(parent, "tmp-", ".tmp");

            if (Files.exists(target)) {
                List<String> existing = Files.readAllLines(target, StandardCharsets.UTF_8);
                existing.add(content); // 追加新内容
                Files.write(tempFile, existing, StandardCharsets.UTF_8);
            } else {
                Files.write(tempFile, Collections.singletonList(content), StandardCharsets.UTF_8);
            }

            if (permissions != null) {
                try {
                    Files.setPosixFilePermissions(tempFile, permissions);
                } catch (UnsupportedOperationException ignored) { }
            }

            Files.move(tempFile, target, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);

            return true;

        } catch (Exception e) {

            log.error("Could not append atomic file {}, error: {}", path, e.toString());

        } finally {

            if (tempFile != null && Files.exists(tempFile)) {
                try {
                    Files.delete(tempFile);
                } catch (IOException ignored) { }
            }
        }

        return false;
    }

    public static void zip(HttpServletResponse response, List<String> filePaths, String zipName, Integer zipEntryLevel) {
        try {
            response.setContentType("application/zip");
            String encodedFileName = URLEncoder.encode(zipName, StandardCharsets.UTF_8.toString()).replaceAll("\\+", "%20");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedFileName + "\"");
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
        } catch (UnsupportedEncodingException e) {
            log.error("Failed to code filename.", e);
        }

        try (ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(response.getOutputStream()))) {
            zos.setLevel(zipEntryLevel == null ? ZipOutputStream.DEFLATED : zipEntryLevel);

            for (String path : filePaths) {
                File file = new File(path);
                if (!file.exists() || !file.isFile()) {
                    log.warn("File is not exist or invalid, skip: {}", path);
                    continue;
                }

                ZipEntry zipEntry = new ZipEntry(file.getName());
                zos.putNextEntry(zipEntry);

                try (FileInputStream fis = new FileInputStream(file)) {
                    byte[] buffer = new byte[8192];
                    int length;
                    while ((length = fis.read(buffer)) >= 0) {
                        zos.write(buffer, 0, length);
                    }
                }

                zos.closeEntry();
            }
            zos.flush();
        } catch (IOException e) {
            log.error("Generate zip outputStream error.", e);
        }
    }

    public static void zip(OutputStream outputStream, List<String> filePaths, Integer zipEntryLevel) {
        try (ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(outputStream))) {
            zos.setLevel(zipEntryLevel == null ? ZipOutputStream.DEFLATED : zipEntryLevel);

            for (String path : filePaths) {
                File file = new File(path);
                if (!file.exists() || !file.isFile()) continue;

                ZipEntry zipEntry = new ZipEntry(file.getName());
                zos.putNextEntry(zipEntry);

                Files.copy(file.toPath(), zos);

                zos.closeEntry();
            }
            zos.finish();
        } catch (IOException e) {
            log.error("Zip process failed", e);
        }
    }

}
