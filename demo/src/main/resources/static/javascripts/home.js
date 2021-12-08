function refreshEditOption(name, id){
console.log("hi");
var data = {name : "name"};
$.ajax({
            url: '/save',
            method: 'GET',
            data: data,
            dataType: 'json',
            success: function (response) {
                               console.log('result is ',response);
            },
            error: function (response) {
            }
        })};