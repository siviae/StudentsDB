var app = angular.module('employeeApp', ['ui.bootstrap', 'ui.select', 'ngSanitize']);

app.factory('global', function ($filter) {
    var result = {};

    result.pageAlert = function (data, scope) {
        if (data && data.status && data.message) {
            scope.alerts.push(data);
        } else {
            alert(data);
        }
    };
    result.DATE_FORMAT = function () {
        return 'dd.MM.yyyy';
    };

    result.reloadTable =
        function (http, scope) {
            scope.tableStatus.block();
            var positionID = "";
            if (scope.filter.position) positionID = scope.filter.position.positionID;
            var dateOfBirth = "";
            if (scope.filter.dateOfBirth) dateOfBirth = $filter('date')(scope.filter.dateOfBirth, result.DATE_FORMAT());
            http({
                method: 'GET',
                url: '/getAll',
                contentType: "application/json",
                params: {
                    employeeID: scope.filter.employeeID,
                    firstName: scope.filter.firstName,
                    surname: scope.filter.surname,
                    patronymic: scope.filter.patronymic,
                    dateOfBirth: dateOfBirth,
                    positionID: positionID,
                    sort: scope.pageSort.by,
                    sortOrder: scope.pageSort.order,
                    limit: scope.tableStatus.limit
                }
            })
                .success(function (data, status, headers, config) {
                    scope.employees.length = 0;
                    scope.positions.length = 0;
                    scope.employees.push.apply(scope.employees, data.employees);
                    scope.positions.push.apply(scope.positions, data.positions);
                    scope.tableStatus.unblock();
                })
                .error(function (data, status, headers, config) {
                    result.pageAlert({status: "danger", message: "Не удалось обновить список пользователей"}, scope);
                    scope.tableStatus.unblock();
                });
        };
    result.enableDatePickerForScope = function (scope) {

        scope.clear = function () {
            scope.selectedDate = null;
        };


        scope.openDatePicker = function ($event) {
            $event.preventDefault();
            $event.stopPropagation();
            scope.opened = true;
        };

        scope.dateOptions = {
            startingDay: 1
        };

        scope.dateFormat = result.DATE_FORMAT();
        return scope;
    };
    return result;
});

app.controller('employeeAppController', function ($scope, $modal, $http, $log, global) {
    $scope.filter = {};
    $scope.pageSort = {by: "surname", order: "asc"};
    $scope.tableStatus = {
        blocked: false,
        block: function () {
            this.blocked = true;
        },
        unblock: function () {
            this.blocked = false;
        },
        limit: 10
    };
    $scope.alerts = [];
    $scope.employees = [];
    $scope.positions = [];

    $scope.needMore = function(){
        return $scope.employees.length==$scope.tableStatus.limit;
    };

    global.reloadTable($http, $scope);
    $scope.reloadTable = function () {
        global.reloadTable($http, $scope);
    };
    $scope.showMore = function(){
        $scope.tableStatus.limit+=20;
        $scope.reloadTable();
    };


    $scope.changeSorting = function (by) {
        function opposite(order) {
            return order == 'asc' ? 'desc' : 'asc';
        }

        if ($scope.pageSort.by == by) {
            $scope.pageSort.order = opposite($scope.pageSort.order);
        } else {
            $scope.pageSort.by = by;
            $scope.pageSort.order = 'asc';
        }
        $scope.reloadTable();
    };

    $scope.editEmployee = function (employee) {

        var modalInstance = $modal.open({
            templateUrl: 'editModalContent.html',
            controller: 'editModalController',
            scope: $scope,
            resolve: {
                employee: function () {
                    return employee;
                }
            }
        });

    };
    $scope.openAddModal = function (employee) {

        var modalInstance = $modal.open({
            templateUrl: 'addModalContent.html',
            controller: 'addModalController',
            scope: $scope
        });
    };
    $scope.openDeleteModal = function ($event, employee) {
        $event.stopPropagation();
        var modalInstance = $modal.open({
            templateUrl: 'deleteModalContent.html',
            controller: 'deleteModalController',
            scope: $scope,
            resolve: {
                employee: function () {
                    return employee;
                }
            }
        });
    }


});

app.controller('editModalController', function ($scope, $modalInstance, $http, employee, global) {
    $scope.employee = employee;
    $scope.oldEmployee = angular.copy(employee);
    $scope.modalStatus = {operationInProcess: false};
    $scope.saveData = function () {
        $scope.modalStatus.operationInProcess = true;
        $http({
            method: 'POST',
            url: '/editEmployee',
            contentType: "application/json",
            data: $scope.employee})
            .success(function (data, status, headers, config) {
                if (data.status == "success") {
                    angular.copy($scope.employee, $scope.oldEmployee);
                }
                global.pageAlert(data, $scope);
                $scope.modalStatus.operationInProcess = false;
                $modalInstance.close();
            })
            .error(function (data, status, headers, config) {
                global.pageAlert(data, $scope);
                $scope.modalStatus.operationInProcess = false;
                $modalInstance.close();
            });

    };
    $scope.closeModal = function () {
        $modalInstance.close();
    };

    $scope.$on('modal.closing', function (event, data) {
        angular.copy($scope.oldEmployee, $scope.employee);
    });
    global.enableDatePickerForScope($scope);
});

app.controller('addModalController', function ($scope, $modalInstance, $http, global) {
    $scope.employee = {firstName: "", surname: "", patronymic: "", positionID: "", dateOfBirth: ""};
    $scope.modalStatus = {operationInProcess: false};
    $scope.addEmployee = function () {
        $scope.modalStatus.operationInProcess = true;
        $http({
            method: 'POST',
            url: '/addEmployee',
            contentType: "application/json",
            data: $scope.employee})
            .success(function (data, status, headers, config) {
                if (data.status == "success") {
                    global.reloadTable($http, $scope);
                }
                global.pageAlert(data, $scope);
                $scope.modalStatus.operationInProcess = false;
                $modalInstance.close();
            })
            .error(function (data, status, headers, config) {
                global.pageAlert(data, $scope);
                $scope.modalStatus.operationInProcess = false;
                $modalInstance.close();
            });

    };
    $scope.closeModal = function () {
        $modalInstance.dismiss('cancel');
    };
    global.enableDatePickerForScope($scope);
});

app.controller('deleteModalController', function ($scope, $modalInstance, $http, employee, global) {
    $scope.employee = employee;
    $scope.modalStatus = {operationInProcess: false};
    $scope.deleteEmployee = function () {
        $scope.modalStatus.operationInProcess = true;
        $http({
            method: 'POST',
            url: '/deleteEmployee',
            params: {
                employeeID: employee.employeeID
            }
        })
            .success(function (data, status, headers, config) {
                if (data.status == "success") {
                    global.reloadTable($http, $scope);
                }
                global.pageAlert(data, $scope);
                $scope.modalStatus = {operationInProcess: false};
                $modalInstance.close();
            })
            .error(function (data, status, headers, config) {
                global.pageAlert(data, $scope);
                $scope.modalStatus = {operationInProcess: false};
                $modalInstance.close();
            });

    };
    $scope.closeModal = function () {
        $modalInstance.dismiss('cancel');
    };
});

app.controller('AlertController', function ($scope) {
    $scope.closeAlert = function (index) {
        $scope.alerts.splice(index, 1);
    };
});