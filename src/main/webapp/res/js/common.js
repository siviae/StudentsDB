var app = angular.module('employeeApp', ['ui.bootstrap']);

function reloadTable(http, scope){
    http.get('/getAll').success(function (data) {
        scope.employees.length = 0;
        scope.positions.length = 0;
        scope.employees.push.apply(scope.employees,data.employees);
        scope.positions.push.apply(scope.positions,data.positions);
    });
}

function pageAlert(data, scope, modalInstance){
    if(data && data.success && data.message){
        scope.alerts.push(data);
    } else {
        alert(data);
    }
    modalInstance.close();
}

app.controller('employeeAppController', function ($scope, $modal, $http, $log) {
    reloadTable($http,$scope);
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
    $scope.openDeleteModal = function ($event,employee) {
        $event.stopPropagation();
        var modalInstance = $modal.open({
            templateUrl: 'deleteModalContent.html',
            controller: 'deleteModalController',
            scope: $scope,
            resolve:{
                employee: function(){
                    return employee;
                }
            }
        });
    }


});

app.controller('editModalController', function ($scope, $modalInstance, $http, employee) {
    $scope.employee = employee;
    $scope.oldEmployee = angular.copy($scope.employee);
    $scope.saveData = function () {
        $http({
            method: 'POST',
            url: '/editEmployee',
            contentType: "application/json",
            data: $scope.employee})
            .success(function (data, status, headers, config) {
                $scope.alerts.push(data);
                if(data.status!="success"){
                    angular.copy($scope.employee,  $scope.oldEmployee);
                }
                $modalInstance.close();
            })
            .error(function (data, status, headers, config) {
                angular.copy($scope.employee,  $scope.oldEmployee);
                pageAlert(data,$scope,$modalInstance);
            });

    };
    $scope.closeModal = function () {
        $scope.oldEmployee = angular.copy($scope.employee);
        $modalInstance.dismiss('cancel');
    };
});

app.controller('addModalController', function ($scope, $modalInstance, $http) {
    $scope.employee = {firstName: "", surname: "", patronymic: "", positionID: "", dateOfBirth: ""};
    $scope.addEmployee = function () {
        $http({
            method: 'POST',
            url: '/addEmployee',
            contentType: "application/json",
            data: $scope.employee})
            .success(function (data, status, headers, config) {
                $scope.alerts.push(data);
                if(data.status=="success"){
                    reloadTable($http, $scope);
                }
                $modalInstance.close();
            })
            .error(function (data, status, headers, config) {
                pageAlert(data,$scope,$modalInstance);
            });

    };
    $scope.closeModal = function () {
        $modalInstance.dismiss('cancel');
    };
});

app.controller('deleteModalController', function ($scope, $modalInstance, $http, employee) {
    $scope.employee=employee;
    $scope.deleteEmployee = function () {
        $http({
            method: 'POST',
            url: '/deleteEmployee',
            params: {
                employeeID: employee.employeeID
            }
        })
        .success(function (data, status, headers, config) {
            $scope.alerts.push(data);
            if(data.status=="success"){
                reloadTable($http, $scope);
            }
            $modalInstance.close();
        })
        .error(function (data, status, headers, config) {
                pageAlert(data,$scope,$modalInstance);
        });

    };
    $scope.closeModal = function () {
        $modalInstance.dismiss('cancel');
    };
});

app.controller('AlertController', function ($scope) {
    $scope.closeAlert = function(index) {
        $scope.alerts.splice(index, 1);
    };
});