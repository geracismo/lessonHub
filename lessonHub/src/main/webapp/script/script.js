var newData = {
    "type": "add_delete_course_professor",
    "params": {
        "mode": 0
    },
    "data": {
        "elem" : {
            "course": {
                "id": 0,
                "name": "Programmazione III"
            },
            "professor":{
                "id": 0,
                "name": "Liliana",
                "surname": "Ardissono"
            }
        }
    },
    "authorization":{
        "email": null,
        "password": null
    }
};

var newData2 = "{\"type\":\"add_delete_course_professor\",\"params\":{\"mode\":0},\"data\":{\"elem\":{\"course\":{\"id\":0,\"name\":\"Programmazione III\"},\"professor\":{\"id\":0,\"name\":\"Liliana\",\"surname\":\"Ardissono\"}}},\"authorization\":{\"email\":null,\"password\":null}}";
var json = {
    "type": "login",
    "params": null,
    "authorization":{
        "email": null,
        "password": null
    }
};

var dataJson = JSON.stringify(newData);

$(document).ready(function(){
    $.ajax({
        type: 'POST',
        url: "http://localhost:8080/TWEB/ServletBooking",
        data: JSON.stringify({
			type: "login",
			params: null,
			authorization:{
				email: "user",
				password: "pwd"
			}
		}),
        success: function(successdata){console.log(successdata)},
        failure: function(errMsg) {
            alert(errMsg);
        }
	});
});