package com.heartplan.util;

import de.androidpit.colorthief.ColorThief;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 图片颜色提取工具类
 * 使用ColorThief-Java库提取图片的主要颜色
 * 
 * @author HeartPlan Team
 */
@Slf4j
public class ColorExtractionUtil {

    private static final String DEFAULT_COLOR = "#FF6B6B";

    /**
     * 提取图片主要颜色
     * 
     * @param imageUrl 图片URL
     * @return String 主要颜色的十六进制值
     */
    public static String extractDominantColor(String imageUrl) {
        if (imageUrl == null || imageUrl.trim().isEmpty()) {
            log.debug("图片URL为空，返回默认颜色");
            return DEFAULT_COLOR;
        }

        try {
            log.info("开始提取图片主要颜色: {}", imageUrl);
            
            // 下载并读取图片
            BufferedImage image = downloadImage(imageUrl);
            if (image == null) {
                log.warn("无法下载图片: {}", imageUrl);
                return DEFAULT_COLOR;
            }

            // 使用ColorThief提取主要颜色
            int[] dominantColorRgb = ColorThief.getColor(image);
            
            if (dominantColorRgb == null || dominantColorRgb.length < 3) {
                log.warn("无法提取图片颜色: {}", imageUrl);
                return DEFAULT_COLOR;
            }

            // 转换为十六进制颜色值
            String hexColor = rgbToHex(dominantColorRgb[0], dominantColorRgb[1], dominantColorRgb[2]);
            
            log.info("图片主要颜色提取成功: {} -> {}", imageUrl, hexColor);
            return hexColor;

        } catch (Exception e) {
            log.error("提取图片主要颜色时发生错误: {}, 错误: {}", imageUrl, e.getMessage());
            return DEFAULT_COLOR;
        }
    }

    /**
     * 提取图片调色板（多个主要颜色）
     * 
     * @param imageUrl 图片URL
     * @param colorCount 要提取的颜色数量
     * @return List<String> 颜色列表（十六进制格式）
     */
    public static List<String> extractColorPalette(String imageUrl, int colorCount) {
        if (imageUrl == null || imageUrl.trim().isEmpty()) {
            log.debug("图片URL为空，返回默认调色板");
            return List.of(DEFAULT_COLOR);
        }

        try {
            log.info("开始提取图片调色板: {}, 颜色数量: {}", imageUrl, colorCount);
            
            // 下载并读取图片
            BufferedImage image = downloadImage(imageUrl);
            if (image == null) {
                log.warn("无法下载图片: {}", imageUrl);
                return List.of(DEFAULT_COLOR);
            }

            // 使用ColorThief提取调色板 - 修复这里的类型问题
            int[][] paletteArray = ColorThief.getPalette(image, Math.max(1, colorCount));
            
            if (paletteArray == null || paletteArray.length == 0) {
                log.warn("无法提取图片调色板: {}", imageUrl);
                return List.of(DEFAULT_COLOR);
            }

            // 转换为十六进制颜色值列表
            List<String> hexColors = Arrays.stream(paletteArray)
                    .map(rgb -> rgbToHex(rgb[0], rgb[1], rgb[2]))
                    .collect(Collectors.toList());
            
            log.info("图片调色板提取成功: {} -> {}", imageUrl, hexColors);
            return hexColors;

        } catch (Exception e) {
            log.error("提取图片调色板时发生错误: {}, 错误: {}", imageUrl, e.getMessage());
            return List.of(DEFAULT_COLOR);
        }
    }

    /**
     * 提取图片主要颜色（安全版本，不会抛出异常）
     * 
     * @param imageUrl 图片URL
     * @return String 主要颜色的十六进制值，失败时返回默认颜色
     */
    public static String extractDominantColorSafely(String imageUrl) {
        try {
            return extractDominantColor(imageUrl);
        } catch (Exception e) {
            log.error("安全提取图片主要颜色失败: {}, 使用默认颜色", imageUrl);
            return DEFAULT_COLOR;
        }
    }

    /**
     * 下载图片
     * 
     * @param imageUrl 图片URL
     * @return BufferedImage 图片对象
     */
    private static BufferedImage downloadImage(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            BufferedImage image = ImageIO.read(url);
            
            if (image == null) {
                log.warn("图片格式不支持或损坏: {}", imageUrl);
                return null;
            }

            log.debug("图片下载成功: {}, 尺寸: {}x{}", imageUrl, image.getWidth(), image.getHeight());
            return image;

        } catch (IOException e) {
            log.warn("下载图片失败: {}, 错误: {}", imageUrl, e.getMessage());
            return null;
        }
    }

    /**
     * RGB转十六进制颜色
     * 
     * @param r 红色分量 (0-255)
     * @param g 绿色分量 (0-255)
     * @param b 蓝色分量 (0-255)
     * @return String 十六进制颜色值 (#RRGGBB)
     */
    private static String rgbToHex(int r, int g, int b) {
        // 确保RGB值在有效范围内
        r = Math.max(0, Math.min(255, r));
        g = Math.max(0, Math.min(255, g));
        b = Math.max(0, Math.min(255, b));
        
        return String.format("#%02X%02X%02X", r, g, b);
    }

    /**
     * 验证颜色值格式
     * 
     * @param color 颜色值
     * @return boolean 是否为有效的十六进制颜色值
     */
    public static boolean isValidHexColor(String color) {
        if (color == null || color.trim().isEmpty()) {
            return false;
        }
        
        // 检查十六进制颜色格式 #RRGGBB
        return color.matches("^#[0-9A-Fa-f]{6}$");
    }

    /**
     * 检查图片URL是否为支持的格式
     * 
     * @param imageUrl 图片URL
     * @return boolean 是否为支持的图片格式
     */
    public static boolean isSupportedImageFormat(String imageUrl) {
        if (imageUrl == null || imageUrl.trim().isEmpty()) {
            return false;
        }
        
        String lowerUrl = imageUrl.toLowerCase();
        return lowerUrl.endsWith(".jpg") || 
               lowerUrl.endsWith(".jpeg") || 
               lowerUrl.endsWith(".png") || 
               lowerUrl.endsWith(".gif") ||
               lowerUrl.endsWith(".bmp") ||
               lowerUrl.endsWith(".webp");
    }

    /**
     * 颜色结果封装类
     */
    public static class ColorResult {
        private final String dominantColor;
        private final List<String> palette;

        public ColorResult(String dominantColor, List<String> palette) {
            this.dominantColor = dominantColor;
            this.palette = palette;
        }

        public String getDominantColor() {
            return dominantColor;
        }

        public List<String> getPalette() {
            return palette;
        }

        @Override
        public String toString() {
            return String.format("ColorResult{dominantColor='%s', palette=%s}", 
                    dominantColor, palette);
        }
    }

    /**
     * 提取完整的颜色信息（主色调 + 调色板）
     * 
     * @param imageUrl 图片URL
     * @param paletteSize 调色板大小
     * @return ColorResult 颜色结果
     */
    public static ColorResult extractCompleteColorInfo(String imageUrl, int paletteSize) {
        String dominantColor = extractDominantColor(imageUrl);
        List<String> palette = extractColorPalette(imageUrl, paletteSize);
        return new ColorResult(dominantColor, palette);
    }
}