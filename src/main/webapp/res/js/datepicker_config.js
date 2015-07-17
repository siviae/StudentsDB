var app = angular.module('employeeApp');


app.controller('tableDateController', function ($scope, $filter, global) {
    $scope.formatDate = function (date) {
        return $filter('date')(date, global.DATE_FORMAT());
    };
    global.enableDatePickerForScope($scope);
});

