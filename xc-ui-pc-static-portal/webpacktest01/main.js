// 导入model01
const {add} = require("./model01")
const Vue = require("./vue.min")


// 编写MVVM中的model部分以及VM部分
const VM = new Vue({
    el: '#app',   // vm接管了app区域的管理
    data: {       // model数据
        name: '哈哈哈',
        num1: 1,  // model变,Dom变
        num2: 0,
        result: 0,
        url: 'https//:www.baidu.com'
    },
    methods: {
        change: function () {
            this.result = add(Number.parseInt(this.num1), Number.parseInt(this.num2));
        }
    }
});