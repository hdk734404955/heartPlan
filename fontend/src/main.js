import { createSSRApp } from "vue";
import { createPinia } from 'pinia';
import App from "./App.vue";
import uviewPlus from 'uview-plus'

// #ifdef VUE3
export function createApp() {
	const app = createSSRApp(App);

	// 创建Pinia实例
	const pinia = createPinia();

	// 使用Pinia状态管理
	app.use(pinia);

	// 使用uView Plus组件库
	app.use(uviewPlus, () => {
		return {
			options: {
				// 修改$u.config对象的属性
				config: {
					// 修改默认单位为rpx，相当于执行 uni.$u.config.unit = 'rpx'
					unit: 'rpx'
				},
				props: {
					// 全局配置uView组件的默认属性
					button: {
						shape: 'round',
						throttleTime: 1000
					},
					input: {
						border: 'none',
						customStyle: {
							backgroundColor: '#FFF5F5'
						}
					}
				}
			}
		}
	});

	return {
		app,
		Pinia: pinia
	};
}
// #endif
