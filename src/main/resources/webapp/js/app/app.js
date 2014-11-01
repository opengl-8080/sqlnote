angular
.module('sqlnote', ['angularLocalStorage', 'ngCookies'])
.controller('MainController', function($scope) {
    toastr.options = {
        "positionClass": "toast-bottom-right"
    };
})
.controller('SqlSelectionListController', function($scope, sqlResource, parameterStorageService) {
    sqlResource
        .getAllSqls()
        .success(function(sqls) {
            $scope.main.sqls = sqls;
            
            parameterStorageService.removeDeletedSqlParameter(sqls);
        });
    
    $scope.selectSql = function(sql) {
        sqlResource
            .getSqlDetail(sql.id)
            .success(function(sql) {
                parameterStorageService.save($scope.main.sql);
                
                $scope.main.sql = sql;
                
                parameterStorageService.load($scope);
            });
    };
})
.service('parameterStorageService', function($window, storage) {
    this.save = function(sql) {
        if (sql) {
            var key = toKey(sql.id);
            
            storage.remove(key);
            
            var values = _.map(sql.parameters, function(param) {
                return param.value;
            });
            
            storage.set(key, values);
        }
    };
    
    this.load = function($scope) {
        var storedValues = storage.get(toKey($scope.main.sql.id));
        
        if (!storedValues) {
            return;
        }
        
        _.each($scope.main.sql.parameters, function(nouse, i) {
            var value = storedValues[i] || '';
            $scope.main.sql.parameters[i].value = value;
        });
    };
    
    this.removeDeletedSqlParameter = function(sqls) {
        var ids = buildIdHashSet(sqls);
        
        forEachParameterKeys(function(key) {
            var id = getSqlId(key);
            
            if (!(id in ids)) {
                storage.remove(key);
            }
        });
    };
    
    function buildIdHashSet(sqls) {
        return _.reduce(sqls, function(memo, sql) {
            memo[sql.id] = 0;
            return memo;
        }, {});
    }
    
    function forEachParameterKeys(callback) {
        var locatStorage = $window.localStorage;
        
        for (var i=0; i<localStorage.length; i++) {
            var key = locatStorage.key(i);
            
            if (isParameterKey(key)) {
                callback(key);
            }
        }
    }
    
    function toKey(id) {
        return 'sql_' + id + '.parameters';
    }
    
    function getSqlId(key) {
        return key.replace(/^sql_|\.parameters$/g, '') - 0;
    }
    
    function isParameterKey(key) {
        return key.match(/^sql_\d+\.parameters$/);
    }
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
.controller('ParameterController', function($scope) {
    $scope.addParameter = function() {
        $scope.main.sql.parameters.push({
            name: '',
            type: 'STRING'
        });
    };
    
    $scope.removeParameter = function($index) {
        $scope.main.sql.parameters.splice($index, 1);
    };
})
.controller('ExecuteSqlController', function($scope, sqlResource, loading) {
    $scope.executeSql = function() {
        loading.show();
        
        sqlResource
            .executeSql($scope.main.sql, $scope.main.selectedDataSourceId)
            .then(function(response) {
                $scope.main.queryResult = response.data;
                
                var columns = _.map(response.data.metaData, function(col) {
                    return {field: col.name, title: col.name};
                });
            })
            .finally(function() {
                loading.hide();
            });
    };
})
.controller('SelectDataSourceController', function($scope, dataSourceResource, storage) {
    storage.bind($scope, 'main.selectedDataSourceId');
    
    dataSourceResource
        .getAllDataSources()
        .then(function(response) {
            $scope.main.dataSources = response.data;
        });
})
.controller('DataSourceController', function($scope) {
    // no implementation?
})
.controller('DataSourceListController', function($scope, dataSourceResource) {
    dataSourceResource
        .getAllDataSources()
        .then(function(response) {
            $scope.dsMain.dataSources = response.data;
        });
    
    $scope.initModifyDataSource = function(ds) {
        dataSourceResource
            .getDataSourceDetail(ds.id)
            .then(function(response) {
                $scope.dsMain.ds = response.data;
                $scope.dsMain.isUpdate = true;
                $scope.dsMain.isCreate = false;
                $scope.dsMain.errorMessage = '';
            });
    };
})
.controller('InitCreateDataSourceController', function($scope) {
    $scope.initCreateDataSource = function() {
        $scope.dsMain.ds = {};
        $scope.dsMain.isCreate = true;
        $scope.dsMain.isUpdate = false;
        $scope.dsMain.errorMessage = '';
    };
})
.controller('DataSourceMaintenanceController', function($scope, dataSourceResource) {
    $scope.verifyDataSource = function() {
        $scope.dsMain.errorMessage = '';
        
        dataSourceResource
            .verify($scope.dsMain.ds.id)
            .success(function(data, status) {
                if (status === 202) {
                    $scope.dsMain.errorMessage = data.message;
                    toastr.warning('Unconnectable!!');
                } else {
                    toastr.info('Connectable!!');
                }
            });
    }
    
    $scope.saveDataSource = function() {
        $scope.dsMain.errorMessage = '';
        
        dataSourceResource
            .saveDataSource($scope.dsMain.ds)
            .then(function(response) {
                if (response.status === 202) {
                    toastr.warning('Save DataSource. But Unconnectable!!');
                    $scope.dsMain.errorMessage = response.data.message;
                } else {
                    toastr.info('Save DataSource');
                }
                
                return dataSourceResource.getAllDataSources();
            })
            .then(function(response) {
                $scope.dsMain.dataSources = response.data;
            });
    };
    
    $scope.createDataSource = function() {
        $scope.dsMain.errorMessage = '';

        dataSourceResource
            .createDataSource($scope.dsMain.ds)
            .then(function(response) {
                if (response.status === 202) {
                    toastr.warning('Create DataSource. But Unconnectable!!');
                    $scope.dsMain.errorMessage = response.data.message;
                } else {
                    toastr.info('Create DataSource');
                }
                
                return dataSourceResource.getAllDataSources();
            })
            .then(function(response) {
                $scope.dsMain.dataSources = response.data;
            });
    };
    
    $scope.deleteDataSource = function() {
        if (!confirm('削除しますか？')) {
            return false;
        }
        
        $scope.dsMain.errorMessage = '';
        
        dataSourceResource
            .deleteDataSource($scope.dsMain.ds.id)
            .then(function() {
                toastr.info('Delete DataSource');
                $scope.dsMain.ds = null;
                return dataSourceResource.getAllDataSources();
            })
            .then(function(response) {
                $scope.dsMain.dataSources = response.data;
            });
    };
})
.controller('SystemConfigurationController', function($scope, systemConfigurationResource) {
    systemConfigurationResource
        .get()
        .success(function(data) {
            $scope.cfMain.config = data;
        });
    
    $scope.save = function() {
        systemConfigurationResource
            .save($scope.cfMain.config)
            .success(function() {
                toastr.info('Save System Configuration');
            });
    };
})
.service('sqlResource', function($http, $log, $filter) {
    this.getAllSqls = function() {
        $log.debug('sqlResource getAllSqls');
        return $http.get('/sqlnote/api/sql').error(handlerError);
    };
    
    this.addSql = function() {
        $log.debug('sqlResource addSql');
        return $http.post('/sqlnote/api/sql').error(handlerError);
    };
    
    this.getSqlDetail = function(id) {
        $log.debug('sqlResource getSqlDetail');
        return $http.get('/sqlnote/api/sql/' + id).error(handlerError);
    };
    
    this.deleteSql = function(id) {
        return $http.delete('/sqlnote/api/sql/' + id).error(handlerError);
    };
    
    this.putSql = function(sql) {
        return $http.put('/sqlnote/api/sql/' + sql.id, sql).error(handlerError);
    }
    
    this.executeSql = function(sql, dataSourceId) {
        var params = _.reduce(sql.parameters, function(p, parameter) {
            p.s[parameter.name] = parameter.value;
            return p;
        }, {
            dataSource: dataSourceId,
            s: {}
        });
        
        var that = this;
        
        return $http
                    .get('/sqlnote/api/sql/' + sql.id + '/result?' + $.param(params))
                    .error(function(response, status) {
                        if (status === 303) {
                            that.downloadCsv(response);
                        } else {
                            handlerError(response);
                        }
                    });
    }
    
    var numberFilter = $filter('number');
    
    this.downloadCsv = function(response) {
        var cnt = numberFilter(response.recordCount);
        var msg = '検索結果： ' + cnt + ' 件\n'
                 + '件数が多いので CSV でダウンロードします。\n'
                 + 'よろしいですか？'
        
        if (confirm(msg)) {
            window.location.href = response.url;
        }
    };
    
    function handlerError(response) {
        alert(response.message || 'エラーが発生しました。');
    }
})
.service('dataSourceResource', function($http) {
    this.getAllDataSources = function() {
        return $http.get('/sqlnote/api/dataSource').error(handlerError);
    };
    
    this.getDataSourceDetail = function(id) {
        return $http.get('/sqlnote/api/dataSource/' + id).error(handlerError);
    };
    
    this.verify = function(id) {
        return $http.get('/sqlnote/api/dataSource/' + id + '/verify').error(handlerError);
    };
    
    this.saveDataSource = function(ds) {
        return $http.put('/sqlnote/api/dataSource/' + ds.id, ds).error(handlerError);
    };
    
    this.createDataSource = function(ds) {
        return $http.post('/sqlnote/api/dataSource', ds).error(handlerError);
    };
    
    this.deleteDataSource = function(id) {
        return $http.delete('/sqlnote/api/dataSource/' + id).error(handlerError);
    };
    
    function handlerError(response) {
        alert(response.message || 'エラーが発生しました。');
    }
})
.service('systemConfigurationResource', function($http) {
    this.get = function() {
        return $http.get('/sqlnote/api/config').error(handlerError);
    };
    
    this.save = function(config) {
        return $http.put('/sqlnote/api/config', config).error(handlerError);
    };
    
    function handlerError(response) {
        alert(response.message || 'エラーが発生しました。');
    }
})
.service('loading', function() {
    this.show = function() {
        $.blockUI();
    };
    
    this.hide = function() {
        $.unblockUI();
    };
})
.directive('snTable', function($log) {
    return {
        link: function($scope, $element, $attr) {
            $element.bootstrapTable();
            
            $scope.$watch('main.queryResult', function() {
                var queryReulst = $scope.main.queryResult;
                
                if (!queryReulst) {
                    return;
                }
                
                var columns = _.map(queryReulst.metaData, function(col) {
                    return {
                        field: col.name,
                        title: col.name
                    }
                });
                
                $element.bootstrapTable('destroy')
                        .bootstrapTable({
                            columns: columns,
                            data: queryReulst.data,
                            height: $scope.main.resultPaneHeight
                        });
            });
            
            // $watch で main.resultPaneHeight を監視しようとしたが、動作しなかったので仕方なく関数で連携するようにした
            $scope.main.resizeResultTable = function(height) {
                $element.bootstrapTable('resetView', {height: height});
            };
        }
    };
})
.directive('snLayout', function($log) {
    var DEFAULT_RESULT_PANE_HEIGHT = 250;
    
    return {
        link: function($scope, $element, $attr) {
            $scope.main.resultPaneHeight = DEFAULT_RESULT_PANE_HEIGHT - 30;
            
            $element.layout({
                applyDefaultStyles: true,
                spacing_closed: 20,
                spacing_open: 8,
                east__size: 300,
                west__size: 350,
                south__size: DEFAULT_RESULT_PANE_HEIGHT,
                togglerLength_closed: '100%',
                enableCursorHotkey: false,
                south__onresize: function() {
                    var height = $element.find('.ui-layout-south').height();
                    $scope.main.resultPaneHeight = height;
                    $scope.main.resizeResultTable(height);
                }
            });
        }
    };
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

