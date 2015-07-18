<script type="text/ng-template" id="addModalContent.html">
    <form name="addForm">
        <div class="modal-header">
            <button type="button" class="close" ng-click="closeModal()"><span
                    aria-hidden="true">&times;</span></button>
            <h4 class="modal-title" id="addModalLabel">Добавление нового сотрудника</h4>
        </div>
        <div class="modal-body">
            <div class="form-group" id="alertZone">

            </div>
            <div class="form-group">
                <label for="addFormInput1">Должность</label>
                <ui-select ng-model="employee.position"
                           reset-search-input="false" name="position"
                           theme="bootstrap" required>
                    <ui-select-match placeholder="Не выбрана" allow-clear="true">
                        {{$select.selected.title}}
                    </ui-select-match>
                    <ui-select-choices repeat="position in positions | filter: $select.search">
                        <div ng-bind-html="position.title | highlight: $select.search"></div>
                    </ui-select-choices>
                </ui-select>
                <span style="color:red"
                      ng-show="addForm.position.$dirty && addForm.position.$invalid">
                    Поле не может быть пустым
                </span>
            </div>
            <div class="form-group">
                <label for="addFormInput2">Фамилия</label>
                <input type="text" class="form-control" id="addFormInput2" name="surname"
                       placeholder="Фамилия" ng-model="employee.surname" required>
                    <span style="color:red"
                          ng-show="addForm.surname.$dirty && addForm.surname.$invalid">
                        Поле не может быть пустым
                </span>
            </div>
            <div class="form-group">
                <label for="addFormInput3">Имя</label>
                <input type="text" class="form-control" id="addFormInput3" name="firstName"
                       placeholder="Имя" ng-model="employee.firstName" required>
                    <span style="color:red"
                          ng-show="addForm.firstName.$dirty && addForm.firstName.$invalid">
                    Поле не может быть пустым
                    </span>
            </div>
            <div class="form-group">
                <label for="addFormInput4">Отчество</label>
                <input type="text" class="form-control" id="addFormInput4" name="patronymic"
                       placeholder="Отчество" ng-model="employee.patronymic" required>
                    <span style="color:red"
                          ng-show="addForm.patronymic.$dirty && addForm.patronymic.$invalid">
                        Поле не может быть пустым
                </span>
            </div>
            <div class="form-group">
                <label for="addFormInput5">Дата рождения</label>

                <p class="input-group">
                    <input type="text" ng-model="employee.dateOfBirth" datepicker-options="dateOptions"
                           id="addFormInput5" name="dateOfBirth"
                           class="form-control" datepicker-popup="{{dateFormat}}"
                           is-open="opened" ng-required="true" required>
                    <span class="input-group-btn">
                        <button type="button" class="btn btn-default" ng-click="openDatePicker($event)"><i
                                class="glyphicon glyphicon-calendar"></i></button>
                    </span>
                </p>
                    <span style="color:red"
                          ng-show="addForm.dateOfBirth.$dirty && addForm.dateOfBirth.$invalid">
                    Поле не может быть пустым и должно содержать дату в формате дд.мм.гггг
                    </span>
            </div>
        </div>
        <div class="modal-footer">
            <button type="button" class="btn btn-default" ng-click="closeModal()">Закрыть</button>
            <button type="button" ng-disabled="addForm.$invalid || modalStatus.operationInProcess"
                    class="btn btn-primary" ng-click="addEmployee()">
                Сохранить
            </button>
        </div>
    </form>
</script>