'use strict'
;
Vue.component('pageTable', {
    props: {
        url: {
            type: String,
            default: ''
        },
        param: {
            type: Object,
            default: null
        },
        paramFun: {
            type: Function,
            default: function () {
                return {}
            }
        },
        pageSizes: {
            type: Array,
            default: function () {
                return [10, 20, 50, 100];
            }
        },
        pageSize: {
            type: Number,
            default: 10
        },
        tableMaxHeight: {
            type: String,
            default: ' '
        }
    },
    data: function () {
        return {
            myPageSize: 10,
            currentPage: 1,
            total: 0,
            tableData: [],
            tableLoading:false
        }
    },
    created: function () {
        this.myPageSize = this.pageSize;
        this.loadData();
    },
    methods: {
        loadData: function (currentPage) {
            if (currentPage) {
                this.currentPage = currentPage;
            }

            let param = this.param;
            if (!param) {
                param = this.paramFun();
            }
            param.skip = this.myPageSize * (this.currentPage - 1);
            param.limit = this.myPageSize;

            let self = this;
            this.tableLoading = true;
            this.total = 0;
            this.tableData = [];
            this.httpGet(this.url, param, function (data) {
                if (data) {
                    self.total = data.total;
                    self.tableData = data.list;
                }
                self.tableLoading = false;
            });
        },
        handleSizeChange: function (pageSize) {
            this.myPageSize = pageSize;
            this.loadData();
        },
        handleCurrentChange: function (currentPage) {
            this.currentPage = currentPage;
            this.loadData();
        }
    },
    template:
        '<div>' +
        '   <el-row>' +
        '       <slot name="search" :loadData="loadData"></slot>' +
        '   </el-row>' +
        '   <el-row>' +
        '       <el-table :data="tableData" border :max-height="tableMaxHeight" v-loading="tableLoading" stripe>' +
        '           <slot name="table"></slot>' +
        '       </el-table>' +
        '   </el-row>' +
        '   <el-row>' +
        '       <el-pagination' +
        '               style="text-align: right"' +
        '               @size-change="handleSizeChange"' +
        '               @current-change="handleCurrentChange"' +
        '               :current-page="currentPage"' +
        '               :page-sizes="pageSizes"' +
        '               :page-size="myPageSize"' +
        '               layout="total, sizes, prev, pager, next"' +
        '               :total="total">' +
        '       </el-pagination>' +
        '   </el-row>' +
        '</div>'
});

Vue.component('dicSelect', {
    props: {
        value: {
            type: String,
            default: ''
        },
        dicType: {
            type: String,
            default: ''
        },
        placeholder: {
            type: String,
            default: ''
        }
    },
    data: function () {
        return {
            selectValue: '',
            items: []
        }
    },
    created: function () {
        this.getItems();
    },
    watch: {
        value: function (newVal) {
            this.selectValue = newVal;
        },
        selectValue: function (newVal) {
            this.$emit('value', newVal)
        }
    },
    methods: {
        getItems: function () {
            let self = this;
            this.httpGet('dic/getUsedDics', {type: this.dicType}, function (data) {
                if (data) {
                    for (let i = 0; i < data.length; i++) {
                        self.items.push({
                            label: data[i].name,
                            value: data[i].code
                        });
                    }
                }
            });
        }
    },
    template:
        '<el-select v-model="selectValue" :placeholder="placeholder" size="small" clearable>' +
        '   <el-option v-for="item in items" :label="item.label" :value="item.value"></el-option>' +
        '</el-select>'
});

Vue.component('dicName', {
    props: {
        type: {
            type: String,
            default: ''
        },
        code: {
            type: String,
            default: ''
        }
    },
    data: function () {
        return {
            name: ''
        }
    },
    created: function () {
        this.getDicName();
    },
    methods: {
        getDicName: function () {
            let self = this;
            this.httpGet('dic/getUsedDic', {
                type: this.type,
                code: this.code
            }, function (data) {
                if (data) {
                    self.name = data.name;
                }
            });
        }
    },
    template: '<span>{{name}}</span>'
});