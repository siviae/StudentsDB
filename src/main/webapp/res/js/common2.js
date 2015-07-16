var app = angular.module('employeeApp', ['ui.bootstrap']);

function reloadTable($http, $scope){
    $http.get('/getAll').success(function (data) {
        $scope.employees = data.employees;
        $scope.positions = data.positions;
    });
}

app.controller('employeeAppController', function ($scope, $modal, $http, $log) {
    reloadTable($http,$scope);
    $scope.alerts = [];
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

        modalInstance.result.then(function (employee) {
        }, function () {
            $log.info(JSON.stringify(employee) + '; Modal dismissed at: ' + new Date());
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
                $scope.alerts.push(data);
                angular.copy($scope.employee,  $scope.oldEmployee);
                $modalInstance.close();
            });

    };
    $scope.closeModal = function () {
        $scope.oldEmployee = angular.copy($scope.employee);
        $modalInstance.dismiss('cancel');
    };
});

app.controller('AlertController', function ($scope) {
    $scope.closeAlert = function(index) {
        $scope.alerts.splice(index, 1);
    };
});


/*

 app.directive('selectPicker', function ($timeout) {
 return {
 link: function (scope, element, attr) {
 $timeout(function() {
 element.selectpicker({
 size: 2
 });
 });
 }
 };
 });*/
