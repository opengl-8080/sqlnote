angular
.module('sqlnote', [])
.controller('MainController', function($scope) {
    $('body').layout({
        applyDefaultStyles: true
    });
    
    toastr.options = {
        "positionClass": "toast-bottom-right"
    };
})
.controller('SqlSelectionListController', function($scope, sqlResource) {
    sqlResource
        .getAllSqls()
        .success(function(sqls) {
            $scope.main.sqls = sqls;
        });
    
    $scope.selectSql = function(sql) {
        sqlResource
            .getSqlDetail(sql.id)
            .success(function(sql) {
                $scope.main.sql = sql;
            });
    };
})
.controller('AddSqlController', function($scope, sqlResource, $log) {
    $scope.addSql = function() {
        sqlResource
            .addSql()
            .then(function() {
                $log.debug('end addSql');
                return sqlResource.getAllSqls();
            })
            .then(function(response) {
                $log.debug('end getAllSqls');
                $scope.main.sqls = response.data;
                toastr.info('Create new SQL');
            });
    };
})
.controller('EditorController', function($scope, $log) {
    var jsEditor = CodeMirror.fromTextArea(document.getElementById('editor'), {
        mode: "sql",
        lineNumbers: true,
        indentUnit: 4,
        lineWrapping: false
    });
    
    $scope.$watch('main.sql', function() {
        if ($scope.main.sql) {
            jsEditor.setValue($scope.main.sql.sql);
        }
    });
    
    $scope.main.getSqlEditorValue = function() {
        return jsEditor.getValue();
    };
})
.controller('DeleteSqlController', function($scope, sqlResource) {
    $scope.deleteSql = function() {
        if (!confirm('削除しますか？')) {
            return false;
        }
        
        sqlResource
            .deleteSql($scope.main.sql.id)
            .then(function() {
                $scope.main.sql = null;
            })
            .then(function() {
                return sqlResource.getAllSqls();
            })
            .then(function(response) {
                $scope.main.sqls = response.data;
                toastr.info('Delete SQL');
            });
    };
})
.controller('SaveSqlController', function($scope, sqlResource, $log) {
    $scope.saveSql = function() {
        var sql = $scope.main.sql;
        sql.sql = $scope.main.getSqlEditorValue();
        
        sqlResource
            .putSql(sql)
            .then(function() {
                return sqlResource.getAllSqls();
            })
            .then(function(response) {
                $scope.main.sqls = response.data;
                toastr.info('Save SQL');
            });
    };
})
.service('sqlResource', function($http, $log) {
    this.getAllSqls = function() {
        $log.debug('sqlResource getAllSqls');
        return $http.get('/sqlnote/api/sql');
    };
    
    this.addSql = function() {
        $log.debug('sqlResource addSql');
        return $http.post('/sqlnote/api/sql');
    };
    
    this.getSqlDetail = function(id) {
        $log.debug('sqlResource getSqlDetail');
        return $http.get('/sqlnote/api/sql/' + id);
    };
    
    this.deleteSql = function(id) {
        return $http.delete('/sqlnote/api/sql/' + id);
    };
    
    this.putSql = function(sql) {
        return $http.put('/sqlnote/api/sql/' + sql.id, sql);
    }
})
.filter('dataType', function($log) {
    return function(type) {
        if (type === 'STRING') {
            return '文字列';
        } else if (type === 'NUMBER') {
            return '数値';
        } else if (type === 'DATE') {
            return '日付';
        } else {
            return '文字列';
        }
    };
})
;

