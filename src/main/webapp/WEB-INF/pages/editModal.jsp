<script type="text/ng-template" id="editModalContent.html">
    <div class="modal-header">
        <button type="button" class="close" ng-click="closeModal()" ><span
                aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="editModalLabel">Сотрудник с ID {{employee.employeeID}}</h4>
    </div>
    <div class="modal-body">
        <div class="form-group" id="alertZone">

        </div>
        <div class="form-group">
            <label for="editFormInput1">Должность</label>
            <input type="text" class="form-control" id="editFormInput1"
                   placeholder="Должность" ng-model="employee.position.title">
        </div>
        <div class="form-group">
            <label for="editFormInput2">Фамилия</label>
            <input type="text" class="form-control" id="editFormInput2"
                   placeholder="Фамилия" ng-model="employee.surname">
        </div>
        <div class="form-group">
            <label for="editFormInput3">Имя</label>
            <input type="text" class="form-control" id="editFormInput3"
                   placeholder="Имя" ng-model="employee.firstName">
        </div>
        <div class="form-group">
            <label for="editFormInput4">Отчество</label>
            <input type="text" class="form-control" id="editFormInput4"
                   placeholder="Отчество" ng-model="employee.patronymic">
        </div>
        <div class="form-group">
            <label for="editFormInput5">Дата рождения</label>
            <p class="input-group">
                <input type="text" ng-model="employee.dateOfBirth" datepicker-options="dateOptions" id="editFormInput5"
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
        <button type="button" class="btn btn-primary"  ng-click="saveData()">Сохранить</button>
    </div>
</script>