import { defineConfig } from "vite";
import uni from "@dcloudio/vite-plugin-uni";
import { visualizer } from "rollup-plugin-visualizer";

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    uni(),
    visualizer()
  ],
  css: {
    preprocessorOptions: {
      scss: {
        // 取消sass废弃API的报警
        silenceDeprecations: ['legacy-js-api', 'color-functions', 'import'],
        additionalData: `@import "@/uni.scss";`
      },
    },
  },
  server: {
    port: 5100,
    fs: {
      // Allow serving files from one level up to the project root
      allow: ['..']
    },
    // 添加代理配置
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        secure: false,
        // 添加更多CORS相关配置
        configure: (proxy, options) => {
          proxy.on('proxyReq', (proxyReq, req, res) => {
            // 添加必要的请求头
            proxyReq.setHeader('Origin', 'http://localhost:5100');
            proxyReq.setHeader('Referer', 'http://localhost:5100');
          });
        },
        // 如果后端接口没有 /api 前缀，可以重写路径
        // rewrite: (path) => path.replace(/^\/api/, '')
      }
    }
  },
  optimizeDeps: {
    include: ['uview-plus']
  }
});
