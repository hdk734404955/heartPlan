// 表单验证工具
export class ValidationUtil {
  constructor() {
    this.rules = new Map()
  }

  // 添加验证规则
  addRule(name, validator, message) {
    this.rules.set(name, { validator, message })
  }

  // 验证单个字段
  validateField(value, rules) {
    for (const rule of rules) {
      if (typeof rule === 'string') {
        // 使用预定义规则
        const predefinedRule = this.rules.get(rule)
        if (predefinedRule && !predefinedRule.validator(value)) {
          return { valid: false, message: predefinedRule.message }
        }
      } else if (typeof rule === 'object') {
        // 自定义规则
        if (!rule.validator(value)) {
          return { valid: false, message: rule.message }
        }
      }
    }
    return { valid: true, message: '' }
  }

  // 验证表单对象
  validateForm(formData, validationRules) {
    const errors = {}
    let isValid = true

    for (const [field, rules] of Object.entries(validationRules)) {
      const result = this.validateField(formData[field], rules)
      if (!result.valid) {
        errors[field] = result.message
        isValid = false
      }
    }

    return { isValid, errors }
  }
}

// 创建验证工具实例
export const validator = new ValidationUtil()

// 预定义验证规则
validator.addRule('required', (value) => {
  return value !== null && value !== undefined && value !== ''
}, 'This field is required')

validator.addRule('email', (value) => {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
  return !value || emailRegex.test(value)
}, 'Please enter a valid email address')

validator.addRule('password', (value) => {
  const passwordRegex = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d@$!%*#?&]{8,}$/
  return !value || passwordRegex.test(value)
}, 'Password must be at least 8 characters with letters and numbers')

validator.addRule('minLength', (minLength) => ({
  validator: (value) => !value || value.length >= minLength,
  message: `Minimum length is ${minLength} characters`
}))

validator.addRule('maxLength', (maxLength) => ({
  validator: (value) => !value || value.length <= maxLength,
  message: `Maximum length is ${maxLength} characters`
}))

validator.addRule('age', (value) => {
  const age = parseInt(value)
  return !value || (age >= 18 && age <= 100)
}, 'Age must be between 18 and 100')

validator.addRule('username', (value) => {
  const usernameRegex = /^[a-zA-Z0-9_]{2,20}$/
  return !value || usernameRegex.test(value)
}, 'Username must be 2-20 characters, letters, numbers and underscore only')

validator.addRule('phone', (value) => {
  const phoneRegex = /^[1-9]\d{10}$/
  return !value || phoneRegex.test(value)
}, 'Please enter a valid phone number')

// 表单验证规则配置
export const VALIDATION_RULES = {
  // 登录表单
  login: {
    email: ['required', 'email'],
    password: ['required']
  },

  // 注册表单 - 第一步
  registerStep1: {
    email: ['required', 'email'],
    password: ['required', 'password'],
    age: ['required', 'age'],
    gender: ['required']
  },

  // 注册表单 - 第二步
  registerStep2: {
    username: ['required', 'username'],
    relationshipStatus: ['required']
  },

  // 完整注册表单
  register: {
    email: ['required', 'email'],
    password: ['required', 'password'],
    username: ['required', 'username'],
    age: ['required', 'age'],
    gender: ['required'],
    relationshipStatus: ['required']
  },

  // 更新用户资料
  updateProfile: {
    username: ['username'],
    age: ['age'],
    bio: [validator.rules.get('maxLength')(500)]
  },

  // 修改密码
  changePassword: {
    oldPassword: ['required'],
    newPassword: ['required', 'password'],
    confirmPassword: [
      'required',
      {
        validator: (value, formData) => value === formData.newPassword,
        message: 'Passwords do not match'
      }
    ]
  },

  // 约会记录
  datingRecord: {
    name: ['required', validator.rules.get('maxLength')(50)],
    meetDate: ['required'],
    location: [validator.rules.get('maxLength')(100)],
    notes: [validator.rules.get('maxLength')(500)]
  },

  // 重要日期
  importantDate: {
    title: ['required', validator.rules.get('maxLength')(100)],
    date: ['required'],
    description: [validator.rules.get('maxLength')(300)]
  },

  // 社区帖子
  communityPost: {
    title: ['required', validator.rules.get('maxLength')(200)],
    content: ['required', validator.rules.get('maxLength')(2000)],
    type: ['required']
  },

  // 评论
  comment: {
    content: ['required', validator.rules.get('maxLength')(500)]
  }
}

// 实时验证混入
export const validationMixin = {
  data() {
    return {
      validationErrors: {},
      isFormValid: false
    }
  },
  methods: {
    // 验证单个字段
    validateField(field, value, rules) {
      const result = validator.validateField(value, rules)
      if (result.valid) {
        this.$delete(this.validationErrors, field)
      } else {
        this.$set(this.validationErrors, field, result.message)
      }
      this.updateFormValidation()
      return result.valid
    },

    // 验证整个表单
    validateForm(formData, validationRules) {
      const result = validator.validateForm(formData, validationRules)
      this.validationErrors = result.errors
      this.isFormValid = result.isValid
      return result.isValid
    },

    // 更新表单验证状态
    updateFormValidation() {
      this.isFormValid = Object.keys(this.validationErrors).length === 0
    },

    // 清除验证错误
    clearValidationErrors() {
      this.validationErrors = {}
      this.isFormValid = false
    },

    // 获取字段错误信息
    getFieldError(field) {
      return this.validationErrors[field] || ''
    },

    // 检查字段是否有错误
    hasFieldError(field) {
      return !!this.validationErrors[field]
    }
  }
}