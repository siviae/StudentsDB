var app = angular.module('employeeApp',[]);

app.controller('employeeAppController', function ($scope, $http) {
    $http.get('/getAll').success(function(data) {
        $scope.employees = data.employees;
        $scope.positions = data.positions;
    });
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
