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
            cellStyle: {
                'text-align': 'center'
            },
            headCellStyle: {
                'text-align': 'center',
                'background-color': '#f5f7fa'
            }
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
            param.skip = this.pageSize * (this.currentPage - 1);
            param.limit = this.pageSize;


            this.$http.get(this.url, {
                params: param
            }).then(function (res) {
                let data = res.body;
                if (data) {
                    this.total = data.total;
                    this.tableData = data.list;
                } else {
                    this.total = [];
                    this.tableData = [];
                    console.log(res);
                }
            }).catch(function (res) {
                console.log(res);
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
        '       <el-table :data="tableData" border :cell-style="cellStyle" :header-cell-style="headCellStyle"' +
        '                 :max-height="tableMaxHeight">' +
        '           <slot name="table"></slot>' +
        '       </el-table>' +
        '   </el-row>' +
        '   <el-row>' +
        '       <el-pagination' +
        '               style="text-align: right"' +
        '               @size-change="handleSizeChange"' +
        '               @current-change="handleCurrentChange"' +
        '               :current-page="myPageSize"' +
        '               :page-sizes="pageSizes"' +
        '               :page-size="myPageSize"' +
        '               layout="total, sizes, prev, pager, next"' +
        '               :total="total">' +
        '       </el-pagination>' +
        '   </el-row>' +
        '</div>'
});