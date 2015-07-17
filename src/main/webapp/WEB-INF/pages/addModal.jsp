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
            <label for="formInput1">Должность</label>
            <input type="text" class="form-control" id="formInput1"
                   placeholder="Должность" ng-model="employee.position.positionID">
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
        <button type="button" class="btn btn-primary"  ng-click="addEmployee()">Сохранить</button>
    </div>
</script>