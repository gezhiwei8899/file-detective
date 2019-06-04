package com.gezhiwei.file.dective.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.security.MessageDigest;

/**
 * @author gezhiwei
 * @Title: CalacFileMd5Util
 * @ProjectName file-dective
 * @Description: TODO
 * @date 2019/6/416:37
 */
public class CalacFileMd5Util {

    public static String getFileMD5(File file) {
        if (!file.isFile()) {
            return null;
        }
        MessageDigest digest;
        FileInputStream in;
        byte[] buffer = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while (-1 != (len = in.read(buffer, 0, 1024))) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        BigInteger bigInt = new BigInteger(1, digest.digest());
        return bigInt.toString(16);
    }

//    public static Map<String, String> getDirMD5(File file, boolean listChild) {
//        if (!file.isDirectory()) {
//            return null;
//        }
//        //<filepath,md5>
//        Map<String, String> map = new HashMap<>();
//        String md5;
//        File files[] = file.listFiles();
//        for (File f : files) {
//            if (f.isDirectory() && listChild) {
//                map.putAll(getDirMD5(f, listChild));
//            } else {
//                md5 = getFileMD5(f);
//                if (md5 != null) {
//                    map.put(f.getPath(), md5);
//                }
//            }
//        }
//        return map;
//    }

//    public static byte[] getMD5Digits(File file) throws IOException {
//        FileInputStream inputStream = new FileInputStream(file);
//        FileChannel channel = inputStream.getChannel();
//        try {
//            MessageDigest messagedigest = MessageDigest.getInstance("MD5");
//            long position = 0;
//            long remaining = file.length();
//            MappedByteBuffer byteBuffer = null;
//            while (remaining > 0) {
//                long size = Integer.MAX_VALUE / 2;
//                if (size > remaining) {
//                    size = remaining;
//                }
//                byteBuffer = channel.map(FileChannel.MapMode.READ_ONLY, position, size);
//                messagedigest.update(byteBuffer);
//                position += size;
//                remaining -= size;
//            }
//            unMapBuffer(byteBuffer, channel.getClass());
//            return messagedigest.digest();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//            return null;
//        } finally {
//            channel.close();
//            inputStream.close();
//        }
//    }

    /**
     * JDK不提供MappedByteBuffer的释放，但是MappedByteBuffer在Full GC时才被回收，通过手动释放的方式让其回收
     *
     * @param buffer
     */
    private static void unMapBuffer(MappedByteBuffer buffer, Class channelClass) throws IOException {
        if (buffer == null) {
            return;
        }

        Throwable throwable = null;
        try {
            Method unmap = channelClass.getDeclaredMethod("unmap", MappedByteBuffer.class);
            unmap.setAccessible(true);
            unmap.invoke(channelClass, buffer);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throwable = e;
        }

        if (throwable != null) {
            throw new IOException("MappedByte buffer unmap error", throwable);
        }
    }

}
