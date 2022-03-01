Vue.directive('toast', {
    inserted: function(el){
        $(el).show();
    }
})

new Vue({
    el: '#App',
    data: function() {
        return{
            api: "http://127.0.0.1:8080/TWEB/ServletBooking",
            page: "",
            hover: {
                guest: "",
                logged: "Home",
                admin: "Corso"
            },
            user: {
                username: null,
                password: null,
                name: "",
                surname: "",
                role: -1,
                mfx: ""
            },
            admin: {
                page: ""
            },
            newDocente: {
                dname: "",
                dsurname: "",
                gender: '',
                id: -1,
            },
            newCorso: {
                cname: "",
                id: -1,
            },
            newInsegnamento: {
                cid: -1,
                did: -1,
            },
            adminProfessors: [],
            adminCourses: [],
            adminTeachings: [],
            availableReservations: [],
            myReservations: [],
            adminHistory: [],
            history: [],
            spaceblank: " ",
            selectCourse: [],
            selectProfessor: [],
            notifications: []
        }
    },
    watch: {
        notifications: function(newelement){
            if(this.notifications.length > 5){
                for( ; this.notifications.length > 5; ){
                    this.notifications.splice(0, 1);
                }
            }
        }, deep: false
    },
    methods: {
        changePage: function(param1, param2){
            if(param2 == 1){
                this.page = "";
                this.admin.page = param1;
                //this.checkSession();
            }else{
                this.admin.page = "";
                this.page = param1;
            }
            if(param1 == "Docente"){
                this.loadDocenti();
            }
            if(param1 == "Corso"){
                this.loadCorsi();
            }
            if(param1 == "Insegnamento"){
                this.loadInsegnamenti();
                this.loadCorsi();
            }
            if(param1 == "myReservations"){
                this.loadMyReservations();
            }
            if(param1 == "History"){
                this.loadHistory();
            }
            if(param1 == "Home" || param1 == "Prenotazioni"){
                this.loadAvailableReservations();
            }
            if(param1 == "Full History"){
                this.loadFullHistory();
            }
        },
        cleanData: function(){
            this.page = "";
            this.user.name = "";
            this.user.surname = "";
            this.user.role = -1;
            this.user.mfx = "";
            this.admin.page = "";
            this.adminProfessors = [];
            this.adminCourses = [];
            this.adminTeachings = [];
            this.availableReservations = [];
            this.myReservations = [];
            this.adminHistory = [];
            this.history = [];
            this.selectCourse = [];
            this.selectProfessor = [];
        },
        logout: function() {
            var self = this;
            $.ajax({
                url: self.api,
                type: 'POST',
                data: JSON.stringify({
                        "type": "logout",
                        "params": null,
                        "authorization": {
                            "email": null,
                            "password": null
                        }
                    }),
                success: function(result){
                    console.log(result);
                    self.cleanData();
                }
            })
        },
        checkSession: function() {
            var self = this;
            $.ajax({
                url: self.api,
                type: 'POST',
                data: JSON.stringify({
                        "type": "check_session",
                        "params": null,
                        "authorization": {
                            "email": null,
                            "password": null
                        }
                    }),
                success: function(result){
                    console.log(result);
                    if(result.error){
                        self.cleanData();
                    }
                }
            })
        },
        login: function() {
            var self = this;
            var username = $('#username').val();
            var password = $('#password').val();
            $.ajax({
                url: self.api,
                type: 'POST',
                data: JSON.stringify({
                        "type": "login",
                        "params": null,
                        "authorization": {
                            "email": username,
                            "password": password
                        }
                    }),
                success: function(result){
                    console.log(result);
                    if(!result.error){
                        self.user.name = result.response.name;
                        self.user.surname = result.response.surname;
                        self.user.role = result.response.role;
                        self.user.mfx = result.response.mfx;
                        /*If works comment these lines*/
                        self.user.username = username;
                        self.user.password = password;
                        /*End of possible comment*/
                        self.page = "Home";
                        self.loadAvailableReservations();
                    }else{
                        self.notifications.push({type: "Errore", text: result.message});
                    }
                }
            })
        },
        loadDocenti: function(){
            var self = this;
            $.ajax({
                url: self.api,
                type: 'POST',
                data: JSON.stringify({
                    "type": "get_professors",
                    "params": null,
                    "authorization":{
                        "email": self.user.username,
                        "password": self.user.password
                    }
                }),
                success: function(result){
                    console.log(result);
                    if(!result.error){
                        self.adminProfessors = result.response.data;
                    }else{
                        if(result.message === "Authorization Error"){
                            self.cleanData();
                            self.notifications.push({type: "Permesso Negato", text: result.message});
                        }else{
                            self.notifications.push({type: "Errore", text: result.message});
                        }
                    }
                }
            })
        },
        loadCorsi: function(){
            var self = this;
            $.ajax({
                url: self.api,
                type: 'POST',
                data: JSON.stringify({
                    "type": "get_courses",
                    "params": null,
                    "authorization":{
                        "email": self.user.username,
                        "password": self.user.password
                    }
                }),
                success: function(result){
                    console.log(result);
                    if(!result.error){
                        self.adminCourses = result.response.data;
                        self.selectCourse = result.response.data;
                    }else{
                        if(result.message === "Authorization Error"){
                            self.cleanData();
                            self.notifications.push({type: "Permesso Negato", text: result.message});
                        }else{
                            self.notifications.push({type: "Errore", text: result.message});
                        }
                    }
                }
            })
        },
        professorByCourse: function(param){
            var self = this;
            self.newInsegnamento.did = -1;
            $.ajax({
                url: self.api,
                type: 'POST',
                data: JSON.stringify({
                    "type": "get_professors",
                    "params": {
                        "course": {
                            "id": parseInt(param.target.value)
                        }
                    },
                    "authorization":{
                        "email": self.user.username,
                        "password": self.user.password
                    }
                }),
                success: function(result){
                    console.log(result);
                    if(!result.error){
                        self.selectProfessor = result.response.data;
                    }else{
                        if(result.message === "Authorization Error"){
                            self.cleanData();
                            self.notifications.push({type: "Permesso Negato", text: result.message});
                        }else{
                            self.notifications.push({type: "Errore", text: result.message});
                        }
                    }
                }
            })
        },
        loadMyReservations: function(){
            var self = this;
            $.ajax({
                url: self.api,
                type: 'POST',
                data: JSON.stringify({
                    "type": "show_my_reservations",
                    "params": null,
                    "authorization":{
                        "email": self.user.username,
                        "password": self.user.password
                    }
                }),
                success: function(result){
                    console.log(result);
                    if(!result.error){
                        self.myReservations = result.response.data;
                    }else{
                        if(result.message === "Authorization Error"){
                            self.cleanData();
                            console.log("HERE");
                            self.notifications.push({type: "Permesso Negato", text: result.message});
                        }else{
                            self.notifications.push({type: "Errore", text: result.message});
                        }
                    }
                }
            })
        },
        loadAvailableReservations: function(){
            var self = this;
            $.ajax({
                url: self.api,
                type: 'POST',
                data: JSON.stringify({
                    "type": "show_available_reservations",
                    "params": null,
                    "authorization":{
                        "email": self.user.username,
                        "password": self.user.password
                    }
                }),
                success: function(result){
                    console.log(result);
                    if(!result.error){
                        self.availableReservations = result.response;
                    }else{
                        if(result.message === "Authorization Error"){
                            self.cleanData();
                            self.notifications.push({type: "Permesso Negato", text: result.message});
                        }else{
                            self.notifications.push({type: "Errore", text: result.message});
                        }
                    }
                }
            })
        },
        loadHistory: function(){
            var self = this;
            $.ajax({
                url: self.api,
                type: 'POST',
                data: JSON.stringify({
                    "type": "user_show_history",
                    "params": null,
                    "authorization":{
                        "email": self.user.username,
                        "password": self.user.password
                    }
                }),
                success: function(result){
                    console.log(result);
                    if(!result.error){
                        self.history = result.response.data;
                    }else{
                        if(result.message === "Authorization Error"){
                            self.cleanData();
                            self.notifications.push({type: "Permesso Negato", text: result.message});
                        }else{
                            self.notifications.push({type: "Errore", text: result.message});
                        }
                    }
                }
            })
        },
        loadInsegnamenti: function(){
            var self = this;
            $.ajax({
                url: self.api,
                type: 'POST',
                data: JSON.stringify({
                    "type": "get_teachings",
                    "params": null,
                    "authorization":{
                        "email": self.user.username,
                        "password": self.user.password
                    }
                }),
                success: function(result){
                    console.log(result);
                    if(!result.error){
                        self.adminTeachings = result.response.data;
                    }else{
                        if(result.message === "Authorization Error"){
                            self.cleanData();
                            self.notifications.push({type: "Permesso Negato", text: result.message});
                        }else{
                            self.notifications.push({type: "Errore", text: result.message});
                        }
                    }
                }
            })
        },
        confermaPrenotazione: function(param1, param2, param3, param4){
            var self = this;
            var teaching_id = parseInt(param1);
            var day = parseInt(param2);
            var hour = parseInt(param3);
            $.ajax({
                url: self.api,
                type: 'POST',
                data: JSON.stringify({
                    "type": "delete_done_reservation",
                    "params": {
                        "mode": 0
                    },
                    "data": {
                        "elem" : {
                            "reservation": {
                                "teaching": teaching_id,
                                "slot": {
                                    "day":day,
                                    "hour": hour
                                }
                            }
                        }
                    },
                    "authorization":{
                        "email": self.user.username,
                        "password": self.user.password
                    }
                }),
                success: function(result){
                    console.log(result);
                    if(!result.error){
                        self.myReservations.splice(parseInt(param4), 1);
                        self.notifications.push({type: "Prenotazione Effettuata", text: "Prenotazione effettuata, disponibile nello storico"});
                    }else{
                        if(result.message === "Authorization Error"){
                            self.cleanData();
                            self.notifications.push({type: "Permesso Negato", text: result.message});
                        }else{
                            self.notifications.push({type: "Errore", text: result.message});
                        }
                    }
                }
            })
        },
        annullaPrenotazione: function(param1, param2, param3, param4){
            var self = this;
            var teaching_id = parseInt(param1);
            var day = parseInt(param2);
            var hour = parseInt(param3);
            $.ajax({
                url: self.api,
                type: 'POST',
                data: JSON.stringify({
                    "type": "delete_done_reservation",
                    "params": {
                        "mode": 1
                    },
                    "data": {
                        "elem" : {
                            "reservation": {
                                "teaching": teaching_id,
                                "slot": {
                                    "day":day,
                                    "hour": hour
                                }
                            }
                        }
                    },
                    "authorization":{
                        "email": self.user.username,
                        "password": self.user.password
                    }
                }),
                success: function(result){
                    console.log(result);
                    if(!result.error){
                        self.myReservations.splice(parseInt(param4), 1);
                        self.notifications.push({type: "Prenotazione Cancellata", text: "Prenotazione cancellata con successo"});
                    }else{
                        if(result.message === "Authorization Error"){
                            self.cleanData();
                            self.notifications.push({type: "Permesso Negato", text: result.message});
                        }else{
                            self.notifications.push({type: "Errore", text: result.message});
                        }
                    }
                }
            })
        },
        annullaPrenotazioneAdmin: function(param1, param2, param3, param4, param5, param6){
            var self = this;
            var teaching_id = parseInt(param1);
            var day = parseInt(param2);
            var hour = parseInt(param3);
            $.ajax({
                url: self.api,
                type: 'POST',
                data: JSON.stringify({
                    "type": "delete_done_reservation",
                    "params": {
                        "mode": 1,
                        "username": param4
                    },
                    "data": {
                        "elem" : {
                            "reservation": {
                                "teaching": teaching_id,
                                "slot": {
                                    "day":day,
                                    "hour": hour
                                }
                            }
                        }
                    },
                    "authorization":{
                        "email": self.user.username,
                        "password": self.user.password
                    }
                }),
                success: function(result){
                    console.log(result);
                    if(!result.error){
                        self.notifications.push({type: "Prenotazione Cancellata", text: "Prenotazione cancellata con successo"});
                        self.loadFullHistory();
                    }else{
                        if(result.message === "Authorization Error"){
                            self.cleanData();
                            self.notifications.push({type: "Permesso Negato", text: result.message});
                        }else{
                            self.notifications.push({type: "Errore", text: result.message});
                        }
                    }
                }
            })
        },
        prenota: function(param1, param2) {
            var self = this;
            var day = parseInt((param2.split(';')[0]).split(' ')[1]);
            var hour = parseInt(((param2.split(';')[1]).split(' ')[1]).split(':')[0]);
            $.ajax({
                url: self.api,
                type: 'POST',
                data: JSON.stringify({
                    "type": "book_reservation",
                    "params": null,
                    "data": {
                        "elem" : {
                            "reservation": {
                                "teaching": parseInt(param1),
                                "slot": {
                                    "day": day,
                                    "hour": hour
                                }
                            }
                        }
                    },
                    "authorization":{
                        "email": self.user.username,
                        "password": self.user.password
                    }
                }),
                success: function(result){
                    console.log(result);
                    if(!result.error){
                        self.notifications.push({type: "Operazione Effettuata", text: "Prenotazione effettuata con successo, disponibile nelle prenotazioni attive"});
                        self.loadAvailableReservations();
                    }else{
                        if(result.message === "Authorization Error"){
                            self.cleanData();
                            self.notifications.push({type: "Permesso Negato", text: result.message});
                        }else{
                            self.notifications.push({type: "Errore", text: result.message});
                        }
                    }
                }
            })
        },
        loadFullHistory: function(){
            var self = this;
            $.ajax({
                url: self.api,
                type: 'POST',
                data: JSON.stringify({
                    "type": "admin_show_all_history",
                    "params": null,
                    "authorization":{
                        "email": self.user.username,
                        "password": self.user.password
                    }
                }),
                success: function(result){
                    console.log(result);
                    if(!result.error){
                        self.adminHistory = result.response.users;
                    }else{
                        if(result.message === "Authorization Error"){
                            self.cleanData();
                            self.notifications.push({type: "Permesso Negato", text: result.message});
                        }else{
                            self.notifications.push({type: "Errore", text: result.message});
                        }
                    }
                }
            })
        },
        addCorso: function(){
            var self = this;
            $.ajax({
                url: self.api,
                type: 'POST',
                data: JSON.stringify({
                    "type": "add_delete_course",
                    "params": {
                        "mode": 0
                    },
                    "data": {
                        "elem" : {
                            "course": {
                                "id": 0,
                                "name": self.newCorso.cname
                            }
                        }
                    },
                    "authorization":{
                        "email": self.user.username,
                        "password": self.user.password
                    }
                }),
                success: function(result){
                    console.log(result);
                    if(!result.error){
                        self.notifications.push({type: "Operazione Effettuata", text: "Corso aggiunto con successo"});
                        self.loadCorsi();
                    }else{
                        if(result.message === "Authorization Error"){
                            self.cleanData();
                            self.notifications.push({type: "Permesso Negato", text: result.message});
                        }else{
                            self.notifications.push({type: "Errore", text: result.message});
                        }
                    }
                }
            })
        },
        deleteCorso: function(param1, param2, param3){
            var self = this;
            $.ajax({
                url: self.api,
                type: 'POST',
                data: JSON.stringify({
                    "type": "add_delete_course",
                    "params": {
                        "mode": 1
                    },
                    "data": {
                        "elem" : {
                            "course": {
                                "id": parseInt(param2),
                                "name": param1
                            }
                        }
                    },
                    "authorization":{
                        "email": self.user.username,
                        "password": self.user.password
                    }
                }),
                success: function(result){
                    console.log(result);
                    if(!result.error){
                        self.adminCourses.splice(parseInt(param3), 1);
                        self.notifications.push({type: "Operazione Effettuata", text: "Corso eliminato con successo"});
                    }else{
                        if(result.message === "Authorization Error"){
                            self.cleanData();
                            self.notifications.push({type: "Permesso Negato", text: result.message});
                        }else{
                            self.notifications.push({type: "Errore", text: result.message});
                        }
                    }
                }
            })
        },
        addCorsoDocente: function(){
            var self = this;
            if(self.newInsegnamento.cid != -1 && self.newInsegnamento.did != -1){
                $.ajax({
                    url: self.api,
                    type: 'POST',
                    data: JSON.stringify({
                        "type": "add_delete_course_professor",
                        "params": {
                            "mode": 0
                        },
                        "data": {
                            "elem" : {
                                "course": {
                                    "id": self.newInsegnamento.cid
                                },
                                "professor":{
                                    "id": self.newInsegnamento.did
                                }
                            }
                        },
                        "authorization":{
                            "email": self.user.username,
                            "password": self.user.password
                        }
                    }),
                    success: function(result){
                        console.log(result);
                        if(!result.error){
                            self.notifications.push({type: "Operazione Effettuata", text: "Insegnamento aggiunto con successo"});
                            self.loadInsegnamenti();
                        }else{
                            if(result.message === "Authorization Error"){
                                self.cleanData();
                                self.notifications.push({type: "Permesso Negato", text: result.message});
                            }else{
                                self.notifications.push({type: "Errore", text: result.message});
                            }
                        }
                    }
                })
            }else{
                console.log("Required!");
            }
        },
        deleteCorsoDocente: function(param1, param2, param3){
            var self = this;
            $.ajax({
                url: self.api,
                type: 'POST',
                data: JSON.stringify({
                    "type": "add_delete_course_professor",
                    "params": {
                        "mode": 1
                    },
                    "data": {
                        "elem" : {
                            "course": {
                                "id": parseInt(param1)
                            },
                            "professor":{
                                "id": parseInt(param2)
                            }
                        }
                    },
                    "authorization":{
                        "email": self.user.username,
                        "password": self.user.password
                    }
                }),
                success: function(result){
                    console.log(result);
                    if(!result.error){
                        self.adminTeachings.splice(parseInt(param3), 1);
                        self.notifications.push({type: "Operazione Effettuata", text: "Insegnamento cancellato con successo"});
                    }else{
                        if(result.message === "Authorization Error"){
                            self.cleanData();
                            self.notifications.push({type: "Permesso Negato", text: result.message});
                        }else{
                            self.notifications.push({type: "Errore", text: result.message});
                        }
                    }
                }
            })
        },
        addDocente: function(){
            var self = this;
            $.ajax({
                url: self.api,
                type: 'POST',
                data: JSON.stringify({
                    "type": "add_delete_professor",
                    "params": {
                        "mode": 0
                    },
                    "data": {
                        "elem" : {
                            "professor":{
                                "id": 0,
                                "name": self.newDocente.dname,
                                "surname": self.newDocente.dsurname,
                                "mfx": self.newDocente.gender
                            }
                        }
                    },
                    "authorization":{
                        "email": self.user.username,
                        "password": self.user.password
                    }
                }),
                success: function(result){
                    console.log(result);
                    if(!result.error){
                        self.notifications.push({type: "Operazione Effettuata", text: "Docente aggiunto con successo"});
                        self.loadDocenti();
                    }else{
                        if(result.message === "Authorization Error"){
                            self.cleanData();
                            self.notifications.push({type: "Permesso Negato", text: result.message});
                        }else{
                            self.notifications.push({type: "Errore", text: result.message});
                        }
                    }
                }
            })
        },
        removeDocente: function(param1, param2){
            var self = this;
            $.ajax({
                url: self.api,
                type: 'POST',
                data: JSON.stringify({
                    "type": "add_delete_professor",
                    "params": {
                        "mode": 1
                    },
                    "data": {
                        "elem" : {
                            "professor":{
                                "id": parseInt(param1),
                                "name": "",
                                "surname": ""
                            }
                        }
                    },
                    "authorization":{
                        "email": self.user.username,
                        "password": self.user.password
                    }
                }),
                success: function(result){
                    console.log(result);
                    if(!result.error){
                        self.adminProfessors.splice(parseInt(param2), 1);
                        self.notifications.push({type: "Operazione Effettuata", text: "Docente cancellato con successo"});
                    }else{
                        if(result.message === "Authorization Error"){
                            self.cleanData();
                            self.notifications.push({type: "Permesso Negato", text: result.message});
                        }else{
                            self.notifications.push({type: "Errore", text: result.message});
                        }
                    }
                }
            })
        }
    }
})