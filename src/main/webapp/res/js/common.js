$(document).ready(function () {
    var input1 = $("#formInput1");
    var input2 = $("#formInput2");
    var input3 = $("#formInput3");
    var input4 = $("#formInput4");
    var input5 = $("#formInput5");
    var formInputs = [input1, input2, input3, input4, input5];
    var modal = $("#addProductModal");
    var addButton = $("#productAddButton");
    var deleteButton = $("#productDeleteButton");
    var deleteModal = $("#maybeDeleteModal");
    var modalAlertZone = $("#alertZone");
    var globalAlertZone = $("#globalAlert");

    function make_global_warning(msg) {
        $("#addProductModal").modal("hide");
        var cl;
        if (msg.type == "success") cl = "alert-success";
        if (msg.type == "error") cl = "alert-danger";
        globalAlertZone.html('<div class="alert ' + cl + '" role="alert">' + msg.message + '</div>');
        setTimeout(function () {
            document.location.reload();
        }, 1500);
    }

    function make_modal_warning(msg) {
        modalAlertZone.html('<div class="alert alert-danger" role="alert">' + msg + '</div>');
        setTimeout(function () {
            modalAlertZone.html("");
        }, 1500)

    }

    function showPreviewImage() {
        input5.html("<img style='width:100%' src='" + input4.val() + "'/>");
    }

    input4.on('input', showPreviewImage);

    function clearInputs() {
        formInputs.forEach(function (item) {
            item.val('');
        });
        input5.html('');
    }


    function prepareForAdding() {
        $("#addProductModalLabel").html("Добавление товара");
        clearInputs();
        addButton.unbind('click');
        addButton.bind('click', addProduct);
        modal.modal('show');
    }

    function prepareForEditing(id, name, description, cost, imgUrl) {
        $("#addProductModalLabel").html("Редактирование товара с ID " + id);
        clearInputs();
        input1.val(name);
        input2.val(description);
        input3.val(cost);
        input4.val(imgUrl);
        showPreviewImage();
        addButton.unbind('click');
        addButton.bind('click', function () {
            editProduct(id);
        });
        modal.modal('show');
    }

    function sendDataToServer(url, productID) {
        var name = input1.val();
        var description = input2.val();
        var cost = input3.val();
        var link = input4.val();
        if (isNaN(parseInt(cost, 10))) {
            make_modal_warning("Неверная цена");
            return;
        }
        if (!name) {
            make_modal_warning("Название не может быть пустым");
            return;
        }
        $.ajax({
            type: "POST",
            url: url,
            data: {
                name: name,
                description: description,
                cost: cost,
                link: link,
                productID: productID
            },
            success: function (msg) {
                make_global_warning(JSON.parse(msg));
            },
            error: function (msg) {
                alert("Сервер недоступен: " + JSON.stringify(msg));
            }
        });
    }

    function editProduct(id) {
        sendDataToServer("db/db_edit.php", id);
    }

    function addProduct() {
        sendDataToServer("db/db_insert.php");
    }

    function deleteProduct(id) {
        $.ajax({
            type: "POST",
            url: "db/db_delete.php",
            data: {
                productID: id
            },
            success: function (msg) {
                deleteModal.modal("hide");
                make_global_warning(JSON.parse(msg));
            },
            error: function (msg) {
                alert("Сервер недоступен: " + msg);
            }
        });
    }

    function askForDelete(id, name) {
        $("#maybeDeleteModalLabel").html('Удалить товар "' + name + '" с productID ' + id + "?");
        clearInputs();
        deleteButton.unbind('click');
        deleteButton.bind('click', function () {
            deleteProduct(id)
        });
        deleteModal.modal('show');
    }


    $("#openModalButton").click(prepareForAdding);
    $(".editableRow").each(function () {
        var id = $(this).children(".prodID").html();
        var name = $(this).children(".prodName").html();
        var description = $(this).children(".prodDescription").html();
        var cost = $(this).children(".prodPrice").html();
        var link = $(this).children(".prodImg").children().prop('src');

        var buttons = $(this).children(".prodButtons").children();
        buttons.first()
            .click(function () {
                prepareForEditing(id, name, description, cost, link);
            });
        buttons.last()
            .click(function () {
                askForDelete(id, name);
            });
    })
});