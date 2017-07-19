<script type="text/ng-template" id="editModalContent.html">
    <form name="editForm">
        <div class="modal-header">
            <button type="button" class="close" ng-click="closeModal()"><span
                    aria-hidden="true">&times;</span></button>
            <h4 class="modal-title" id="editModalLabel">Сотрудник с ID {{student.employeeID}}</h4>
        </div>
        <div class="modal-body">
            <div class="form-group" id="alertZone">

            </div>
            <div class="form-group">
                <label for="editFormInput1">Должность</label>
                <ui-select ng-model="student.position"
                           name="position"
                           reset-search-input="false"
                           theme="bootstrap" required>
                    <ui-select-match placeholder="Не выбрана" allow-clear="true">
                        {{$select.selected.title}}
                    </ui-select-match>
                    <ui-select-choices repeat="position in positions | filter: $select.search">
                        <div ng-bind-html="position.title | highlight: $select.search"></div>
                    </ui-select-choices>
                </ui-select>

                    <span style="color:red" ng-show="editForm.position.$invalid">
                    <span ng-show="editForm.position.$error.required">Поле не может быть пустым</span>
                </span>
            </div>
            <div class="form-group">
                <label for="editFormInput2">Фамилия</label>
                <input type="text" class="form-control" id="editFormInput2" name="surname"
                       placeholder="Фамилия" ng-model="student.surname" required>
                <span style="color:red" ng-show="editForm.surname.$invalid">
                    <span ng-show="editForm.surname.$error.required">Поле не может быть пустым</span>
                </span>
            </div>
            <div class="form-group">
                <label for="editFormInput3">Имя</label>
                <input type="text" class="form-control" id="editFormInput3" name="firstName"
                       placeholder="Имя" ng-model="student.firstName" required>
                <span style="color:red" ng-show="editForm.firstName.$invalid">
                    <span ng-show="editForm.firstName.$error.required">Поле не может быть пустым</span>
                </span>
            </div>
            <div class="form-group">
                <label for="editFormInput4">Отчество</label>
                <input type="text" class="form-control" id="editFormInput4" name="patronymic"
                       placeholder="Отчество" ng-model="student.patronymic" required>
                <span style="color:red" ng-show="editForm.patronymic.$invalid">
                    <span ng-show="editForm.patronymic.$error.required">Поле не может быть пустым</span>
                </span>
            </div>
            <div class="form-group">
                <label for="editFormInput5">Дата рождения</label>

                <p class="input-group">
                    <input type="text" ng-model="student.dateOfBirth" datepicker-options="dateOptions"
                           name="dateOfBirth"
                           id="editFormInput5"
                           class="form-control" datepicker-popup="{{dateFormat}}"
                           is-open="opened" required>
                    <span class="input-group-btn">
                        <button type="button" class="btn btn-default" ng-click="openDatePicker($event)"><i
                                class="glyphicon glyphicon-calendar"></i></button>
                    </span>
                </p>
                    <span style="color:red" ng-show="editForm.dateOfBirth.$invalid">
                    <span ng-show="editForm.dateOfBirth.$error.required">Поле не может быть пустым, либо заполнено неверно</span>
                </span>
            </div>
        </div>
        <div class="modal-footer">
            <button type="button"
                    class="btn btn-default"
                    ng-click="closeModal()">Закрыть
            </button>
            <button type="button" ng-disabled="editForm.$invalid  || modalStatus.operationInProcess"
                    class="btn btn-primary" ng-click="saveData()">
                Сохранить
            </button>
        </div>
    </form>
</script>