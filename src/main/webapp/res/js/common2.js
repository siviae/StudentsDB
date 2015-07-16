var app = angular.module('employeeApp',['ui.bootstrap']);

app.controller('employeeAppController', function ($scope, $modal, $http,$log) {
    $http.get('/getAll').success(function(data) {
        $scope.employees = data.employees;
        $scope.positions = data.positions;
    });
    $scope.editEmployee = function (employee) {

        var modalInstance = $modal.open({
            templateUrl: 'editModalContent.html',
            controller: 'editModalController',
            resolve: {
                employee: function(){
                    return employee;
                }
            }
        });

        modalInstance.result.then(function (employee) {
        }, function () {
            $log.info(JSON.stringify(employee)+'; Modal dismissed at: ' + new Date());
        });
    }


});

app.controller('editModalController', function ($scope, $modalInstance, employee) {
    $scope.employee = employee;
    $scope.saveData = function(){
        alert(JSON.stringify($scope.employee));
        $modalInstance.close($scope.employee);
    };
    $scope.closeModal = function(){
        alert(JSON.stringify($scope.employee));
        $modalInstance.dismiss('cancel');
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
