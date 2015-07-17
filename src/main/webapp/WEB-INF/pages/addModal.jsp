<script type="text/ng-template" id="addModalContent.html">
    <div class="modal-header">
        <button type="button" class="close" ng-click="closeModal()" ><span
                aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="addModalLabel">Добавление нового сотрудника</h4>
    </div>
    <div class="modal-body">
        <div class="form-group" id="alertZone">

        </div>
        <div class="form-group">
            <label for="addFormInput1">Должность</label>
            <input type="text" class="form-control" id="addFormInput1"
                   placeholder="Должность" ng-model="employee.position.positionID">
        </div>
        <div class="form-group">
            <label for="addFormInput2">Фамилия</label>
            <input type="text" class="form-control" id="addFormInput2"
                   placeholder="Фамилия" ng-model="employee.surname">
        </div>
        <div class="form-group">
            <label for="addFormInput3">Имя</label>
            <input type="text" class="form-control" id="addFormInput3"
                   placeholder="Имя" ng-model="employee.firstName">
        </div>
        <div class="form-group">
            <label for="addFormInput4">Отчество</label>
            <input type="text" class="form-control" id="addFormInput4"
                   placeholder="Отчество" ng-model="employee.patronymic">
        </div>
        <div class="form-group">
            <label for="addFormInput5">Дата рождения</label>
            <p class="input-group">
                <input type="text" ng-model="employee.dateOfBirth" datepicker-options="dateOptions" id="addFormInput5"
                       class="form-control" datepicker-popup="{{dateFormat}}"
                is-open="opened" ng-required="true"/>
                    <span class="input-group-btn">
                        <button type="button" class="btn btn-default" ng-click="openDatePicker($event)"><i
                                class="glyphicon glyphicon-calendar"></i></button>
                    </span>
            </p>
        </div>
    </div>
    <div class="modal-footer">
        <button type="button" class="btn btn-default"  ng-click="closeModal()">Закрыть</button>
        <button type="button" class="btn btn-primary"  ng-click="addEmployee()">Сохранить</button>
    </div>
</script>