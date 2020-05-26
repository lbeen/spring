const pathName = window.document.location.pathname;
const projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1) + '/';

Vue.prototype.projectName = projectName;

Vue.prototype.httpGet = function (url, param, callback) {
    var self = this;
    this.$http.get(projectName + url, {
        params: param
    }).then(function (res) {
        let data = res.body;
        if (data && data.msg) {
            self.$message.success(data.msg);
        }
        if (callback) {
            callback(data);
        }
    }).catch(function (res) {
        console.log(res);
        self.$message.error('操作失败');
    });
};

Vue.prototype.postEmulateJSON = function (url, param, success, error) {
    var self = this;
    this.$http.post(projectName + url, param, {
        emulateJSON: true
    }).then(function (res) {
        let data = res.body;
        if (data.code === 0) {
            if (data.msg) {
                self.$message.success(data.msg);
            }
            if (success) {
                success(data.data);
            }
        } else {
            self.$message.error(data.msg);
            if (error) {
                error();
            }
        }
    }).catch(function (res) {
        console.log(res);
        self.$message.error('操作失败');
    });
};

Vue.prototype.httpPost = function (url, param, success, error) {
    var self = this;
    this.$http.post(projectName + url, param).then(function (res) {
        let data = res.body;
        if (data.code === 0) {
            if (data.msg) {
                self.$message.success(data.msg);
            }
            if (success) {
                success(data.data);
            }
        } else {
            self.$message.error(data.msg);
            if (error) {
                error();
            }
        }
    }).catch(function (res) {
        console.log(res);
        self.$message.error('操作失败');
    });
};