<!DOCTYPE html>
<html ng-app="employeeApp">
<head lang="en">
    <meta http-equiv='Content-Type' content='text/html; charset=UTF-8'/>
    <title>База сотрудников</title>
    <link rel="stylesheet" href="/res/plugins/bootstrap-3.3.5-dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="/res/plugins/ui-select/select.min.css">
    <%--
        <script src="/res/plugins/angular.min.js"></script>--%>
    <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.4.3/angular.js"></script>
    <script src="//code.angularjs.org/1.2.20/angular-sanitize.min.js"></script>
    <script src="/res/plugins/ui-bootstrap-tpls-0.13.0.min.js"></script>
    <script src="/res/plugins/ui-select/select.min.js"></script>
    <script src="/res/js/common.js"></script>
    <script src="/res/js/datepicker_config.js"></script>
</head>
<body ng-controller="employeeAppController">

<%@include file="editModal.jsp" %>
<%@include file="addModal.jsp" %>
<%@include file="deleteModal.jsp" %>

<div class="container" ng-controller="tableDateController">
    <div ng-controller="AlertController">
        <alert ng-repeat="alert in alerts" type="{{alert.status}}">{{alert.message}}
            <button type="button" class="close" ng-click="closeAlert($index)"><span
                    aria-hidden="true">&times;</span></button>
        </alert>
    </div>
    <div class="row" style="padding: 10px 0 10px 0;">
        <div class="col-xs-2">
            <button type="button" id="openModalButton" class="btn btn-primary" ng-click="openAddModal()">
                Добавить сотрудника
            </button>
        </div>
        <div class="col-xs-3">
        </div>
        <div class="col-xs-3">
            <h4><%--<?= $recordsOnPage ?> записей на странице--%></h4>
        </div>
        <div class="col-xs-4">
            <h4><%--Страница <?= $page + 1 ?> из <?= $maxPage + 1 ?>--%></h4>
        </div>
    </div>
    <table class="table table-bordered table-hover">
        <thead>
        <tr>
            <th style="width: 10%">ID</th>
            <th style="width: 15%">Должность</th>
            <th>Фамилия</th>
            <th>Имя</th>
            <th>Отчество
                <a href="#" onclick="changeOrder('price','<?= $sort_by ?>','<?= $order ?>','');">
                    <%--<span style="color: grey" class="glyphicon
                        <?php if ($sort_by == "productID"): ?>glyphicon-sort<?php endif; ?>
                        <?php if ($sort_by == "price" && $order == "asc"): ?>glyphicon-sort-by-attributes<?php endif; ?>
                        <?php if ($sort_by == "price" && $order == "desc"): ?>glyphicon-sort-by-attributes-alt<?php endif; ?>
                        pull-right" aria-hidden="true">
                        </span>--%>
                </a>
            </th>
            <th style="width: 20%">Дата рождения</th>
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
        <tr class="editableRow" ng-repeat="employee in employees" ng-click="editEmployee(employee)">
            <td class="employeeID">{{employee.employeeID}}</td>
            <td class="position">{{employee.position.title}}</td>
            <td class="surname">{{employee.surname}}</td>
            <td class="firstName">{{employee.firstName}}</td>
            <td class="patronymic">{{employee.patronymic}}</td>
            <td class="dateOfBurth">
                <h4><span class="label label-info center-block">{{formatDate(employee.dateOfBirth)}}</span></h4>
            </td>
            <td class="prodButtons" style="width:100px;">
                <button type="button" class="btn btn-default center-block" aria-label="Удалить"
                        ng-click="openDeleteModal($event,employee)">
                    <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
                </button>
            </td>
        </tr>
        </tbody>
    </table>
</div>
</body>
</html>