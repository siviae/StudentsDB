<!DOCTYPE html>
<html ng-app="employeeApp">
<head lang="en">
    <meta http-equiv='Content-Type' content='text/html; charset=UTF-8'/>
    <title>База сотрудников</title>
    <link rel="stylesheet" href="./res/plugins/bootstrap-3.3.5-dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="./res/plugins/ui-select/select.min.css">
    <script src="./res/plugins/angular.min.js"></script>
    <script src="./res/plugins/angular-sanitize.min.js"></script>
    <script src="./res/plugins/ui-bootstrap-tpls-0.13.0.min.js"></script>
    <script src="./res/plugins/ui-select/select.min.js"></script>
    <script src="./res/js/common.js"></script>
    <script src="./res/js/datepicker_config.js"></script>
    <style>
        .non-clickable {
            pointer-events: none;
            opacity: 0.5;
        }
    </style>
</head>
<body ng-controller="employeeAppController">

<%@include file="editModal.jsp" %>
<%@include file="addModal.jsp" %>
<%@include file="deleteModal.jsp" %>

<div class="container" ng-controller="tableDateController">
    <div ng-controller="AlertController" style="margin-top: 10px">
        <alert ng-repeat="alert in alerts" type="{{alert.status}}">{{alert.message}}
            <button type="button" class="close" ng-click="closeAlert($index)"><span
                    aria-hidden="true">&times;</span></button>
        </alert>
    </div>
    <div class="row" style="padding: 0 0 10px 0;">
        <div class="col-xs-2">
            <button type="button" id="openModalButton" class="btn btn-primary" ng-click="openAddModal()">
                Добавить сотрудника
            </button>
        </div>
    </div>
    <table class="table table-bordered table-hover" ng-class="{'non-clickable': tableStatus.blocked}">
        <thead>
        <tr>
            <th style="width: 10%">ID</th>
            <th style="width: 15%">Должность</th>
            <th>Фамилия
                <span style="color: grey"
                      ng-click="changeSorting('surname')"
                      class="glyphicon glyphicon-sort pull-right"
                      ng-class="{'glyphicon-sort-by-alphabet' : pageSort.by == 'surname' && pageSort.order == 'asc',
                                 'glyphicon-sort-by-alphabet-alt' : pageSort.by == 'surname' && pageSort.order == 'desc'}"
                      aria-hidden="true">
                </span>
            </th>
            <th>Имя
                <span style="color: grey"
                      ng-click="changeSorting('firstName')"
                      class="glyphicon glyphicon-sort pull-right"
                      ng-class="{'glyphicon-sort-by-alphabet' : pageSort.by == 'firstName' && pageSort.order == 'asc',
                                 'glyphicon-sort-by-alphabet-alt' : pageSort.by == 'firstName' && pageSort.order == 'desc'}"
                      aria-hidden="true">
                </span>
            </th>
            <th>Отчество
                    <span style="color: grey"
                          ng-click="changeSorting('patronymic')"
                          class="glyphicon glyphicon-sort pull-right"
                          ng-class="{'glyphicon-sort-by-alphabet' : pageSort.by == 'patronymic' && pageSort.order == 'asc',
                                     'glyphicon-sort-by-alphabet-alt' : pageSort.by == 'patronymic' && pageSort.order == 'desc'}"
                          aria-hidden="true">
                    </span>
            </th>
            <th style="width: 20%">Дата рождения
                <span style="color: grey"
                      ng-click="changeSorting('dateOfBirth')"
                      class="glyphicon glyphicon-sort pull-right"
                      ng-class="{'glyphicon-sort-by-attributes' : pageSort.by == 'dateOfBirth' && pageSort.order == 'asc',
                                         'glyphicon-sort-by-attributes-alt' : pageSort.by == 'dateOfBirth' && pageSort.order == 'desc'}"
                      aria-hidden="true">
                </span>
            </th>
            <th>Действия</th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td><input type="text" class="form-control" ng-model="filter.employeeID" ng-change="reloadTable()"></td>
            <td>
                <ui-select ng-model="filter.position"
                           reset-search-input="false"
                           ng-change="reloadTable()"
                           theme="bootstrap">
                    <ui-select-match placeholder="Не выбрана" allow-clear="true">
                        {{$select.selected.title}}
                    </ui-select-match>
                    <ui-select-choices repeat="position in positions | filter: $select.search">
                        <div ng-bind-html="position.title | highlight: $select.search"></div>
                    </ui-select-choices>
                </ui-select>
            </td>
            <td><input type="text" class="form-control"
                       ng-change="reloadTable()" ng-model="filter.surname"></td>
            <td><input type="text" class="form-control"
                       ng-change="reloadTable()" ng-model="filter.firstName"></td>
            <td><input type="text" class="form-control"
                       ng-change="reloadTable()" ng-model="filter.patronymic"></td>
            <td>
                <p class="input-group">
                    <input type="text" ng-model="filter.dateOfBirth"
                           ng-change="reloadTable()"
                           datepicker-options="dateOptions" class="form-control" datepicker-popup="{{dateFormat}}"
                           is-open="opened" ng-required="true"/>
                    <span class="input-group-btn">
                        <button type="button" class="btn btn-default" ng-click="openDatePicker($event)"><i
                                class="glyphicon glyphicon-calendar"></i></button>
                    </span>
                </p>
            </td>
            <td>
                <button type="button" class="btn btn-success center-block" aria-label="Обновить"
                        ng-click="reloadTable()">
                    <span class="glyphicon glyphicon-refresh" aria-hidden="true"></span>
                </button>
            </td>
        </tr>
        <tr class="editableRow" ng-repeat="student in students  track by student.employeeID" ng-click="editEmployee(student)">
            <td class="employeeID">{{student.employeeID}}</td>
            <td class="position">{{student.position.title}}</td>
            <td class="surname">{{student.surname}}</td>
            <td class="firstName">{{student.firstName}}</td>
            <td class="patronymic">{{student.patronymic}}</td>
            <td class="dateOfBurth">
                <h4><span class="label label-info center-block">{{formatDate(student.dateOfBirth)}}</span></h4>
            </td>
            <td class="prodButtons" style="width:100px;">
                <button type="button" class="btn btn-default center-block" aria-label="Удалить"
                        ng-click="openDeleteModal($event,student)">
                    <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
                </button>
            </td>
        </tr>
        </tbody>
    </table>
    <button type="button" id="showMoreButton" ng-show="needMore()"
            style="width: 100%; opacity: 0.6; margin-bottom: 10px" class="btn btn-default" ng-click="showMore()">
        Показать больше результатов
    </button>
</div>
</body>
</html>