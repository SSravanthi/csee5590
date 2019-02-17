let app = angular.module('toDoApp', ['ngSanitize']);
app.controller('toDoController', function ($scope) {

  $scope.todoListbgcolor = {
   "background-color" : "grey",
    //"background-image" : url("TDLimg.png"),

  }

  $scope.speaker = {};
  $scope.speaker.img = "TDLimg.png";
  //Default ToDo List
  $scope.todoList = [{todoText:'ICP1', status:'Pending'}, {todoText:'ICP2', status:'Pending'},
    {todoText:'ICP3', status:'Pending'}];
  updateAddTotal();

  $scope.addItem = function () {
    $scope.todoList.push({todoText:$scope.newItem, status:'Pending'});
    $scope.newItem= " ";
    $("#item").focus();
    updateAddTotal();
  };

  $scope.removeItem = function (index) {
    $scope.todoList.splice(index,1);
    updateAddTotal();

  };
  //modifying pending to done
  $scope.changeToDone = function(event){
    angular.element(event.target).parent().append("<span class='label success'>Done!</span>");
    angular.element(event.target).parent().attr("class", 'completed');
    angular.element(event.target).remove();
    updateRemoveTotal();

  };


  function updateAddTotal() {

    completed = $('.success').length;
    pending = $('.pending').length;

    if (completed > 0 || pending > 0) {
      $('.total').text(" Pending: " + pending + " Completed: " + completed);
    }

  }


  function updateRemoveTotal() {
    completed = $('.success').length;
    pending = $('.pending').length;

    if (completed > 0 || pending > 0) {
      $('.total').text(" Pending: " + pending + " Completed: " + completed);
    }
  }

});
