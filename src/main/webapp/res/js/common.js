var app = angular.module('employeeApp', ['ui.bootstrap','ui.select','ngSanitize']);

app.factory('global', function($filter) {
    var result = {};

    result.pageAlert = function (data, scope) {
        if (data && data.success && data.message) {
            scope.alerts.push(data);
        } else {
            alert(data);
        }
    };
    result.DATE_FORMAT = function(){
        return 'dd.MM.yyyy';
    };

    result.reloadTable =
        function (http, scope) {
            var positionID = "";
            if(scope.filter.position) positionID = scope.filter.position.positionID;
            var dateOfBirth = "";
            if(scope.filter.dateOfBirth) dateOfBirth= $filter('date')(scope.filter.dateOfBirth, result.DATE_FORMAT());
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
                        positionID: positionID
                    }
                })
                .success(function (data, status, headers, config) {
                    scope.employees.length = 0;
                    scope.positions.length = 0;
                    scope.employees.push.apply(scope.employees, data.employees);
                    scope.positions.push.apply(scope.positions, data.positions);
                })
                .error(function (data, status, headers, config) {
                    result.pageAlert({status: "danger", message: "Не удалось обновить список пользователей"}, scope);
                });
        };
    result.enableDatePickerForScope = function(scope){

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

app.controller('employeeAppController', function ($scope, $modal, $http, $log,global) {
    $scope.filter = {};
    global.reloadTable($http, $scope);
    $scope.reloadTable = function() {
        global.reloadTable($http, $scope);
    };
    $scope.alerts = [];
    $scope.employees = [];
    $scope.positions = [];
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

app.controller('editModalController', function ($scope, $modalInstance, $http, employee,global) {
    $scope.employee = employee;
    $scope.oldEmployee = angular.copy(employee);
    $scope.saveData = function () {
        $http({
            method: 'POST',
            url: '/editEmployee',
            contentType: "application/json",
            data: $scope.employee})
            .success(function (data, status, headers, config) {
                if (data.status == "success") {
                    angular.copy($scope.employee,$scope.oldEmployee);
                }
                global.pageAlert(data, $scope);
                $modalInstance.close();
            })
            .error(function (data, status, headers, config) {
                global.pageAlert(data, $scope);
                $modalInstance.close();
            });

    };
    $scope.closeModal = function () {
        $modalInstance.close();
    };

    $scope.$on('modal.closing', function (event, data) {
        angular.copy($scope.oldEmployee,$scope.employee);
    });
    global.enableDatePickerForScope($scope);
});

app.controller('addModalController', function ($scope, $modalInstance, $http, global) {
    $scope.employee = {firstName: "", surname: "", patronymic: "", positionID: "", dateOfBirth: ""};
    $scope.addEmployee = function () {
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
                $modalInstance.close();
            })
            .error(function (data, status, headers, config) {
                global.pageAlert(data, $scope);
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
    $scope.deleteEmployee = function () {
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
                $modalInstance.close();
            })
            .error(function (data, status, headers, config) {
                global.pageAlert(data, $scope);
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