/// <reference types='@dcloudio/types' />
import 'vue'

declare module '@vue/runtime-core' {
  type Hooks = App.AppInstance & Page.PageInstance;

  interface ComponentCustomOptions extends Hooks {

  }
  
  // uView Plus 组件类型声明
  export interface GlobalComponents {
    UButton: any
    UInput: any
    UForm: any
    UFormItem: any
    UIcon: any
    UText: any
    UAvatar: any
    UCard: any
    UList: any
    UListItem: any
    URadio: any
    URadioGroup: any
    UCheckbox: any
    UCheckboxGroup: any
    UPicker: any
    UActionSheet: any
    UModal: any
    UToast: any
    ULoading: any
    ULoadingIcon: any
    UTransition: any
    USteps: any
    UTag: any
    UDivider: any
    UTabbar: any
    UTabs: any
    UPopup: any
    USwiper: any
    UUpload: any
    USlider: any
    USwitch: any
    URate: any
    UBadge: any
    UAlert: any
    UNoticeBar: any
    UNavbar: any
    USubsection: any
    URow: any
    UCol: any
    UCellGroup: any
    UCell: any
    USwipeAction: any
    UCollapse: any
    UAlbum: any
    UCalendar: any
    UTimeLine: any
  }
}

declare module 'uview-plus' {
  import { App } from 'vue'
  
  interface UViewPlus {
    install(app: App): void
  }
  
  const uviewPlus: UViewPlus
  export default uviewPlus
}
