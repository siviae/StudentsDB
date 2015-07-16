<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@page pageEncoding="utf-8" %>
<!DOCTYPE html>
<html ng-app="employeeApp">
<head lang="en">
    <meta http-equiv='Content-Type' content='text/html; charset=UTF-8'/>
    <title>База сотрудников</title>
    <link rel="stylesheet" href="/res/plugins/bootstrap-3.3.5-dist/css/bootstrap.min.css"><%--
    <script src="/res/plugins/angular.min.js"></script>--%>
    <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.4.3/angular.js"></script>
    <script src="/res/plugins/ui-bootstrap-tpls-0.13.0.min.js"></script>
    <script src="/res/js/common2.js"></script>
</head>
<body ng-controller="employeeAppController">
<h1>{{selectedDate}}{{selectedPosition}}</h1>

<script type="text/ng-template" id="editModalContent.html">
        <div class="modal-header">
            <button type="button" class="close" ng-click="closeModal()" ><span
                    aria-hidden="true">&times;</span></button>
            <h4 class="modal-title" id="addProductModalLabel">Сотрудник с ID {{employee.employeeID}}</h4>
        </div>
        <div class="modal-body">
            <div class="form-group" id="alertZone">

            </div>
            <div class="form-group">
                <label for="formInput1">Должность</label>
                <input type="text" class="form-control" id="formInput1"
                       placeholder="Должность" ng-model="employee.position.title">
            </div>
            <div class="form-group">
                <label for="formInput2">Фамилия</label>
                <input type="text" class="form-control" id="formInput2"
                       placeholder="Фамилия" ng-model="employee.surname">
            </div>
            <div class="form-group">
                <label for="formInput3">Имя</label>
                <input type="text" class="form-control" id="formInput3"
                       placeholder="Имя" ng-model="employee.firstName">
            </div>
            <div class="form-group">
                <label for="formInput4">Отчество</label>
                <input type="text" class="form-control" id="formInput4"
                       placeholder="Отчество" ng-model="employee.patronymic">
            </div>
            <div class="form-group">
                <label for="formInput5">Дата рождения</label>
                <input type="text" class="form-control" id="formInput5"
                       placeholder="" ng-model="employee.dateOfBirth">
            </div>
        </div>
        <div class="modal-footer">
            <button type="button" class="btn btn-default"  ng-click="closeModal()">Закрыть</button>
            <button type="button" class="btn btn-primary"  ng-click="saveData()">Сохранить</button>
        </div>
</script>

<div class="modal fade" id="maybeDeleteModal" tabindex="-1" role="dialog" aria-labelledby="maybeDeleteModalLabel">

    <form>
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                            aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="maybeDeleteModalLabel"></h4>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">Отмена</button>
                    <button id="productDeleteButton" type="button" class="btn btn-danger">Удалить</button>
                </div>
            </div>
        </div>
    </form>
</div>
<div class="container">
    <div id="globalAlert">
    </div>
    <div class="row" style="padding: 10px 0 10px 0;">
        <div class="col-xs-2">
            <button type="button" id="openModalButton" class="btn btn-primary">
                Добавить товар
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
            <th>ID</th>
            <th>Должность</th>
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
            <th>Дата рождения</th>
            <th>Действия</th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td><input type="search" class="form-control" ng-model="selectedID"></td>
            <td>
                <select ng-model="selectedPosition">
                    <option ng-repeat="position in positions" ng-value="position.positionID">{{position.title}}</option>
                </select>
            </td>
            <td><input type="search" class="form-control" ng-model="selectedSurname"></td>
            <td><input type="search" class="form-control" ng-model="selectedFirstName"></td>
            <td><input type="search" class="form-control" ng-model="selectedPatronymic"></td>
            <td><input type="search" class="form-control" data-provide="datepicker" ng-model="selectedDate"></td>
            <td></td>
        </tr>
        <tr class="editableRow" ng-repeat="employee in employees" ng-click="editEmployee(employee)">
            <td class="employeeID">{{employee.employeeID}}</td>
            <td class="position">{{employee.position.title}}</td>
            <td class="surname">{{employee.surname}}</td>
            <td class="firstName">{{employee.firstName}}</td>
            <td class="patronymic">{{employee.patronymic}}</td>
            <td class="dateOfBurth">{{employee.dateOfBirth}}</td>
            <td class="prodButtons" style="width:100px;">
                <button type="button" class="btn btn-default center-block" aria-label="Удалить" ng-click="">
                    <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
                </button>
            </td>
        </tr>
        </tbody>
    </table>
</div>
</body>
</html>