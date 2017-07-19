<script type="text/ng-template" id="deleteModalContent.html">
    <div class="modal-header">
        <button type="button" class="close" ng-click="closeModal()"><span
                aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="addModalLabel">Удалить сотрудника {{student.surname}} {{student.firstName}} ?</h4>
    </div>
    <div class="modal-footer">
        <button type="button" class="btn btn-default" ng-click="closeModal()">Закрыть</button>
        <button type="button" class="btn btn-danger" ng-disabled="modalStatus.operationInProcess"
                ng-click="deleteEmployee()">Удалить
        </button>
    </div>
</script>